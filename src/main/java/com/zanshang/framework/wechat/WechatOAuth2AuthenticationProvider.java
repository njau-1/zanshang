package com.zanshang.framework.wechat;

import com.zanshang.framework.spring.OAuth2AuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Lookis on 5/18/15.
 */
public class WechatOAuth2AuthenticationProvider extends OAuth2AuthenticationProvider {

    @Autowired
    public WechatOAuth2AuthenticationProvider(WechatUserDetailsService wechatUserDetailsService) {
        setDetailsService(wechatUserDetailsService);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatOAuth2AuthenticationToken.class.equals(authentication);
    }
}
