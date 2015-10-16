package com.zanshang.framework.filter;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.utils.Cookie;
import com.zanshang.utils.Json;
import com.zanshang.utils.UserCountUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuming on 15/9/23.
 */
public class UserCountFilter implements Filter {

    private final static String PRINCIPAL = "principal";

    protected CacheManager cacheManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebApplicationContext springContext =
                WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
        cacheManager = springContext.getBean(CacheManager.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String nologinUserFlag = (String) request.getAttribute(UserCountUtils.NOLGINUSERFLAG);
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String ticket = Cookie.getTicket(request, response);
        Cache cache = cacheManager.getCache(CacheConfig.CACHE_NAME_AUTHENTICATION);
        String principal = cache.get(ticket, String.class);
        if (nologinUserFlag != null && !nologinUserFlag.isEmpty()) {
            UserCountUtils.recordLoginUser(ticket, nologinUserFlag);
        } else {
            if (principal != null && !principal.isEmpty()) {
                Map<String, Object> authMap = Json.fromJson(principal, Map.class);
                UserCountUtils.recordLoginUser(ticket, (String) authMap.get(PRINCIPAL));
            } else {
                UserCountUtils.recordAccessUser(ticket);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
