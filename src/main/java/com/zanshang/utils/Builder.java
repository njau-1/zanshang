package com.zanshang.utils;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.springframework.security.web.PortResolver;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

/**
 * Created by Lookis on 7/16/15.
 */
public class Builder {

    /**
     * List<Cookie> cookies = (List<Cookie>) value.get("cookies");
     * List<Locale> locales = (List<Locale>) value.get("locales");
     * Map<String, String[]> parameters = (Map<String, String[]>) value.get("parameters");
     * Map<String, List<String>> headers = (Map<String, List<String>>) value.get("headers");
     * String contextPath = (String) value.get("contextPath");
     * String method = (String) value.get("method");
     * String pathInfo = (String) value.get("pathInfo");
     * String queryString = (String) value.get("queryString");
     * String requestURI = (String) value.get("requestURI");
     * String requestURL = (String) value.get("requestURL");
     * String scheme = (String) value.get("scheme");
     * String serverName = (String) value.get("serverName");
     * String servletPath = (String) value.get("servletPath");
     * int serverPort = (int) value.get("serverPort");
     *
     * @return
     */
    public static DefaultSavedRequest build(List<Cookie> cookies, List<Locale> locales, Map<String, String[]>
            parameters, Map<String, List<String>> headers, String contextPath, String method, String pathInfo, String
            queryString, String requestURI, String requestURL, String scheme, String serverName, String servletPath,
                                            int serverPort) {
        return new DefaultSavedRequest(new HttpServletRequestWrapper(new HttpServletRequest() {
            @Override
            public String getAuthType() {
                return null;
            }

            @Override
            public javax.servlet.http.Cookie[] getCookies() {
                return new javax.servlet.http.Cookie[0];
            }

            @Override
            public long getDateHeader(String name) {
                return 0;
            }

            @Override
            public String getHeader(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                return null;
            }

            @Override
            public int getIntHeader(String name) {
                return 0;
            }

            @Override
            public String getMethod() {
                return null;
            }

            @Override
            public String getPathInfo() {
                return null;
            }

            @Override
            public String getPathTranslated() {
                return null;
            }

            @Override
            public String getContextPath() {
                return null;
            }

            @Override
            public String getQueryString() {
                return null;
            }

            @Override
            public String getRemoteUser() {
                return null;
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }

            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public String getRequestedSessionId() {
                return null;
            }

            @Override
            public String getRequestURI() {
                return null;
            }

            @Override
            public StringBuffer getRequestURL() {
                return null;
            }

            @Override
            public String getServletPath() {
                return null;
            }

            @Override
            public HttpSession getSession(boolean create) {
                return null;
            }

            @Override
            public HttpSession getSession() {
                return null;
            }

            @Override
            public boolean isRequestedSessionIdValid() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromCookie() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromURL() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromUrl() {
                return false;
            }

            @Override
            public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
                return false;
            }

            @Override
            public void login(String username, String password) throws ServletException {

            }

            @Override
            public void logout() throws ServletException {

            }

            @Override
            public Collection<Part> getParts() throws IOException, ServletException {
                return null;
            }

            @Override
            public Part getPart(String name) throws IOException, ServletException {
                return null;
            }

            @Override
            public Object getAttribute(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getAttributeNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

            }

            @Override
            public int getContentLength() {
                return 0;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletInputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public String getParameter(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getParameterNames() {
                return null;
            }

            @Override
            public String[] getParameterValues(String name) {
                return new String[0];
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return null;
            }

            @Override
            public String getProtocol() {
                return null;
            }

            @Override
            public String getScheme() {
                return null;
            }

            @Override
            public String getServerName() {
                return null;
            }

            @Override
            public int getServerPort() {
                return 0;
            }

            @Override
            public BufferedReader getReader() throws IOException {
                return null;
            }

            @Override
            public String getRemoteAddr() {
                return null;
            }

            @Override
            public String getRemoteHost() {
                return null;
            }

            @Override
            public void setAttribute(String name, Object o) {

            }

            @Override
            public void removeAttribute(String name) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public Enumeration<Locale> getLocales() {
                return null;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public RequestDispatcher getRequestDispatcher(String path) {
                return null;
            }

            @Override
            public String getRealPath(String path) {
                return null;
            }

            @Override
            public int getRemotePort() {
                return 0;
            }

            @Override
            public String getLocalName() {
                return null;
            }

            @Override
            public String getLocalAddr() {
                return null;
            }

            @Override
            public int getLocalPort() {
                return 0;
            }

            @Override
            public ServletContext getServletContext() {
                return null;
            }

            @Override
            public AsyncContext startAsync() throws IllegalStateException {
                return null;
            }

            @Override
            public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws
                    IllegalStateException {
                return null;
            }

            @Override
            public boolean isAsyncStarted() {
                return false;
            }

            @Override
            public boolean isAsyncSupported() {
                return false;
            }

            @Override
            public AsyncContext getAsyncContext() {
                return null;
            }

            @Override
            public DispatcherType getDispatcherType() {
                return null;
            }
        }) {
            @Override
            public javax.servlet.http.Cookie[] getCookies() {
                return cookies.stream().toArray(Cookie[]::new);
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                return new IteratorEnumeration(headers.keySet().iterator());
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                return new IteratorEnumeration(headers.get(name).iterator());
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return parameters;
            }

            @Override
            public Enumeration<Locale> getLocales() {
                return new IteratorEnumeration(locales.iterator());
            }

            @Override
            public String getMethod() {
                return method;
            }

            @Override
            public String getPathInfo() {
                return pathInfo;
            }

            @Override
            public String getQueryString() {
                return queryString;
            }

            @Override
            public String getRequestURI() {
                return requestURI;
            }

            @Override
            public StringBuffer getRequestURL() {
                return new StringBuffer(requestURL);
            }

            @Override
            public String getScheme() {
                return scheme;
            }

            @Override
            public String getServerName() {
                return serverName;
            }

            @Override
            public String getContextPath() {
                return contextPath;
            }

            @Override
            public String getServletPath() {
                return servletPath;
            }
        }, new PortResolver() {
            @Override
            public int getServerPort(ServletRequest request) {
                return serverPort;
            }
        });
    }
}
