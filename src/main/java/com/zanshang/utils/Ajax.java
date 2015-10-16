package com.zanshang.utils;

import com.zanshang.config.spring.MvcConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lookis on 4/29/15.
 */
public class Ajax {

    public static Map<String, Object> ok() {
        return ok(null);
    }

    public static Map<String, Object> ok(Object result) {
        return ret("ok", result);
    }

    public static Map<String, Object> failure(Object result) {
        return ret("fail", result);
    }

    public static Map<String, Object> ret(String code, Object result) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("code", code);
        if (result != null) {
            ret.put("result", result);
        }
        return ret;
    }

    public static Map<String, Object> getPagedModel(Page<?> messages) {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("content", messages.getContent());
        if (messages.hasNext()) {
            Pageable next = messages.nextPageable();
            Map<String, Object> nextPage = new HashMap<>();
            nextPage.put(MvcConfig.DEFAULT_PAGE_PARAMETER, next.getPageNumber());
            nextPage.put(MvcConfig.DEFAULT_SIZE_PARAMETER, next.getPageSize());
            retMap.put("next", nextPage);
        }
        if (messages.hasPrevious()) {
            Pageable pre = messages.previousPageable();
            Map<String, Object> nextPage = new HashMap<>();
            nextPage.put(MvcConfig.DEFAULT_PAGE_PARAMETER, pre.getPageNumber());
            nextPage.put(MvcConfig.DEFAULT_SIZE_PARAMETER, pre.getPageSize());
            retMap.put("previous", nextPage);
        }
        return retMap;
    }
}
