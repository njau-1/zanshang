package com.zanshang.services.wechat.payment;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import com.zanshang.config.spring.CacheConfig;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import scala.concurrent.Future;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by Lookis on 6/16/15.
 */
public class WechatQRPaymentCreateActor extends BaseUntypedActor {

    private String MP_ID;

    private String MP_SECRET;

    private String IP;

    private ActorRef paymentCreateActor;

    public WechatQRPaymentCreateActor(ApplicationContext spring) {
        super(spring);
        paymentCreateActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(WechatPaymentCreateActor.class,
                spring)));
        MP_ID = getProperty("WECHAT_MP_ID");
        MP_SECRET = getProperty("WECHAT_MP_SECRET");
        IP = getProperty("CREATE_ORDER_IP");
        Assert.notNull(IP);
        try {
            boolean found = false;
            Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
            for (; n.hasMoreElements() && !found; ) {
                NetworkInterface e = n.nextElement();
                Enumeration<InetAddress> a = e.getInetAddresses();
                for (; a.hasMoreElements() && !found; ) {
                    InetAddress addr = a.nextElement();
                    found = found || StringUtils.equals(IP, addr.getHostAddress());
                }
            }
            Assert.isTrue(found, "No Valid IP in environment.properties");
        } catch (SocketException e) {
            logger.info("Ignore IP check.", e);
        }
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Parameters params = (Parameters) o;
        String paymentId = params.getPaymentId();
        Cache cache = getCacheManager().getCache(CacheConfig.CACHE_NAME_WECHAT_QR_ORDER);
        String codeUrl = cache.get(paymentId, String.class);
        ActorRef sender = getSender();
        ActorRef self = getSelf();
        if (StringUtils.isEmpty(codeUrl)) {
            Future<Object> paymentAsk = Patterns.ask(paymentCreateActor, new WechatPaymentCreateActor.Parameters(paymentId,
                    MP_ID, IP, MP_SECRET, WechatPaymentCreateActor.Parameters.Type.NATIVE, params.getRequestContext(), null),
                    ActorConstant.REMOTE_CALL_TIMEOUT);
            paymentAsk.andThen(new OnComplete<Object>() {
                @Override
                public void onComplete(Throwable throwable, Object o) throws Throwable {
                    if (o != null) {
                        Map<String, String> body = (Map<String, String>) o;
                        String codeUrl = body.get("code_url");
                        cache.put(paymentId, codeUrl);
                        sender.tell(codeUrl, self);
                    } else {
                        logger.error("Ops.", throwable);
                    }
                }
            }, getContext().dispatcher());
        } else {
            getSender().tell(codeUrl, getSelf());
        }
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
