package com.zanshang.framework.weibo;

import com.zanshang.controllers.web.RegisterController;
import com.zanshang.framework.spring.ForeignUsernameNotFoundException;
import com.zanshang.framework.spring.OAuth2AuthenticationFilter;
import com.zanshang.framework.spring.OAuth2AuthenticationToken;
import com.zanshang.services.WeiboAuthorizationTrapdoor;
import com.zanshang.services.weibo.WeiboAuthorizationTrapdoorImpl;
import com.zanshang.utils.AkkaTrapdoor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Lookis on 5/18/15.
 */
public class WeiboOAuth2AuthenticationFilter extends OAuth2AuthenticationFilter {

    public static final String PLATFORM = "weibo";

    private String serverContext;

    public static final String CALLBACK_REQUEST_PATH = "/" + PLATFORM + "/authentication";

    private WeiboAuthorizationTrapdoor weiboAuthorizationTrapdoor;

    AkkaTrapdoor akkaTrapdoor;

    public WeiboOAuth2AuthenticationFilter() {
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(CALLBACK_REQUEST_PATH, "GET"));
    }

    @Override
    protected void exceptionMapping(Map<String, String> exceptionMapping) {
        exceptionMapping.put(ForeignUsernameNotFoundException.class.getName(), RegisterController.REGISTER_PATH +
                RegisterController.CONNECT_PATH + "/" + PLATFORM);
    }

    @Override
    protected OAuth2AuthenticationToken buildAuthenticationToken(HttpServletRequest request) {
        String callback = callbackServerContext+request.getContextPath() + CALLBACK_REQUEST_PATH;
        String code = request.getParameter("code");
        Map<String, String> accessTokenMap = weiboAuthorizationTrapdoor.fetchAndSave(code, callback);
        WeiboOAuth2AuthenticationToken token = new WeiboOAuth2AuthenticationToken(accessTokenMap);
        return token;
    }

    public void setAkkaTrapdoor(AkkaTrapdoor akkaTrapdoor) {
        this.akkaTrapdoor = akkaTrapdoor;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(akkaTrapdoor);
        weiboAuthorizationTrapdoor = akkaTrapdoor.createTrapdoor(WeiboAuthorizationTrapdoor.class,
                WeiboAuthorizationTrapdoorImpl.class);
    }
}
