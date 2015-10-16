package com.zanshang.framework.wechat;

import com.zanshang.framework.spring.OAuth2AuthenticationToken;

import java.util.Map;

/**
 * Created by Lookis on 5/18/15.
 */
public class WechatOAuth2AuthenticationToken extends OAuth2AuthenticationToken {

    public WechatOAuth2AuthenticationToken(Map<String, String> accessTokenMap) {
        super(accessTokenMap);
    }
}
