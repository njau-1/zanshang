package com.zanshang.services;

import com.zanshang.models.WeiboInformation;

/**
 * Created by Lookis on 6/4/15.
 */
public interface WeiboInformationTrapdoor {

    WeiboInformation get(String openId);
}
