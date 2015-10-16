package com.zanshang.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by xuming on 15/8/11.
 */
public class SavedRequestAwareRegisterSuccessHandler {

    protected final Log logger = LogFactory.getLog(this.getClass());
    private String targetUrlParameter = null;
    private String targetUrl = null;
    private String defaultTargetUrl = null;
    private boolean alwaysUseDefaultTargetUrl = false;
    private boolean useReferer = false;
    private boolean useTargetUrlParameter = false;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private RequestCache requestCache = new HttpSessionRequestCache();

    public String onRegisterSuccess(HttpServletRequest request, HttpServletResponse response) {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        useTargetUrlParameter = false;
        if (savedRequest == null) {
            targetUrl = determineTargetUrl(request, response);
            if (useTargetUrlParameter) {
//                try {
//                    redirectStrategy.sendRedirect(request, response, (String) targetUrl);
                    clearAuthenticationAttributes(request);
                    return targetUrl;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
            return targetUrl;
        }
        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl()
                || (targetUrlParameter != null && StringUtils.hasText(request
                .getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            targetUrl = determineTargetUrl(request, response);
            clearAuthenticationAttributes(request);
            return targetUrl;
        }
        clearAuthenticationAttributes(request);
        // Use the DefaultSavedRequest URL
        return defaultTargetUrl;
    }

    protected String determineTargetUrl(HttpServletRequest request,
                                        HttpServletResponse response) {
        if (isAlwaysUseDefaultTargetUrl()) {
            return defaultTargetUrl;
        }

        // Check for the parameter and use that if available
        String targetUrl = null;

        if (targetUrlParameter != null) {
            targetUrl = request.getParameter(targetUrlParameter);

            if (StringUtils.hasText(targetUrl)) {
                logger.debug("Found targetUrlParameter in request: " + targetUrl);
                //use target url in parameter
                useTargetUrlParameter = true;
                return targetUrl;
            }
        }

        if (useReferer && !StringUtils.hasLength(targetUrl)) {
            targetUrl = request.getHeader("Referer");
            logger.debug("Using Referer header: " + targetUrl);
        }

        if (!StringUtils.hasText(targetUrl)) {
            logger.debug("Using default Url: " + targetUrl);
            return defaultTargetUrl;
        }

        return targetUrl;
    }

    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

    public void setAlwaysUseDefaultTargetUrl(boolean alwaysUseDefaultTargetUrl) {
        this.alwaysUseDefaultTargetUrl = alwaysUseDefaultTargetUrl;
    }

    protected boolean isAlwaysUseDefaultTargetUrl() {
        return alwaysUseDefaultTargetUrl;
    }
    public void setDefaultTargetUrl(String defaultTargetUrl) {
        Assert.isTrue(defaultTargetUrl != null,
                "defaultTarget is null");
        this.defaultTargetUrl = defaultTargetUrl;
    }
    public void setTargetUrlParameter(String targetUrlParameter) {
        if (targetUrlParameter != null) {
            Assert.hasText(targetUrlParameter, "targetUrlParameter cannot be empty");
        }
        this.targetUrlParameter = targetUrlParameter;
    }
    protected String getTargetUrlParameter() {
        return targetUrlParameter;
    }
}
