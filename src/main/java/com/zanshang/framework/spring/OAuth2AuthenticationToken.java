package com.zanshang.framework.spring;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Map;

/**
 * Created by Lookis on 5/5/15.
 */
public abstract class OAuth2AuthenticationToken extends AbstractAuthenticationToken {

    private Map<String,String> accessTokenMap;

    public OAuth2AuthenticationToken(Map<String, String> accessTokenMap) {
        super(null);
        this.accessTokenMap = accessTokenMap;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return accessTokenMap;
    }

}
