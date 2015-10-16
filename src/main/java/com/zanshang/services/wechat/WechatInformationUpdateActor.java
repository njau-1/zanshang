package com.zanshang.services.wechat;

import com.zanshang.constants.Connect;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.WechatInformation;
import com.zanshang.utils.Json;
import org.apache.commons.lang.StringUtils;
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
 * Document at
 * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419316518
 * &token=&lang=zh_CN
 * Created by Lookis on 6/7/15.
 */
public class WechatInformationUpdateActor extends BaseUntypedActor {

    private static final String PERSONAL_INFORMATION_POINT = "https://api.weixin.qq.com/sns/userinfo";

    private CloseableHttpAsyncClient httpClient = HttpAsyncClients.createMinimal();

    public WechatInformationUpdateActor(ApplicationContext spring) {
        super(spring);
        httpClient.start();
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Map<String, String> accessTokenMap = (Map<String, String>) o;
        //according to the document, we need accessToken and openId to get the information from wechat
        String accessToken = accessTokenMap.get(Connect.WECHAT_ACCESSTOKEN_IN_ACCESSTOKEN);
        String openId = accessTokenMap.get(Connect.WECHAT_OPENID_IN_ACCESSTOKEN);

        URIBuilder builder = new URIBuilder(PERSONAL_INFORMATION_POINT);
        builder.setParameter("openid", openId);
        builder.setParameter("access_token", accessToken);
        HttpGet httpRequest = null;
        httpRequest = new HttpGet(builder.build());
        httpClient.execute(httpRequest, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                try {
                    Map<String, String> userInformationMap = Json.fromJson(result.getEntity().getContent(), Map.class);
                    String unionId = userInformationMap.get("unionid");
                    String avatar = userInformationMap.get("headimgurl");
                    String name = userInformationMap.get("nickname");
                    if (StringUtils.isEmpty(unionId) || StringUtils.isEmpty(avatar) || StringUtils.isEmpty(name)) {
                        logger.warn("Wechat UserInformation fetch error, may be accessToken expired? "+ userInformationMap);
                    } else {
                        WechatInformation information = new WechatInformation(unionId, name, avatar);
                        getMongoTemplate().save(information);
                    }
                } catch (IOException e) {
                    logger.error("Wechat UserInformation Response read failed.", e);
                }
            }

            @Override
            public void failed(Exception ex) {
                logger.error("Wechat UserInformation Fetch Failed.", ex);
            }

            @Override
            public void cancelled() {

            }
        });
    }
}
