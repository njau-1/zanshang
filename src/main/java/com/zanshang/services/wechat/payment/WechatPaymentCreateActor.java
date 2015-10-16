package com.zanshang.services.wechat.payment;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.controllers.web.WechatController;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.PriceUnit;
import com.zanshang.models.Payment;
import com.zanshang.services.payment.PaymentGetActor;
import com.zanshang.services.project.ProjectGetActor;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.WechatUtils;
import com.zanshang.utils.Xml;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import scala.concurrent.Future;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Lookis on 6/18/15.
 */
public class WechatPaymentCreateActor extends BaseUntypedActor {

    private static final String WECHAT_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    private ActorRef projectActor;

    private ActorRef paymentActor;

    private String WECHAT_KEY;

    private String MERCHANT_ID;

    private String REQUEST_CONTEXT;

    private CloseableHttpAsyncClient httpClient = HttpAsyncClients.createMinimal();

    public WechatPaymentCreateActor(ApplicationContext spring) {
        super(spring);
        projectActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(ProjectGetActor.class, spring)));
        paymentActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(PaymentGetActor.class, spring)));
        httpClient.start();
        MERCHANT_ID = getProperty("WECHAT_MERCHANT_ID");
        REQUEST_CONTEXT = getProperty("SERVER_CONTEXT");
        WECHAT_KEY = getProperty("WECHAT_KEY");
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Parameters parameters = (Parameters) o;
        logger.debug("Wechat create order parameters:" + parameters);
        Future<Object> paymentAsk = Patterns.ask(paymentActor, parameters.getPaymentId(), ActorConstant.DEFAULT_TIMEOUT);
        ActorRef sender = getSender();
        ActorRef self = getSelf();
        paymentAsk.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object o) throws Throwable {
                Payment payment = (Payment) o;
                Assert.isTrue(!payment.isPaid());
                Map<String, String> orderMap = new TreeMap<>();
                orderMap.put("appid", parameters.getAppid());
                orderMap.put("mch_id", MERCHANT_ID);
                orderMap.put("nonce_str", RandomStringUtils.randomAlphanumeric(32));
                orderMap.put("body", payment.getProductName());
                orderMap.put("out_trade_no", payment.getId().toHexString() + "_" + RandomStringUtils
                        .randomAlphanumeric(32 - 1 - payment.getId().toHexString().length()));
                orderMap.put("total_fee", Long.toString(payment.getPrice().to(PriceUnit.CENT)));
                orderMap.put("spbill_create_ip", parameters.getIp());
                orderMap.put("notify_url", REQUEST_CONTEXT + parameters.getRequestContext() +
                        WechatController.WECHAT_PATH +
                        WechatController.NOTIFY_PATH);
                orderMap.put("trade_type", parameters.getType().toString().toUpperCase());
                if (parameters.getType() == Parameters.Type.JSAPI) {
                    orderMap.put("openid", parameters.getOpenId());
                }
                orderMap.put("product_id", payment.getId().toHexString());
                String signValue = WechatUtils.sign(orderMap, WECHAT_KEY);
                orderMap.put("sign", signValue);
                String data = Xml.toXml(orderMap);
                logger.debug("Wechat CreateOrder Date to send:" + data);
                String encodeData = new String(data.getBytes(), "ISO8859-1");
                HttpPost post = new HttpPost(WECHAT_ORDER);
                post.setEntity(new StringEntity(encodeData, ContentType.APPLICATION_XML));
                httpClient.execute(post, new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(HttpResponse result) {
                        try {
                            InputStream content = result.getEntity().getContent();
                            Map<String, String> body = Xml.fromXml(content, Map.class);
                            if (StringUtils.equals("SUCCESS", body.get("return_code"))) {
                                String sign = WechatUtils.sign(body, WECHAT_KEY);
                                if (StringUtils.equals(sign, body.get("sign"))) {
                                    if (StringUtils.equals(body.get("result_code"), "SUCCESS")) {
                                        sender.tell(body, self);
                                    } else {
                                        logger.error("Wechat payment create failed for " + body.get("err_code") +
                                                "/" + body.get("err_code_des"));
                                    }
                                } else {
                                    logger.error("Wechat signature error: suppose to be [" + sign + "] " +
                                            "but we got" +
                                            " [" + body.get("sign") + "]");
                                }
                            } else {
                                logger.error("Wechat communication failed message:" + body);
                                logger.info("Wechat communication failed for:" + body.get("return_msg"));
                            }
                        } catch (IOException e) {
                            logger.error("Parse wechat entity failed", e);
                        }
                        sender.tell(new Status.Failure(new RuntimeException("Error happend! see " +
                                "Actor log " + "for details.")), self);
                    }

                    @Override
                    public void failed(Exception ex) {
                        logger.error("Call wechat unfiedorder url failed", ex);
                        sender.tell(new Status.Failure(ex), self);
                    }

                    @Override
                    public void cancelled() {

                    }
                });
            }
        }, getContext().dispatcher());
    }

    static class Parameters {

        static enum Type {
            JSAPI, NATIVE;
        }

        private Type type;

        private String paymentId;

        private String appid;

        private String ip;

        private String secret;

        private String requestContext;

        private String openId;

        public Parameters(String paymentId, String appid, String ip, String secret, Type type, String requestContext,
                          String openId) {
            this.paymentId = paymentId;
            this.appid = appid;
            this.ip = ip;
            this.secret = secret;
            this.type = type;
            this.requestContext = requestContext;
            this.openId = openId;
        }

        public String getOpenId() {
            return openId;
        }

        public String getRequestContext() {
            return requestContext;
        }

        public Type getType() {
            return type;
        }

        public String getPaymentId() {
            return paymentId;
        }

        public String getAppid() {

            return appid;
        }

        public String getIp() {
            return ip;
        }

        public String getSecret() {
            return secret;
        }

        @Override
        public String toString() {
            return "Parameters{" +
                    "type=" + type +
                    ", paymentId='" + paymentId + '\'' +
                    ", appid='" + appid + '\'' +
                    ", ip='" + ip + '\'' +
                    ", requestContext='" + requestContext + '\'' +
                    ", openId='" + openId + '\'' +
                    '}';
        }
    }
}
