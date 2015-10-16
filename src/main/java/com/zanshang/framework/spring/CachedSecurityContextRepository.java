package com.zanshang.framework.spring;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.utils.Cookie;
import com.zanshang.utils.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Lookis on 4/30/15.
 */
@Service
public class CachedSecurityContextRepository implements SecurityContextRepository {

    private static final String PRINCIPAL = "principal";
    private static final String AUTHORITIES = "authorities";
    private static final String CACHE_NAME = CacheConfig.CACHE_NAME_AUTHENTICATION;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        SaveToCookieResponseWrapper response = new SaveToCookieResponseWrapper(requestResponseHolder.getResponse());
        requestResponseHolder.setResponse(response);
        String ticket = Cookie.getTicket(requestResponseHolder.getRequest(), response);
        response.setTicket(ticket);
        Cache cache = cacheManager.getCache(CACHE_NAME);
        String strAuth = cache.get(ticket, String.class);
        SecurityContextImpl securityContext = new SecurityContextImpl();
        if (strAuth != null) {
            Map<String, Object> authMap = Json.fromJson(strAuth, Map.class);
            UsernameAuthenticationToken authentication = new UsernameAuthenticationToken(authMap.get(PRINCIPAL), ((List<String>) authMap.get(AUTHORITIES)).stream().map(auth -> new SimpleGrantedAuthority(auth)).collect(Collectors.toList()));
            securityContext.setAuthentication(authentication);
        }

        return securityContext;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        SaveToCookieResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, SaveToCookieResponseWrapper.class);
        if (responseWrapper == null) {
            throw new IllegalStateException("Cannot invoke saveContext on response " + response + ". You must use the HttpRequestResponseHolder.response after invoking loadContext");
        }
        responseWrapper.saveContext(context);
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String ticket = Cookie.containTicket(request);
        if (ticket != null) {
            Cache cache = cacheManager.getCache(CACHE_NAME);
            Cache.ValueWrapper valueWrapper = cache.get(ticket);
            return valueWrapper != null && valueWrapper.get() != null;
        }
        return false;
    }

    class SaveToCookieResponseWrapper extends SaveContextOnUpdateOrErrorResponseWrapper {

        private String ticket;

        /**
         * @param response the response to be wrapped
         */
        public SaveToCookieResponseWrapper(HttpServletResponse response) {
            super(response, false);
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        @Override
        protected void saveContext(SecurityContext context) {
            if (!isContextSaved() && !(context.getAuthentication() instanceof AnonymousAuthenticationToken)) {
                Cache cache = cacheManager.getCache(CACHE_NAME);
                if (context.getAuthentication() == null) {
                    cache.evict(ticket);
                } else {
                    Map<String, Object> authMap = new HashMap<>();
                    authMap.put(PRINCIPAL, context.getAuthentication().getName());
                    Collection<? extends GrantedAuthority> authorities = context.getAuthentication().getAuthorities();
                    authMap.put(AUTHORITIES, authorities.stream().map(authority -> authority.getAuthority()).collect(Collectors.toList()));
                    cache.put(ticket, Json.toJson(authMap));
                }
            }
        }
    }
}