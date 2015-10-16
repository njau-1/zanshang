package com.zanshang.framework.spring;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.utils.Cookie;
import com.zanshang.utils.Json;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lookis on 5/5/15.
 */
public abstract class OAuth2AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    protected String callbackServerContext;

    protected CacheManager cacheManager;

    public OAuth2AuthenticationFilter() {
        super(new AntPathRequestMatcher("/oauth2/callback", "GET"));
        ExceptionMappingAuthenticationFailureHandler handler = initAuthenticationFailureHandler();
        setAuthenticationFailureHandler(handler);
    }

    private ExceptionMappingAuthenticationFailureHandler initAuthenticationFailureHandler() {
        ExceptionMappingAuthenticationFailureHandler handler = new AuthenticationFailureSavedHandler();
        Map<String, String> exceptionMapping = new HashMap<>();
        exceptionMapping(exceptionMapping);
        handler.setExceptionMappings(exceptionMapping);
        handler.setUseForward(true);
        return handler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        OAuth2AuthenticationToken authRequest = buildAuthenticationToken(request);
        if (authRequest != null){
            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
            return null;
        }
    }

    protected abstract OAuth2AuthenticationToken buildAuthenticationToken(HttpServletRequest request);
    protected abstract void exceptionMapping(Map<String, String> exceptionMapping);

    public void setCallbackServerContext(String serverContext){
        this.callbackServerContext = serverContext;
    }

    class AuthenticationFailureSavedHandler extends ExceptionMappingAuthenticationFailureHandler{

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            String ticket = Cookie.getTicket(request, response);
            ForeignUsernameNotFoundException e = (ForeignUsernameNotFoundException) exception;
            Cache cache = cacheManager.getCache(CacheConfig.CACHE_NAME_PRE_REGISTER_ACCESSTOKEN);
            cache.put(ticket, Json.toJson(e.getForeignUser()));
            super.onAuthenticationFailure(request, response, exception);
        }
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(cacheManager);
    }
}
