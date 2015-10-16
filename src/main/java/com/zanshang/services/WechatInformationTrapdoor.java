package com.zanshang.services;

import com.zanshang.models.WechatInformation;

/**
 * Created by Lookis on 6/4/15.
 */
public interface WechatInformationTrapdoor {

    WechatInformation get(String unionId);
}
