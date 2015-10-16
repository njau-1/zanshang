package com.zanshang.framework.wechat;

import com.zanshang.constants.WechatPlatform;
import com.zanshang.controllers.web.RegisterController;
import com.zanshang.framework.CodeExpireException;
import com.zanshang.framework.spring.ForeignUsernameNotFoundException;
import com.zanshang.framework.spring.OAuth2AuthenticationFilter;
import com.zanshang.framework.spring.OAuth2AuthenticationToken;
import com.zanshang.services.WechatAuthorizationTrapdoor;
import com.zanshang.services.wechat.WechatAuthorizationTrapdoorImpl;
import com.zanshang.utils.AkkaTrapdoor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Lookis on 5/18/15.
 */
public class WechatOAuth2AuthenticationFilter extends OAuth2AuthenticationFilter {

    public static final String PLATFORM = "wechat";

    public static final String CALLBACK_REQUEST_PATH = "/" + PLATFORM + "/authentication";

    private WechatAuthorizationTrapdoor wechatAuthorizationTrapdoor;

    private AkkaTrapdoor akkaTrapdoor;

    public WechatOAuth2AuthenticationFilter() {
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(CALLBACK_REQUEST_PATH, "GET"));
    }

    @Override
    protected OAuth2AuthenticationToken buildAuthenticationToken(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        WechatPlatform platform;
        if (userAgent.contains("MicroMessenger")) {
            platform = WechatPlatform.MP;
        } else {
            platform = WechatPlatform.OPEN;
        }
        try {
            Map<String, String> accessTokenMap = wechatAuthorizationTrapdoor.fetchAndSave(request.getParameter("code"),
                    platform, false);
            WechatOAuth2AuthenticationToken token = new WechatOAuth2AuthenticationToken(accessTokenMap);
            return token;
        } catch (CodeExpireException e) {
            logger.debug("contains no accesstoken");
        }
        return null;
    }

    @Override
    protected void exceptionMapping(Map<String, String> exceptionMapping) {
        exceptionMapping.put(ForeignUsernameNotFoundException.class.getName(), RegisterController.REGISTER_PATH +
                RegisterController.CONNECT_PATH + "/" + PLATFORM);
    }

    public void setAkkaTrapdoor(AkkaTrapdoor akkaTrapdoor) {
        this.akkaTrapdoor = akkaTrapdoor;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(akkaTrapdoor);
        wechatAuthorizationTrapdoor = akkaTrapdoor.createTrapdoor(WechatAuthorizationTrapdoor.class,
                WechatAuthorizationTrapdoorImpl.class);
    }
}
