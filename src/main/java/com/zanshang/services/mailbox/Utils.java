package com.zanshang.services.mailbox;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.constants.EmailConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lookis on 6/23/15.
 */
 class Utils {

    public static String duplicateCacheKey(String templateName, String email) {
        return templateName + ":" + email;
    }

    public static String cacheName(String templateName){
        switch (templateName){
            case EmailConstants.EMAIL_ACTIVE_TEMPLATENAME:
                return CacheConfig.CACHE_NAME_EMAIL_VERIFICATION_CODE;
            case EmailConstants.EMAIL_UPDATE_TEMPLATENAME:
                return CacheConfig.CACHE_NAME_EMAIL_VERIFICATION_MATCHED_EMAIL;
            case EmailConstants.EMAIL_RESET_PASSWORD_TEMPLATENAME:
                return CacheConfig.CACHE_NAME_EMAIL_RESETPASSWORD_CODE;
            case EmailConstants.EMAIL_NOLOGIN_REGIST_TEMPLATENAME:
                return CacheConfig.CACHE_NAME_EMAIL_RESETPASSWORD_CODE;
            default:
                throw new RuntimeException("no cache register under:"+templateName);
        }
    }

}
