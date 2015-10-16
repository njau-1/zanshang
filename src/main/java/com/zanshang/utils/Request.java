package com.zanshang.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Lookis on 6/18/15.
 */
public class Request {

    static Logger logger = LoggerFactory.getLogger(Request.class);

    public static String getRemoteIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }

    public static boolean isWechat(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent.contains("MicroMessenger");
    }

    public static String buildCurrentURL(HttpServletRequest request) {
        return buildCurrentURL(request, Collections.emptyList());
    }

    public static String buildCurrentURL(HttpServletRequest request, Collection<String> removedParams) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        if (queryString == null) {
            return requestURL.toString();
        } else {
            StringBuffer queryStringBuilder = new StringBuffer();
            String[] nvPairs = queryString.split("&");
            for (String nvPair : nvPairs) {
                String[] nv = nvPair.split("=");
                if (!removedParams.contains(nv[0])) {
                    queryStringBuilder.append(nvPair);
                    queryStringBuilder.append("&");
                }
            }
            if (queryStringBuilder.length() > 0) {
                queryStringBuilder.deleteCharAt(queryStringBuilder.length() - 1);
            }
            return requestURL.append('?').append(queryStringBuilder.toString()).toString();
        }
    }

    public static String encode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Encode Error.", e);
            throw new RuntimeException(e);
        }
    }
}
