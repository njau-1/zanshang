package com.zanshang.services.alipay;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.controllers.web.AlipayController;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Payment;
import com.zanshang.services.payment.PaymentGetActor;
import com.zanshang.services.project.ProjectGetActor;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.AlipayUtils;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import scala.concurrent.Future;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lookis on 6/16/15.
 */
public class AlipayPaymentQRCreateActor extends BaseUntypedActor {

    private String PARTNER;

    private String APP_SECRET;

    private String REQUEST_CONTEXT;

    private static final String GATEWAY = "https://mapi.alipay.com/gateway.do";

    private ActorRef projectActor;

    private ActorRef paymentActor;

    private CloseableHttpAsyncClient httpClient = HttpAsyncClients.createMinimal();

    public AlipayPaymentQRCreateActor(ApplicationContext spring) {
        super(spring);
        projectActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(ProjectGetActor.class, spring)));
        paymentActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(PaymentGetActor.class, spring)));
        httpClient.start();
        PARTNER = getProperty("ALIPAY_ID");
        APP_SECRET = getProperty("ALIPAY_SECRET");
        REQUEST_CONTEXT = getProperty("SERVER_CONTEXT");
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Parameters parameters = (Parameters) o;
        Future<Object> paymentAsk = Patterns.ask(paymentActor, parameters.getPaymentId(), ActorConstant
                .DEFAULT_TIMEOUT);
        ActorRef sender = getSender();
        ActorRef self = getSelf();
        paymentAsk.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object o) throws Throwable {
                Payment payment = (Payment) o;
                Assert.isTrue(!payment.isPaid());
                Map<String, String> orderMap = new HashMap<String, String>();
                orderMap.put("service", "create_direct_pay_by_user");
                orderMap.put("partner", PARTNER);
                orderMap.put("_input_charset", "utf-8");
                orderMap.put("payment_type", "1");
                orderMap.put("notify_url", REQUEST_CONTEXT + parameters.getRequestContext() +
                        AlipayController.ALIPAY_PATH + AlipayController.NOTIFY_PATH);
                orderMap.put("return_url", REQUEST_CONTEXT + parameters.getRequestContext() +
                        AlipayController.ALIPAY_PATH + AlipayController.RETURN_PATH);
                orderMap.put("out_trade_no", payment.getId().toHexString());
                orderMap.put("subject", payment.getProductName());
                orderMap.put("qr_pay_mode", "3");
                long cents = payment.getPrice().getUnit().toCent(payment.getPrice().getPrice());
                orderMap.put("total_fee", Double.toString(((double) cents) / 100));
                orderMap.put("seller_id", PARTNER);
                String signValue = AlipayUtils.sign(orderMap, APP_SECRET);
                orderMap.put("sign", signValue);
                orderMap.put("sign_type", "MD5");
                URI requestUri = AlipayUtils.buildIFrameURL(orderMap, GATEWAY);
                sender.tell(requestUri, self);
            }
        }, getContext().dispatcher());
    }

    public static class Parameters {

        private String paymentId;

        private String requestContext;

        public Parameters(String paymentId, String requestContext) {
            this.paymentId = paymentId;
            this.requestContext = requestContext;
        }

        public String getPaymentId() {
            return paymentId;
        }

        public String getRequestContext() {
            return requestContext;
        }
    }
}
