package com.zanshang.framework.spring;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

/**
 * Created by Lookis on 5/7/15.
 */
public class ForeignUsernameNotFoundException extends UsernameNotFoundException {

    private Map<String, Object> foreignUser;

    public ForeignUsernameNotFoundException(Map<String, Object> foreignUser, Throwable t) {
        this(t.getMessage(), foreignUser, t);
    }

    public ForeignUsernameNotFoundException(String msg, Map<String, Object> foreignUser) {
        super(msg);
        this.foreignUser = foreignUser;
    }

    public ForeignUsernameNotFoundException(String msg, Map<String, Object> foreignUser, Throwable t) {
        super(msg, t);
        this.foreignUser = foreignUser;
    }

    public Map<String, Object> getForeignUser() {
        return foreignUser;
    }
}
