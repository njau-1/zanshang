package com.zanshang.controllers.web;

import akka.actor.ActorSystem;
import com.zanshang.constants.Connect;
import com.zanshang.constants.WechatPlatform;
import com.zanshang.framework.CodeExpireException;
import com.zanshang.models.Payment;
import com.zanshang.models.Person;
import com.zanshang.services.PaymentTrapdoor;
import com.zanshang.services.PersonTrapdoor;
import com.zanshang.services.WechatAuthorizationTrapdoor;
import com.zanshang.services.payment.PaymentTrapdoorImpl;
import com.zanshang.services.person.PersonTrapdoorImpl;
import com.zanshang.services.wechat.WechatAuthorizationTrapdoorImpl;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.WechatUtils;
import com.zanshang.utils.Xml;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lookis on 5/17/15.
 */
@Controller
@RequestMapping(value = WechatController.WECHAT_PATH)
public class WechatController implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(getClass());

    public static final String WECHAT_PATH = "/wechat";

    public static final String NOTIFY_PATH = "/notify";

    public static final String CALLBACK_REQUEST_PATH = WECHAT_PATH + "/bind";

    public static final String UNBIND_PATH = WECHAT_PATH + "/unbind";

    private static final int LENGTH_OF_OBJECTID = ObjectId.get().toHexString().length();

    private String CALLBACK_FAIL_BODY;

    private String CALLBACK_SUCCESS_BODY;

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    PaymentTrapdoor paymentTrapdoor;

    //xuming wechat bind 07/14/15
    PersonTrapdoor personService;

    private WechatAuthorizationTrapdoor wechatAuthorizationTrapdoor;
    //end

    @Value("${WECHAT_KEY}")
    String secret;

    private ApplicationContext applicationContext;

    public WechatController() {
        Map<String, String> failMap = new HashMap<>();
        failMap.put("return_code", "FAIL");
        failMap.put("return_msg", "FAIL");
        CALLBACK_FAIL_BODY = Xml.toXml(failMap);
        Map<String, String> successMap = new HashMap<>();
        successMap.put("return_code", "SUCCESS");
        successMap.put("return_msg", "OK");
        CALLBACK_SUCCESS_BODY = Xml.toXml(successMap);
    }

    @PostConstruct
    protected void init() {
        paymentTrapdoor = akkaTrapdoor.createTrapdoor(PaymentTrapdoor.class, PaymentTrapdoorImpl.class);
        //xuming wechat bind 07/14/15
        personService = akkaTrapdoor.createTrapdoor(PersonTrapdoor.class, PersonTrapdoorImpl.class);
        wechatAuthorizationTrapdoor = akkaTrapdoor.createTrapdoor(WechatAuthorizationTrapdoor.class,
                WechatAuthorizationTrapdoorImpl.class);
    }

    //微信支付回调
    @RequestMapping(value = NOTIFY_PATH)
    @ResponseBody
    public Object payment(HttpServletRequest request) {
        try {
            Map<String, String> data = Xml.fromXml(request.getInputStream(), Map.class);
            //由于我们可能会有跨号支付，如果产生跨号支付的话，会在原orderId后加入随机数
            String paymentId = data.get("out_trade_no").substring(0, LENGTH_OF_OBJECTID);
            if (StringUtils.equals("SUCCESS", data.get("return_code")) &&
                    StringUtils.equals("SUCCESS", data.get("result_code")) &&
                    StringUtils.equals(WechatUtils.sign(data, secret), data.get("sign")) &&
                    ObjectId.isValid(paymentId)) {
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
                return CALLBACK_SUCCESS_BODY;
            } else {
                logger.error("Wechat callback fail:" + data);
                return CALLBACK_FAIL_BODY;
            }
        } catch (IOException e) {
            logger.error("Wechat callback exception", e);
            return CALLBACK_FAIL_BODY;
        }
    }

    //wechat 绑定回调处理
    //xuming wechat bind 07/14/15
    @RequestMapping(value = "/bind", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public String bind(HttpServletRequest request, Principal principal) {
        boolean isWechat = WechatController.isWechat(request);
        WechatPlatform platform;
        if (isWechat) {
            platform = WechatPlatform.MP;
        } else {
            platform = WechatPlatform.OPEN;
        }
        Map<String, String> accessTokenMap = null;
        try {
            accessTokenMap = wechatAuthorizationTrapdoor.fetchAndSave(request.getParameter("code"), platform, false);
            ObjectId uid = new ObjectId(principal.getName());
            Person person = personService.get(uid);
            person.setWechatId(accessTokenMap.get(Connect.WECHAT_ID_IN_ACCESSTOKEN));
            personService.save(person);
        } catch (CodeExpireException e) {
            logger.debug("contains no accesstoken");
        }
        return "redirect:/settings";
    }

    //wechat 解除绑定处理
    //xuming wechat bind 07/14/15
    @RequestMapping(value = "/unbind", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public String unbind(HttpServletRequest request, Principal principal) {
        ObjectId uid = new ObjectId(principal.getName());
        Person person = personService.get(uid);
        person.setWechatId(null);
        personService.save(person);
        return "redirect:/settings";
    }

    public static boolean isWechat(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        WechatPlatform platform;
        if (userAgent.contains("MicroMessenger")) {
            return true;
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
