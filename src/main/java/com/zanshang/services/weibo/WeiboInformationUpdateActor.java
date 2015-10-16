package com.zanshang.services.weibo;

import com.zanshang.constants.Connect;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.WeiboInformation;
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
 * Document at http://open.weibo.com/wiki/2/users/show
 * Created by Lookis on 6/7/15.
 */
public class WeiboInformationUpdateActor extends BaseUntypedActor {


    private static final String PERSONAL_INFORMATION_POINT = "https://api.weibo.com/2/users/show.json";

    private CloseableHttpAsyncClient httpClient = HttpAsyncClients.createMinimal();

    public WeiboInformationUpdateActor(ApplicationContext spring) {
        super(spring);
        httpClient.start();
    }

    @Override
    public void onReceive(Object o) throws Exception { 
	    Map<String, String> accessTokenMap = (Map<String, String>) o;
	    //according to the document, we need accessToken and uid to get the information from weibo
	    String accessToken = accessTokenMap.get(Connect.WEIBO_ACCESSTOKEN_IN_ACCESSTOKEN);
	    String uid = accessTokenMap.get(Connect.WEIBO_ID_IN_ACCESSTOKEN);
	    
	    URIBuilder builder = new URIBuilder(PERSONAL_INFORMATION_POINT);
	    builder.setParameter("access_token", accessToken);
	    builder.setParameter("uid", uid);
	    HttpGet httpRequest = null;
	    httpRequest = new HttpGet(builder.build());
	    httpClient.execute(httpRequest, new FutureCallback<HttpResponse>() {
	        @Override
	        public void completed(HttpResponse result) {
	            try {
	                Map<String, Object> userInformationMap = Json.fromJson(result.getEntity().getContent(), Map.class);
	                Long id = (Long) userInformationMap.get("id");
	                String avatar = (String) userInformationMap.get("profile_image_url");
	                String name = (String) userInformationMap.get("screen_name");
	                if (id == null|| StringUtils.isEmpty(avatar) || StringUtils.isEmpty(name)) {
	                    logger.warn("Weibo UserInformation fetch error, may be accessToken expired? "+ userInformationMap);
	                } else {
	                    WeiboInformation information = new WeiboInformation(Long.toString(id), name, avatar);
	                    getMongoTemplate().save(information);
	                }
	            } catch (IOException e) {
	                logger.error("Weibo UserInformation Response read failed.", e);
	            }
	        }
	
	        @Override
	        public void failed(Exception ex) {
	            logger.error("Weibo UserInformation Fetch Failed.", ex);
	        }
	
	        @Override
	        public void cancelled() {
	        }
	    });
    }
}
