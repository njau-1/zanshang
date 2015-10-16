package com.zanshang.utils;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.bson.types.ObjectId;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.makeAccessible;

/**
 * Created by Lookis on 4/29/15.
 */
public class Json {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        SimpleModule myModule = new SimpleModule("ObjectIdSerializer", new Version(1, 0, 0, null, null, null));
        myModule.addSerializer(new Json.ObjectIdSerializer());
        myModule.setMixInAnnotation(DefaultSavedRequest.class, SavedRequest.class);
        mapper.registerModule(myModule);
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clz) {
        try {
            return mapper.readValue(json, clz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(InputStream inputStream, Class<T> clz) {
        try {
            return mapper.readValue(inputStream, clz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class ObjectIdSerializer extends ToStringSerializer {

        public ObjectIdSerializer() {
            super(ObjectId.class);
        }
    }

    @JsonSerialize(converter = SavedRequestToMapConverter.class)
    @JsonDeserialize(converter = MapToSavedRequestConverter.class)
    static class SavedRequest {

    }

    private static Locale convertLocale(Map<String, String> locale){
        String country = locale.get("country");
        String variant = locale.get("variant");
        String language = locale.get("language");
        return new Locale(language,country,variant);
    }

    private static Map<String,String> convertLocale(Locale locale){
        String country = locale.getCountry();
        String variant = locale.getVariant();
        String language = locale.getLanguage();
        Map<String,String> retMap = new HashMap<>();
        retMap.put("country", country);
        retMap.put("variant", variant);
        retMap.put("language", language);
        return retMap;
    }

    private static Cookie convertCookie(Map<String, Object> value) {
        String name = (String) value.get("name");
        String v = (String) value.get("value");
        String comment = (String) value.get("comment");
        String domain = (String) value.get("domain");
        int maxAge = (int) value.get("maxAge");
        String path = (String) value.get("path");
        boolean secure = (boolean) value.get("secure");
        int version = (int) value.get("version");
        boolean httpOnly = (boolean) value.get("httpOnly");
        Cookie cookie = new Cookie(name, v);
        if (comment != null)
            cookie.setComment(comment);
        if (domain != null)
            cookie.setDomain(domain);
        cookie.setMaxAge(maxAge);
        if (path != null)
            cookie.setPath(path);
        if (secure)
            cookie.setSecure(secure);
        cookie.setVersion(version);
        cookie.setHttpOnly(httpOnly);
        return cookie;
    }

    private static Map<String, Object> convertCookie(Cookie value) {
        Map<String, Object> cookieMap = new HashMap<>();
        cookieMap.put("name", value.getName());
        cookieMap.put("value", value.getValue());
        cookieMap.put("comment", value.getComment());
        cookieMap.put("domain", value.getDomain());
        cookieMap.put("maxAge", value.getMaxAge());
        cookieMap.put("path", value.getPath());
        cookieMap.put("secure", value.getSecure());
        cookieMap.put("version", value.getVersion());
        cookieMap.put("httpOnly", value.isHttpOnly());
        return cookieMap;
    }

    static class SavedRequestToMapConverter implements Converter<DefaultSavedRequest, Map<String, Object>> {

        @Override
        public Map<String, Object> convert(DefaultSavedRequest value) {
            List<Cookie> cookies = value.getCookies();
            List<Locale> locales = value.getLocales();
            Map<String, String[]> parameterMap = value.getParameterMap();
            Field headers = findField(DefaultSavedRequest.class, "headers");
            makeAccessible(headers);
            Map<String, List<String>> o;
            try {
                o = (Map<String, List<String>>) headers.get(value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            String contextPath = value.getContextPath();
            String method = value.getMethod();
            String pathInfo = value.getPathInfo();
            String queryString = value.getQueryString();
            String requestURI = value.getRequestURI();
            String requestURL = value.getRequestURL();
            String scheme = value.getScheme();
            String serverName = value.getServerName();
            String servletPath = value.getServletPath();
            int serverPort = value.getServerPort();

            Map<String, Object> requestMap = new HashMap<>();
            List<Map<String, Object>> cookieList = new ArrayList<>();
            for (Cookie cookie : cookies) {
                cookieList.add(convertCookie(cookie));
            }

            List<Map<String, String>> localeList = new ArrayList<>();
            for (Locale locale : locales) {
                localeList.add(convertLocale(locale));
            }
            requestMap.put("cookies", cookieList);
            requestMap.put("locales", localeList);
            requestMap.put("parameters", parameterMap);
            requestMap.put("headers", o);
            requestMap.put("contextPath", contextPath);
            requestMap.put("method", method);
            requestMap.put("pathInfo", pathInfo);
            requestMap.put("queryString", queryString);
            requestMap.put("requestURI", requestURI);
            requestMap.put("requestURL", requestURL);
            requestMap.put("scheme", scheme);
            requestMap.put("serverName", serverName);
            requestMap.put("servletPath", servletPath);
            requestMap.put("serverPort", serverPort);
            return requestMap;
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(DefaultSavedRequest.class);
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructMapLikeType(HashMap.class, String.class, Object.class);
        }
    }

    static class MapToSavedRequestConverter implements Converter<Map<String, Object>, DefaultSavedRequest> {

        @Override
        public DefaultSavedRequest convert(Map<String, Object> value) {
            List<Map<String, Object>> cookieList = (List<Map<String, Object>>) value.get("cookies");
            List<Cookie> cookies = cookieList.stream().map(stringObjectMap -> convertCookie(stringObjectMap)).collect
                    (Collectors.toList());
            List<Map<String, String>> localeList = (List<Map<String, String>>) value.get("locales");
            List<Locale> locales = localeList.stream().map(locale -> convertLocale(locale)).collect
                    (Collectors.toList());
            Map<String, String[]> parameters = (Map<String, String[]>) value.get("parameters");
            Map<String, List<String>> headers = (Map<String, List<String>>) value.get("headers");
            String contextPath = (String) value.get("contextPath");
            String method = (String) value.get("method");
            String pathInfo = (String) value.get("pathInfo");
            String queryString = (String) value.get("queryString");
            String requestURI = (String) value.get("requestURI");
            String requestURL = (String) value.get("requestURL");
            String scheme = (String) value.get("scheme");
            String serverName = (String) value.get("serverName");
            String servletPath = (String) value.get("servletPath");
            int serverPort = (int) value.get("serverPort");
            return Builder.build(cookies, locales, parameters, headers, contextPath, method, pathInfo, queryString,
                    requestURI, requestURL, scheme, serverName, servletPath, serverPort);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructMapLikeType(HashMap.class, String.class, Object.class);
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(DefaultSavedRequest.class);
        }
    }
}
