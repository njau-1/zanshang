package com.zanshang.framework.index;

import org.springframework.data.domain.Pageable;

/**
 * Created by Lookis on 7/3/15.
 */
public class IndexPageableParams {

    private Pageable pageable;

    private Object key;

    public IndexPageableParams(Pageable pageable, Object key) {

        this.pageable = pageable;
        this.key = key;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public Object getKey() {
        return key;
    }
}
