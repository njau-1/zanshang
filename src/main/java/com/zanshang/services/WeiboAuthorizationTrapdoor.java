package com.zanshang.services;

import java.util.Map;

/**
 * Created by Lookis on 6/4/15.
 */
public interface WeiboAuthorizationTrapdoor {

    Map<String, String> get(String openId);

    Map<String, String> fetchAndSave(String code, String callbackUrl);

    void save(Map<String, String> weixinAuthorization);

    void delete(String openId);
}
