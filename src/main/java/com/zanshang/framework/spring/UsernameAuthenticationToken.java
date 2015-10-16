package com.zanshang.framework.spring;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by Lookis on 5/5/15.
 */
public class UsernameAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public UsernameAuthenticationToken(Object principal) {
        super(principal, null);
    }

    public UsernameAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(principal, null, authorities);
    }
}
