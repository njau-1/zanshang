package com.zanshang.utils;

import com.zanshang.config.spring.CacheConfig;
import org.bson.types.ObjectId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Lookis on 5/18/15.
 */
public class Cookie {

    private static String COOKIE_TICKET_KEY = "t";

    private static String COOKIE_TICKET_IN_REQUEST = "$$COOKIE_IN_REQUEST";

    public static String containTicket(HttpServletRequest request) {
        String ticket = null;
        javax.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (javax.servlet.http.Cookie cookie : cookies) {
                if (ticket == null && cookie.getName().equals(COOKIE_TICKET_KEY)) {
                    ticket = cookie.getValue();
                    return ticket;
                }
            }
        }else if(request.getAttribute(COOKIE_TICKET_IN_REQUEST)!= null){
            return request.getAttribute(COOKIE_TICKET_IN_REQUEST).toString();
        }
        return null;
    }

    public static String getTicket(HttpServletRequest request, HttpServletResponse response) {
        String ticket = containTicket(request);
        if (ticket == null) {
            ticket = ObjectId.get().toHexString();
            javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(COOKIE_TICKET_KEY, ticket);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            int maxAge = ((Long) CacheConfig.CacheKey.AUTHENTICATION.getTimeUnit().toSeconds(CacheConfig.CacheKey.AUTHENTICATION.getTime())).intValue();
            cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
            request.setAttribute(COOKIE_TICKET_IN_REQUEST, ticket);
        }
        return ticket;
    }
}
