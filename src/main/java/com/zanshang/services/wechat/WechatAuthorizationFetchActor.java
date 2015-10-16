package com.zanshang.services.wechat;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import com.zanshang.constants.Connect;
import com.zanshang.constants.WechatPlatform;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.CodeExpireException;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.Json;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
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
public class WechatAuthorizationFetchActor extends BaseUntypedActor {

    private String APP_ID;

    private String APP_SECRET;

    private String MP_ID;

    private String MP_SECRET;

    private final String ACCESS_TOKEN_POINT = "https://api.weixin.qq.com/sns/oauth2/access_token";

    private CloseableHttpAsyncClient httpClient = HttpAsyncClients.createMinimal();

    private ActorRef saveActor;

    public WechatAuthorizationFetchActor(ApplicationContext spring) {
        super(spring);
        APP_ID = getProperty("WECHAT_APPID");
        APP_SECRET = getProperty("WECHAT_SECRET");
        MP_ID = getProperty("WECHAT_MP_ID");
        MP_SECRET = getProperty("WECHAT_MP_SECRET");
        saveActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(WechatAuthorizationSaveActor.class,
                spring)));
        httpClient.start();
    }

    @Override
    public void onReceive(Object o) throws Exception {
        CodeAndPlatform codeAndPlatform = (CodeAndPlatform) o;
        String code = codeAndPlatform.getCode();
        URIBuilder builder = new URIBuilder(ACCESS_TOKEN_POINT);
        builder.setParameter("code", code);
        switch (codeAndPlatform.getPlatform()) {
            case MP:
                builder.addParameter("appid", MP_ID);
                builder.addParameter("secret", MP_SECRET);
                break;
            case OPEN:
                builder.addParameter("appid", APP_ID);
                builder.addParameter("secret", APP_SECRET);
                break;
        }
        builder.addParameter("grant_type", "authorization_code");
        HttpGet httpRequest = null;
        httpRequest = new HttpGet(builder.build());
        ActorRef sender = getSender();
        ActorRef self = getSelf();
        httpClient.execute(httpRequest, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                try {
                    Map<String, String> accessTokenMap = Json.fromJson(httpResponse.getEntity().getContent(), Map
                            .class);
                    if (accessTokenMap.containsKey(Connect.WECHAT_ACCESSTOKEN_IN_ACCESSTOKEN)) {
                        if (codeAndPlatform.isSave()) {
                            saveActor.tell(accessTokenMap, getSelf());
                        }
                        sender.tell(accessTokenMap, self);
                    }else{
                        logger.error("Wechat AccessToken fetch error:"+accessTokenMap);
                        sender.tell(new Status.Failure(new CodeExpireException("contains no accesstoken")),self);
                    }
                } catch (IOException e) {
                    logger.error("Wechat Authorization Response read failed.", e);
                }
            }

            @Override
            public void failed(Exception ex) {
                logger.error("Wechat Authorization Fetch Failed.", ex);
            }

            @Override
            public void cancelled() {

            }
        });
    }

    static class CodeAndPlatform {

        private String code;

        private WechatPlatform platform;

        //如果使用snsapi_base的方式取的accessToken,请不要存，如果存了，可能会覆盖权限更高的accessToken
        private boolean save = true;

        public CodeAndPlatform(String code, WechatPlatform platform, boolean save) {
            this.code = code;
            this.platform = platform;
            this.save = save;
        }

        public CodeAndPlatform(String code, WechatPlatform platform) {
            this.code = code;
            this.platform = platform;
        }

        public boolean isSave() {
            return save;
        }

        public String getCode() {

            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public WechatPlatform getPlatform() {
            return platform;
        }

        public void setPlatform(WechatPlatform platform) {
            this.platform = platform;
        }
    }
}
