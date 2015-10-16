package com.zanshang.framework.spring;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by Lookis on 5/5/15.
 */
public abstract class OAuth2AuthenticationProvider implements AuthenticationProvider, InitializingBean {

    private ForeignUserDetailsService detailsService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(detailsService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Map<String, Object> accessTokenMap = (Map<String, Object>) authentication.getPrincipal();
        UserDetails details = detailsService.loadUserByAccessToken(accessTokenMap);
        UsernameAuthenticationToken authenticationToken = new UsernameAuthenticationToken(details.getUsername(), details.getAuthorities());
        authenticationToken.setDetails(authentication.getDetails());
        return authenticationToken;
    }

    public void setDetailsService(ForeignUserDetailsService detailsService) {
        this.detailsService = detailsService;
    }
}
