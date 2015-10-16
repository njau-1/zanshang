package com.zanshang.services.phone;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.framework.BaseUntypedActor;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/4/15.
 */
public class PhoneCaptchaGetActor extends BaseUntypedActor {


    private Cache verificationCache;

    public PhoneCaptchaGetActor(ApplicationContext spring) {
        super(spring);
        verificationCache = getCacheManager().getCache(CacheConfig.CACHE_NAME_PHONE_VERIFICATION_CODE);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        String code = verificationCache.get(o.toString(), String.class);
        getSender().tell(code, getSelf());
    }
}
