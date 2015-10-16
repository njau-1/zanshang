package com.zanshang.framework.spring;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.utils.Cookie;
import com.zanshang.utils.Json;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.web.PortResolver;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Lookis on 7/1/15.
 */
@Service
public class CacheRequestCache implements RequestCache {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private PortResolver portResolver = new PortResolverImpl();

    private RequestMatcher requestMatcher = new MethodRequestMatcher();

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        if (requestMatcher.matches(request)) {
            DefaultSavedRequest savedRequest = new DefaultSavedRequest(request, portResolver);
            Cache cache = cacheManager.getCache(CacheConfig.CACHE_NAME_LOGIN_SAVED_REQUEST);
            String ticket = Cookie.getTicket(request, response);
            cache.put(ticket, Json.toJson(savedRequest));
        } else {
            logger.debug("Request not saved as configured RequestMatcher did not match");
        }
    }

    public SavedRequest getRequest(HttpServletRequest currentRequest, HttpServletResponse response) {
        Cache cache = cacheManager.getCache(CacheConfig.CACHE_NAME_LOGIN_SAVED_REQUEST);
        String ticket = Cookie.getTicket(currentRequest, response);
        String jsonRequest = cache.get(ticket, String.class);
        return jsonRequest == null ? null : Json.fromJson(jsonRequest, DefaultSavedRequest.class);
    }

    public void removeRequest(HttpServletRequest currentRequest, HttpServletResponse response) {
        Cache cache = cacheManager.getCache(CacheConfig.CACHE_NAME_LOGIN_SAVED_REQUEST);
        String ticket = Cookie.getTicket(currentRequest, response);
        cache.evict(ticket);
    }

    public HttpServletRequest getMatchingRequest(HttpServletRequest request, HttpServletResponse response) {
        DefaultSavedRequest saved = (DefaultSavedRequest) getRequest(request, response);
        if (saved == null) {
            return null;
        }

        if (!saved.doesRequestMatch(request, portResolver)) {
            logger.debug("saved request doesn't match");
            return null;
        }
        removeRequest(request, response);
        return new SavedRequestAwareWrapper(saved, request);
    }

    /**
     * Allows selective use of saved requests for a subset of requests. By default any
     * request will be cached by the {@code saveRequest} method.
     * <p>
     * If set, only matching requests will be cached.
     *
     * @param requestMatcher a request matching strategy which defines which requests
     *                       should be cached.
     */
    public void setRequestMatcher(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    public void setPortResolver(PortResolver portResolver) {
        this.portResolver = portResolver;
    }

    class MethodRequestMatcher implements RequestMatcher {

        @Override
        public boolean matches(HttpServletRequest request) {
            return request.getMethod().equalsIgnoreCase("get");
        }
    }
}
