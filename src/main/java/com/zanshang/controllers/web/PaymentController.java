package com.zanshang.controllers.web;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import com.zanshang.constants.ActorConstant;
import com.zanshang.constants.Connect;
import com.zanshang.constants.WechatPlatform;
import com.zanshang.models.Payment;
import com.zanshang.models.QRCode;
import com.zanshang.services.PaymentTrapdoor;
import com.zanshang.services.QRCodeTrapdoor;
import com.zanshang.services.WechatAuthorizationTrapdoor;
import com.zanshang.services.alipay.AlipayPaymentFormCreateActor;
import com.zanshang.services.alipay.AlipayPaymentQRCreateActor;
import com.zanshang.services.alipay.AlipayWapPaymentFormCreateActor;
import com.zanshang.services.alipay.AlipayWebPaymentFormCreateActor;
import com.zanshang.services.payment.PaymentTrapdoorImpl;
import com.zanshang.services.qrcode.QRCodeSaveActor;
import com.zanshang.services.wechat.WechatAuthorizationTrapdoorImpl;
import com.zanshang.services.wechat.WechatJSApiTicketGetActor;
import com.zanshang.services.wechat.payment.WechatJSPaymentCreateActor;
import com.zanshang.services.wechat.payment.WechatQRPaymentCreateActor;
import com.zanshang.utils.Ajax;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.Request;
import com.zanshang.utils.WechatUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 9/18/15.
 */
@Controller
@RequestMapping("/payments")
public class PaymentController implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @Value("${WECHAT_MP_ID}")
    String MP_ID;

    @Value("${WECHAT_MP_SECRET}")
    String MP_SECRET;

    WechatAuthorizationTrapdoor wechatTrapdoor;

    PaymentTrapdoor paymentTrapdoor;

    ActorRef wechatQRPaymentActor;

    ActorRef wechatJSPaymentActor;

    ActorRef wechatJSApiTicketGetActor;

    ActorRef alipayQRActor;

    ActorRef alipayFormActor;

    ActorRef alipayWapFormActor;

    ActorRef QRActor;

    private ApplicationContext applicationContext;

    @PostConstruct
    protected void init() {
        wechatTrapdoor = akkaTrapdoor.createTrapdoor(WechatAuthorizationTrapdoor.class,
                WechatAuthorizationTrapdoorImpl.class);
        wechatQRPaymentActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(WechatQRPaymentCreateActor
                .class, applicationContext)));
        wechatJSPaymentActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(WechatJSPaymentCreateActor
                .class, applicationContext)));
        wechatJSApiTicketGetActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(WechatJSApiTicketGetActor
                .class, applicationContext)));
        alipayQRActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(AlipayPaymentQRCreateActor.class,
                applicationContext)));
        alipayFormActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(AlipayWebPaymentFormCreateActor
                .class, applicationContext)));
        alipayWapFormActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(AlipayWapPaymentFormCreateActor
                .class, applicationContext)));
        QRActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(QRCodeSaveActor.class, applicationContext)));
        paymentTrapdoor = akkaTrapdoor.createTrapdoor(PaymentTrapdoor.class, PaymentTrapdoorImpl.class);
    }

    @RequestMapping(value = "/{paymentId}/status", method = RequestMethod.GET)
    @ResponseBody
    public Object doneAjax(@PathVariable("paymentId") String paymentId) {
        Payment payment = paymentTrapdoor.get(new ObjectId(paymentId));
        Map<String, String> status = new HashMap<>();
        status.put("paid", Boolean.toString(payment != null && payment.isPaid()));
        String callback = applicationContext.getBean(payment.getType().getClz()).getCallback(payment.getArguments());
        status.put("callback", callback);
        return Ajax.ok(status);
    }

    @RequestMapping(value = "/{paymentId}/micromessenger/crosspay", method = RequestMethod.GET)
    public Object crossPay(@PathVariable("paymentId") String paymentId, HttpServletRequest request) {
        //跨号支付用二维码
        Map<String, Object> payment = new HashMap<>();
        Future<Object> paymentFuture = ask(wechatQRPaymentActor, new WechatQRPaymentCreateActor.Parameters(paymentId,
                request.getContextPath()), ActorConstant.REMOTE_CALL_TIMEOUT);
        Future<Object> qrCodeFuture = paymentFuture.andThen(new OnComplete<Object>() {
            @Override
            public void onComplete(Throwable throwable, Object o) throws Throwable {
                if (o != null) {
                    QRCode qr = new QRCode(o.toString());
                    try {
                        Await.result(ask(QRActor, qr, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                                .DEFAULT_TIMEOUT_DURATION);
                        payment.put("wechat", String.format(QRCodeTrapdoor.pathFormat, qr.getId()));
                    } catch (Exception e) {
                        logger.error("Ops.", e);
                    }
                } else {
                    logger.error("Future Error.", throwable);
                }
            }
        }, actorSystem.dispatcher());
        ModelAndView mav = new ModelAndView("6_5_1");
        try {
            Await.result(qrCodeFuture, Duration.Inf());
        } catch (Exception e) {
            logger.warn("Timeout Of Payment, fetched:" + payment, e);
        }
        Payment pay = paymentTrapdoor.get(new ObjectId(paymentId));
        mav.addAllObjects(payment);
        mav.addObject("paymentId", paymentId);
        String callback = applicationContext.getBean(pay.getType().getClz()).getCallback(pay.getArguments());
        mav.addObject("callback", callback);
        return mav;
    }

    //这个支付订单改为传参的方式是因为微信支付需要填一个蛋疼的支付授权目录，所以这个路径需要修改的话，记得在微信公众平台那边把支付授权目录改了
    @RequestMapping(value = "/mobile", method = RequestMethod.GET)
    //    @Secured("ROLE_USER")
    public Object mobilePayment(@RequestParam("paymentId") String paymentId, @RequestParam(value = "code", required =
            false) String code, HttpServletRequest request, Date requestTime) {

        Assert.isTrue(ObjectId.isValid(paymentId));
        Payment pay = paymentTrapdoor.get(new ObjectId(paymentId));
        if (pay.isPaid()) {
            String callback = applicationContext.getBean(pay.getType().getClz()).getCallback(pay.getArguments());
            return "redirect:" + callback;
        }
        Map<String, Object> payment = new HashMap<>();
        ArrayList<Future<Object>> futures = new ArrayList<Future<Object>>();
        if (Request.isWechat(request)) {
            //如果没有code, 需要静默授权拿到code, 再拿到openId
            if (code == null && StringUtils.isEmpty(code)) {
                return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + MP_ID +
                        "&redirect_uri=" + Request.encode(Request.buildCurrentURL(request)) +
                        "&response_type=code&scope=snsapi_base#wechat_redirect";
            } else {
                //JSAPI ticket and config
                Future<Object> ticketFuture = ask(wechatJSApiTicketGetActor, "", ActorConstant.REMOTE_CALL_TIMEOUT);
                futures.add(ticketFuture.andThen(new OnComplete<Object>() {
                    @Override
                    public void onComplete(Throwable throwable, Object o) throws Throwable {
                        if (o != null) {
                            Map<String, String> config = new HashMap<>();
                            config.put("timestamp", Long.toString(requestTime.getTime() / 1000));
                            config.put("url", Request.buildCurrentURL(request));
                            config.put("nonceStr", RandomStringUtils.randomAlphanumeric(32));
                            config.put("signature", WechatUtils.signSha1(config, o.toString()));
                            config.remove("url");
                            config.put("appId", MP_ID);
                            payment.put("config", config);
                        } else {
                            logger.error("Future Error.", throwable);
                        }
                    }
                }, actorSystem.dispatcher()));
                Map<String, String> accessTokenMap = null;
                try {
                    accessTokenMap = wechatTrapdoor.fetchAndSave(code, WechatPlatform.MP, true);
                } catch (Exception e) {
                    return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + MP_ID +
                            "&redirect_uri=" + Request.encode(Request.buildCurrentURL(request, Collections.singleton
                            ("code"))) +
                            "&response_type=code&scope=snsapi_base#wechat_redirect";
                }
                logger.debug("Wechat JSAPI snsbase accessTokenMap:" + accessTokenMap);
                String openId = accessTokenMap.get(Connect.WECHAT_OPENID_IN_ACCESSTOKEN);
                //JSAPI payment
                Future<Object> future = ask(wechatJSPaymentActor, new WechatJSPaymentCreateActor.Parameters
                        (paymentId, Request.getRemoteIP(request), requestTime.getTime(), request.getContextPath(),
                                openId), ActorConstant.REMOTE_CALL_TIMEOUT);
                futures.add(future.andThen(new OnComplete<Object>() {
                    @Override
                    public void onComplete(Throwable throwable, Object o) throws Throwable {
                        if (o != null) {
                            payment.put("wechat", o);
                        } else {
                            logger.error("Future Error.", throwable);
                        }
                    }
                }, actorSystem.dispatcher()));
            }
        } else {
            Future<Object> future = ask(alipayWapFormActor, new AlipayPaymentFormCreateActor.Parameters(paymentId,
                    request.getContextPath()), ActorConstant.DEFAULT_TIMEOUT);
            futures.add(future.andThen(new OnComplete<Object>() {
                @Override
                public void onComplete(Throwable throwable, Object o) throws Throwable {
                    if (o != null) {
                        payment.put("alipay", o);
                    } else {
                        logger.error("Future Error.", throwable);
                    }
                }
            }, actorSystem.dispatcher()));
        }
        ModelAndView mav = new ModelAndView("6_5");
        try {
            Await.result(Futures.sequence(futures, actorSystem.dispatcher()), Duration.Inf());
        } catch (Exception e) {
            logger.warn("Timeout Of Payment, fetched:" + payment, e);
        }
        mav.addAllObjects(payment);
        mav.addObject("paymentId", paymentId);
        String callback = applicationContext.getBean(pay.getType().getClz()).getCallback(pay.getArguments());
        mav.addObject("callback", callback);
        return mav;
    }

    //获取定单支付方式
    @RequestMapping(value = "/{paymentId}", method = RequestMethod.GET)
    //    @Secured("ROLE_USER")
    public Object payment(@PathVariable("paymentId") String paymentId, HttpServletRequest request, Date requestTime) {
        Assert.isTrue(ObjectId.isValid(paymentId));
        Payment pay = paymentTrapdoor.get(new ObjectId(paymentId));
        if (pay.isPaid()) {
            String callback = applicationContext.getBean(pay.getType().getClz()).getCallback(pay.getArguments());
            return "redirect:" + callback;
        }
        final ArrayList<Future<Object>> futures = new ArrayList<Future<Object>>();
        final Map<String, Object> payment = new HashMap<>();
        //String codeUrl
        Future<Object> codeUrlFuture = ask(wechatQRPaymentActor, new WechatQRPaymentCreateActor.Parameters(paymentId,
                request.getContextPath()), ActorConstant.REMOTE_CALL_TIMEOUT);
        futures.add(codeUrlFuture.andThen(new OnComplete<Object>() {
            @Override
            public void onComplete(Throwable throwable, Object o) throws Throwable {
                if (o != null) {
                    QRCode qr = new QRCode(o.toString());
                    try {
                        Await.result(ask(QRActor, qr, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                                .DEFAULT_TIMEOUT_DURATION);
                        payment.put("wechat", String.format(QRCodeTrapdoor.pathFormat, qr.getId()));
                    } catch (Exception e) {
                        logger.error("Ops.", e);
                    }
                } else {
                    logger.error("Future Error.", throwable);
                }
            }
        }, actorSystem.dispatcher()));
        //String iframe url
        Future<Object> iframeFuture = ask(alipayQRActor, new AlipayPaymentQRCreateActor.Parameters(paymentId, request
                .getContextPath()), ActorConstant.DEFAULT_TIMEOUT);
        futures.add(iframeFuture.andThen(new OnComplete<Object>() {
            @Override
            public void onComplete(Throwable throwable, Object o) throws Throwable {
                if (o != null) {
                    payment.put("alipayQr", o.toString());
                } else {
                    logger.error("Future Error.", throwable);
                }
            }
        }, actorSystem.dispatcher()));
        //Map of form
        Future<Object> alipayFormFuture = ask(alipayFormActor, new AlipayPaymentFormCreateActor.Parameters(paymentId,
                request.getContextPath()), ActorConstant.DEFAULT_TIMEOUT);
        futures.add(alipayFormFuture.andThen(new OnComplete<Object>() {
            @Override
            public void onComplete(Throwable throwable, Object o) throws Throwable {
                if (o != null) {
                    payment.put("alipayForm", o);
                } else {
                    logger.error("Future Error.", throwable);
                }
            }
        }, actorSystem.dispatcher()));
        ModelAndView mav = new ModelAndView("6_5");
        try {
            Await.result(Futures.sequence(futures, actorSystem.dispatcher()), Duration.Inf());
        } catch (Exception e) {
            logger.warn("Timeout Of Payment, fetched:" + payment, e);
        }
        mav.addAllObjects(payment);
        mav.addObject("paymentId", paymentId);
        String callback = applicationContext.getBean(pay.getType().getClz()).getCallback(pay.getArguments());
        mav.addObject("callback", callback);
        return mav;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
