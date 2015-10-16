package com.zanshang.framework.spring;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.utils.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by Lookis on 5/4/15.
 */
@Service
public class CachedCsrfTokenRepository implements CsrfTokenRepository {

    private static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";

    private static final String DEFAULT_CSRF_HEADER_NAME = "X-CSRF-TOKEN";

    private String parameterName = DEFAULT_CSRF_PARAMETER_NAME;

    private String headerName = DEFAULT_CSRF_HEADER_NAME;

    private static final String CACHE_NAME = CacheConfig.CACHE_NAME_CSRFTOKEN;
    @Autowired
    private CacheManager cacheManager;

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return new DefaultCsrfToken(headerName, parameterName, createNewToken());
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        String ticket = Cookie.getTicket(request, response);
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if(token == null){
            cache.evict(ticket);
        }else {
            cache.put(ticket, token.getToken());
        }
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        String ticket = Cookie.containTicket(request);
        if (ticket == null){
            return null;
        }else{
            Cache cache = cacheManager.getCache(CACHE_NAME);
            String token = cache.get(ticket, String.class);
            return token == null? null: new DefaultCsrfToken(headerName, parameterName, token);
        }
    }

    /**
     * Sets the {@link HttpServletRequest} parameter name that the {@link CsrfToken} is
     * expected to appear on
     *
     * @param parameterName the new parameter name to use
     */
    public void setParameterName(String parameterName) {
        Assert.hasLength(parameterName, "parameterName cannot be null or empty");
        this.parameterName = parameterName;
    }

    /**
     * Sets the header name that the {@link CsrfToken} is expected to appear on and the
     * header that the response will contain the {@link CsrfToken}.
     *
     * @param headerName the new header name to use
     */
    public void setHeaderName(String headerName) {
        Assert.hasLength(headerName, "headerName cannot be null or empty");
        this.headerName = headerName;
    }

    private String createNewToken() {
        return UUID.randomUUID().toString();
    }
}
