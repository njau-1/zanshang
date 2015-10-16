package com.zanshang.framework.weibo;

import com.zanshang.framework.spring.OAuth2AuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Lookis on 5/18/15.
 */
public class WeiboOAuth2AuthenticationProvider extends OAuth2AuthenticationProvider {

    @Autowired
    public WeiboOAuth2AuthenticationProvider(WeiboUserDetailsService weiboUserDetailsService) {
        setDetailsService(weiboUserDetailsService);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WeiboOAuth2AuthenticationToken.class.equals(authentication);
    }
}
