package com.zanshang.services.wechat;

import akka.actor.ActorRef;
import akka.actor.Status;
import com.zanshang.config.spring.CacheConfig;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.utils.Json;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Lookis on 7/23/15.
 */
public class WechatAccessTokenGetActor extends BaseUntypedActor {

    private static final String ACCESS_TOKEN_KEY = "access_token";

    private static final String API_POINT_FORMAT = "https://api.weixin.qq" +
            ".com/cgi-bin/token?grant_type=client_credential" + "&appid=%s&secret=%s";

    public static final String ACCESS_TOKEN = "access_token";

    private CloseableHttpAsyncClient httpClient = HttpAsyncClients.createMinimal();

    private String MP_ID;

    private String MP_SECRET;

    public WechatAccessTokenGetActor(ApplicationContext spring) {
        super(spring);
        httpClient.start();
        MP_ID = getProperty("WECHAT_MP_ID");
        MP_SECRET = getProperty("WECHAT_MP_SECRET");
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Cache cache = getCacheManager().getCache(CacheConfig.CacheKey.WECHAT_JSAPI.getName());
        String accessTokenMap = cache.get(ACCESS_TOKEN_KEY, String.class);
        ActorRef sender = getSender();
        ActorRef self = getSelf();
        if (accessTokenMap == null || StringUtils.isEmpty(accessTokenMap)) {
            String url = String.format(API_POINT_FORMAT, MP_ID, MP_SECRET);
            HttpGet get = new HttpGet(url);
            httpClient.execute(get, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse result) {
                    try {
                        InputStream content = result.getEntity().getContent();
                        Map<String, Object> body = Json.fromJson(content, Map.class);
                        String accessToken = (String) body.get(ACCESS_TOKEN);
                        if (accessToken != null) {
                            cache.put(ACCESS_TOKEN_KEY, Json.toJson(body));
                            sender.tell(accessToken, self);
                        } else {
                            logger.debug("Wechat accessToken fetch error:" + content);
                            sender.tell(new Status.Failure(new RuntimeException("Error happend! see " +
                                    "Actor log " + "for details.")), self);
                        }
                    } catch (IOException e) {
                        logger.error("Ops. wechat accessToken fetch error.", e);
                        sender.tell(new Status.Failure(new RuntimeException("Error " +
                                "happend! see " +
                                "Actor log " + "for details.")), self);
                    }
                }

                @Override
                public void failed(Exception ex) {
                    logger.error("Wechat AccessToken failed.", ex);
                    sender.tell(new Status.Failure(new RuntimeException("Error " +
                            "happend! see " +
                            "Actor log " + "for details.")), self);
                }

                @Override
                public void cancelled() {

                }
            });
        } else {
            Map<String, Object> accessToken = Json.fromJson(accessTokenMap, Map.class);
            String token = (String) accessToken.get(ACCESS_TOKEN);
            sender.tell(token, self);
        }
    }
}
