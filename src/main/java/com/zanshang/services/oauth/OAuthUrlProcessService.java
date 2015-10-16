package com.zanshang.services.oauth;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.constants.BusinessType;
import com.zanshang.constants.OAuthType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * Created by xuming on 15/8/25.
 */
@Component
public class OAuthUrlProcessService {

    @Autowired
    CacheManager cacheManager;

    @Value("${WECHAT_APPID}")
    String WECHAT_APPID;

    @Value("${WEIBO_APPID}")
    String WEIBO_APPID;

    @Value("${WECHAT_MP_ID}")
    String MP_ID;

    private String WECHAT_QR_URL(String redirectUrl) {
        return "https://open.weixin.qq.com/connect/qrconnect?appid=" + WECHAT_APPID + "&redirect_uri=" + redirectUrl + "&response_type=code&scope=snsapi_login#wechat_redirect";
    }

    private String WECHAT_OAUTH_URL(String redirectUrl) {
        return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + MP_ID +"&redirect_uri=" + redirectUrl + "&response_type=code&scope=snsapi_userinfo#wechat_redirect";
    }

    private String WEIBO_OAUTH_URL(String redirectUrl) {
        return "https://api.weibo.com/oauth2/authorize?client_id=" + WEIBO_APPID + "&response_type=code&redirect_uri=" + redirectUrl;
    }

    public String bulidUrl(BusinessType businessType, OAuthType oAuthType, String ticket, String returnUrl, String redirectUrl) {
        if (returnUrl != null && !returnUrl.isEmpty()) {
            final Cache cache = cacheManager.getCache(CacheConfig.CacheKey.OAUTH_PARAMS.getName());
            cache.put(ticket + businessType.getBusinessType(), returnUrl);
        }
        String url = "";
        switch (oAuthType) {
            case WECHAT: url = WECHAT_QR_URL(redirectUrl); break;
            case WECHAT_MP: url = WECHAT_OAUTH_URL(redirectUrl); break;
            case WEIBO: url = WEIBO_OAUTH_URL(redirectUrl); break;
        }
        return url;
    }

//    public Map<String, String> getParamsMap(BusinessType businessType, String ticket) {
//        final Cache cache = cacheManager.getCache(CacheConfig.CacheKey.OAUTH_PARAMS.getName());
//        String paramStr = cache.get(ticket + businessType.getBusinessType(), String.class);
//        if (paramStr != null && !paramStr.isEmpty()) {
//            String returnUrl = Json.fromJson(paramStr, String.class);
//            return returnUrl;
//        }
//        return null;
//    }

    public String getParamByKey(BusinessType businessType, String ticket, String key) {
        final Cache cache = cacheManager.getCache(CacheConfig.CacheKey.OAUTH_PARAMS.getName());
        String returnUrl = cache.get(ticket + businessType.getBusinessType(), String.class);
        if (returnUrl != null && !returnUrl.isEmpty()) {
            return returnUrl;
        }
        return null;
    }
}
