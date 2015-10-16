package com.zanshang.framework.weibo;

import com.zanshang.framework.spring.OAuth2AuthenticationToken;

import java.util.Map;

/**
 * Created by Lookis on 5/18/15.
 */
public class WeiboOAuth2AuthenticationToken extends OAuth2AuthenticationToken {

    public WeiboOAuth2AuthenticationToken(Map<String, String> accessTokenMap) {
        super(accessTokenMap);
    }
}
