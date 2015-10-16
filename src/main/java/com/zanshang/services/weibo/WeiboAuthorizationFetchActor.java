package com.zanshang.services.weibo;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.Json;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Lookis on 6/14/15.
 */
public class WeiboAuthorizationFetchActor extends BaseUntypedActor {

    private String APP_ID;

    private String APP_SECRET;

    public static final String CALLBACK_PARAMETER = "callback";

    public static final String CODE_PARAMETER = "code";

    private static final String ACCESS_TOKEN_POINT = "https://api.weibo.com/oauth2/access_token";

    private CloseableHttpAsyncClient httpClient = HttpAsyncClients.createMinimal();

    private ActorRef saveActor;

    public WeiboAuthorizationFetchActor(ApplicationContext spring) {
        super(spring);
        APP_ID = getProperty("WEIBO_APPID");
        APP_SECRET = getProperty("WEIBO_SECRET");
        saveActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(WeiboAuthorizationSaveActor.class, spring)));
        httpClient.start();
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Map<String, String> parameters = (Map<String, String>) o;
        String callback = parameters.get(CALLBACK_PARAMETER);
        String code = parameters.get(CODE_PARAMETER);
        URIBuilder builder = new URIBuilder(ACCESS_TOKEN_POINT);
        builder.setParameter("code", code);
        builder.addParameter("client_id", APP_ID);
        builder.addParameter("client_secret", APP_SECRET);
        builder.addParameter("grant_type", "authorization_code");
        builder.addParameter("redirect_uri", callback);
        HttpUriRequest httpRequest = null;
        httpRequest = new HttpPost(builder.build());
        ActorRef sender = getSender();
        ActorRef self = getSelf();
        httpClient.execute(httpRequest, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                try {
                    Map<String, String> accessTokenMap = Json.fromJson(httpResponse.getEntity().getContent(), Map
                            .class);
                    saveActor.tell(accessTokenMap, self);
                    sender.tell(accessTokenMap, self);
                } catch (IOException e) {
                    logger.error("Weibo Authorization Response read failed.", e);
                }
            }

            @Override
            public void failed(Exception ex) {
                logger.error("Weibo Authorization Fetch Failed.", ex);
            }

            @Override
            public void cancelled() {

            }
        });
    }
}
