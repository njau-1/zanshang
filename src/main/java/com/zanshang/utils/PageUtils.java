package com.zanshang.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lookis on 6/26/15.
 */
public class PageUtils {

    public static <V> Page<V> paging(Pageable pageable, List<V> value) {
        int startIndex = pageable.getPageNumber() * pageable.getPageSize();
        int endIndex = (pageable.getPageNumber() + 1) * pageable.getPageSize();
        Page<V> page;
        if (startIndex > value.size()) {
            page = new PageImpl<V>(new ArrayList<>(), pageable, value.size());
        } else if (endIndex > value.size()) {
            page = new PageImpl<V>(value.subList(startIndex, value.size()), pageable, value.size());
        } else {
            page = new PageImpl<V>(value.subList(startIndex, endIndex), pageable, value.size());
        }
        return page;
    }
}
