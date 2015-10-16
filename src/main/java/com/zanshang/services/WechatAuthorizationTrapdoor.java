package com.zanshang.services;

import com.zanshang.constants.WechatPlatform;

import java.util.Map;

/**
 * Created by Lookis on 6/4/15.
 */
public interface WechatAuthorizationTrapdoor {

//    Map<String, String> get(String unionId, WechatPlatform platform);

    Map<String, String> fetchAndSave(String code, WechatPlatform platform, boolean baseApi);

    void save(Map<String, String> wechatAuthorization);

    void delete(String unionId);
}
