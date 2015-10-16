package com.zanshang.services.wechat;

import akka.actor.ActorRef;
import akka.actor.Status;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import com.zanshang.config.spring.CacheConfig;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.Json;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Future;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Lookis on 7/23/15.
 */
public class WechatJSApiTicketGetActor extends BaseUntypedActor {

    private static final String TICKET_KEY = "ticket";

    private static final String TICKET = "ticket";

    private static final String API_POINT_FORMAT = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token" +
            "=%s&type=jsapi";

    private CloseableHttpAsyncClient httpClient = HttpAsyncClients.createMinimal();

    private ActorRef accessTokenGetActor;

    public WechatJSApiTicketGetActor(ApplicationContext spring) {
        super(spring);
        accessTokenGetActor = AkkaTrapdoor.create(getContext(), WechatAccessTokenGetActor.class, spring);
        httpClient.start();
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Cache cache = getCacheManager().getCache(CacheConfig.CacheKey.WECHAT_JSAPI.getName());
        String ticketMap = cache.get(TICKET_KEY, String.class);
        ActorRef sender = getSender();
        ActorRef self = getSelf();
        if(ticketMap == null || StringUtils.isEmpty(ticketMap)){
            Future<Object> ask = Patterns.ask(accessTokenGetActor, "", ActorConstant.DEFAULT_TIMEOUT);
            ask.onSuccess(new OnSuccess<Object>(){
                @Override
                public void onSuccess(Object o) throws Throwable {
                    String accessToken = o.toString();
                    String url = String.format(API_POINT_FORMAT, accessToken);
                    HttpGet get = new HttpGet(url);
                    httpClient.execute(get, new FutureCallback<HttpResponse>() {
                        @Override
                        public void completed(HttpResponse result) {
                            try {
                                InputStream content = result.getEntity().getContent();
                                Map<String, Object> body = Json.fromJson(content, Map.class);
                                String ticket = (String) body.get(TICKET);
                                if (ticket != null) {
                                    cache.put(TICKET_KEY, Json.toJson(body));
                                    sender.tell(ticket, self);
                                } else {
                                    logger.debug("Wechat ticket fetch error:" + content);
                                    sender.tell(new Status.Failure(new RuntimeException("Error happend! see " +
                                            "Actor log " + "for details.")), self);
                                }
                            } catch (IOException e) {
                                logger.error("Ops. wechat ticket fetch error.", e);
                                sender.tell(new Status.Failure(new RuntimeException("Error happend! see " +
                                        "Actor log " + "for details.")), self);
                            }
                        }

                        @Override
                        public void failed(Exception ex) {
                            logger.error("Wechat JSApi Ticket failed.",ex);
                            sender.tell(new Status.Failure(new RuntimeException("Error happend! see " +
                                    "Actor log " + "for details.")), self);
                        }

                        @Override
                        public void cancelled() {

                        }
                    });
                }
            }, getContext().dispatcher());
        }else{
            Map<String,Object> tickets = Json.fromJson(ticketMap, Map.class);
            String token = (String) tickets.get(TICKET);
            sender.tell(token, self);
        }
    }
}
