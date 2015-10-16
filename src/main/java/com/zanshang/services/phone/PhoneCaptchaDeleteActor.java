package com.zanshang.services.phone;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.framework.BaseUntypedActor;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/4/15.
 */
public class PhoneCaptchaDeleteActor extends BaseUntypedActor {

    private Cache duplicateCache;

    private Cache verificationCache;
    public PhoneCaptchaDeleteActor(ApplicationContext spring) {
        super(spring);
        duplicateCache = getCacheManager().getCache(CacheConfig.CACHE_NAME_PHONE_VERIFICATION_DUPLICATE);
        verificationCache = getCacheManager().getCache(CacheConfig.CACHE_NAME_PHONE_VERIFICATION_CODE);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        verificationCache.evict(o.toString());
        duplicateCache.evict(o.toString());
    }
}
