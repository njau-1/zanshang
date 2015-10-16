package com.zanshang.captcha;

import com.zanshang.config.spring.CacheConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Created by Lookis on 9/29/15.
 */
@Service
public class ImageCaptchaService implements CaptchaService {

    @Autowired
    CacheManager cacheManager;

    @Override
    public boolean verify(String key, String inputCode) throws CaptchaException {
        final Cache cache = cacheManager.getCache(CacheConfig.CacheKey.CAPTCHA.getName());
        final String expected = cache.get(key, String.class);
        if (StringUtils.equals(expected, inputCode)) {
            cache.evict(key);
            return true;
        }else{
            return false;
        }
    }
}
