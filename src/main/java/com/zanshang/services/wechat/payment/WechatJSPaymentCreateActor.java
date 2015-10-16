package com.zanshang.services.wechat.payment;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.WechatUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Future;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lookis on 6/18/15.
 */
public class WechatJSPaymentCreateActor extends BaseUntypedActor {

    private String MP_ID;

    private String MP_SECRET;

    private String WECHAT_KEY;

    private ActorRef orderCreateActor;

    public WechatJSPaymentCreateActor(ApplicationContext spring) {
        super(spring);
        orderCreateActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(WechatPaymentCreateActor.class,
                spring)));
        MP_ID = getProperty("WECHAT_MP_ID");
        MP_SECRET = getProperty("WECHAT_MP_SECRET");
        WECHAT_KEY = getProperty("WECHAT_KEY");
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Parameters parameters = (Parameters) o;
        String orderId = parameters.getPaymentId();
        Future<Object> orderAsk = Patterns.ask(orderCreateActor, new WechatPaymentCreateActor.Parameters(orderId,
                MP_ID, parameters.getIp(), MP_SECRET, WechatPaymentCreateActor.Parameters.Type.JSAPI, parameters
                .getRequestContext(), parameters.getOpenId()), ActorConstant.DEFAULT_TIMEOUT);
        ActorRef sender = getSender();
        ActorRef self = getSelf();
        orderAsk.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object o) throws Throwable {
                Map<String, String> body = (Map<String, String>) o;
                sender.tell(buildResponse(body.get("prepay_id"), parameters.getTimeInMills() / 1000), self);
            }
        }, getContext().dispatcher());
    }

    private Map<String, String> buildResponse(String prepayId, Long timeInSeconds) {
        Map<String, String> retMap = new HashMap<>();
        retMap.put("appId", MP_ID);
        retMap.put("timeStamp", Long.toString(timeInSeconds));
        retMap.put("nonceStr", RandomStringUtils.randomAlphanumeric(32));
        retMap.put("package", "prepay_id=" + prepayId);
        retMap.put("signType", "MD5");
        String sign = WechatUtils.sign(retMap, WECHAT_KEY);
        retMap.put("paySign", sign);
        retMap.remove("appId");
        retMap.remove("timeStamp");
        retMap.put("timestamp", Long.toString(timeInSeconds));
        logger.debug("JSOrder Create Map:"+retMap);
        return retMap;
    }

    public static class Parameters {

        private String paymentId;

        private String ip;

        private Long timeInMills;

        private String requestContext;

        private String openId;

        public Parameters(String paymentId, String ip, Long timeInMills, String requestContext, String openId) {
            this.paymentId = paymentId;
            this.ip = ip;
            this.timeInMills = timeInMills;
            this.requestContext = requestContext;
            this.openId = openId;
        }

        public String getOpenId() {
            return openId;
        }

        public String getRequestContext() {
            return requestContext;
        }

        public Long getTimeInMills() {
            return timeInMills;
        }

        public String getPaymentId() {

            return paymentId;
        }

        public String getIp() {
            return ip;
        }
    }
}
