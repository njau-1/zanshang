package com.zanshang.controllers.web;

import com.zanshang.models.Payment;
import com.zanshang.services.PaymentTrapdoor;
import com.zanshang.services.payment.PaymentTrapdoorImpl;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.AlipayUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Lookis on 5/17/15.
 */
@Controller
@RequestMapping(value = AlipayController.ALIPAY_PATH)
public class AlipayController implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(getClass());

    public static final String ALIPAY_PATH = "/alipay";

    public static final String NOTIFY_PATH = "/notify";

    public static final String RETURN_FORM_PATH = "/returnform";

    public static final String RETURN_PATH = "/return";

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    PaymentTrapdoor paymentTrapdoor;

    @Value("${ALIPAY_SECRET}")
    String secret;

    private ApplicationContext applicationContext;

    @PostConstruct
    protected void init() {
        paymentTrapdoor = akkaTrapdoor.createTrapdoor(PaymentTrapdoor.class, PaymentTrapdoorImpl.class);
    }

    //支付宝支付回调
    @RequestMapping(value = NOTIFY_PATH)
    @ResponseBody
    public String notifyPath(@RequestParam("trade_status") String tradeStatus, @RequestParam("out_trade_no") String
            paymentId, @RequestParam("sign") String sign, HttpServletRequest request) {
        return valid(paymentId, tradeStatus, sign, request) ? "success" : "failed";
    }

    @RequestMapping(value = RETURN_FORM_PATH)
    public String returnFormPath(@RequestParam("out_trade_no") String paymentId, @RequestParam("trade_status") String
            tradeStatus, @RequestParam("sign") String sign, HttpServletRequest request) {
        valid(paymentId, tradeStatus, sign, request);
        Payment payment = paymentTrapdoor.get(new ObjectId(paymentId));
        String callback = applicationContext.getBean(payment.getType().getClz()).getCallback(payment.getArguments());
        return "redirect:" + callback;
    }

    @RequestMapping(value = RETURN_PATH)
    public ModelAndView returnPath(@RequestParam("out_trade_no") String paymentId, @RequestParam("trade_status")
    String tradeStatus, @RequestParam("sign") String sign, HttpServletRequest request) {
        valid(paymentId, tradeStatus, sign, request);
        Payment payment = paymentTrapdoor.get(new ObjectId(paymentId));
        ModelAndView mav = new ModelAndView("alipay_return");
        String callback = applicationContext.getBean(payment.getType().getClz()).getCallback(payment.getArguments());
        mav.addObject("callback", callback);
        return mav;
    }

    private boolean valid(String paymentId, String tradeStatus, String sign, HttpServletRequest request) {
        Map<String, String> data = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            data.put(name, valueStr);
        }

        if ((StringUtils.equals("TRADE_SUCCESS", tradeStatus) || StringUtils.equals("TRADE_FINISHED", tradeStatus)) &&
                ObjectId.isValid(paymentId) &&
                StringUtils.equals(AlipayUtils.sign(data, secret), sign)) {

            Payment payment = paymentTrapdoor.get(new ObjectId(paymentId));
            if (!payment.isPaid()) {
                payment.setPaid(true);
                paymentTrapdoor.save(payment);
            }
            try {
                applicationContext.getBean(payment.getType().getClz()).paymentNotify(payment.getArguments());
            } catch (Exception e) {
                logger.error("Ops.", e);
            }
            if (logger.isWarnEnabled() && StringUtils.equals("TRADE_FINISHED", tradeStatus)) {
                logger.warn("Should received trade_success! trade_finished may lead to refund;");
            }
            return true;
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
