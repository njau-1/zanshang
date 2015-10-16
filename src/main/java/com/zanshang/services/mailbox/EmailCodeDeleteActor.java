package com.zanshang.services.mailbox;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.framework.BaseUntypedActor;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/4/15.
 */
public class EmailCodeDeleteActor extends BaseUntypedActor {

    private Cache duplicateCache;


    public EmailCodeDeleteActor(ApplicationContext spring) {
        super(spring);
        duplicateCache = getCacheManager().getCache(CacheConfig.CACHE_NAME_EMAIL_DUPLICATE);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Parameter params = (Parameter) o;
        duplicateCache.evict(Utils.duplicateCacheKey(params.getTemplateName(), params.getEmail()));
        Cache verificationCache = getCacheManager().getCache(Utils.cacheName(params.getTemplateName()));
        verificationCache.evict(params.getEmail());
    }

    public static class Parameter {

        private String templateName;

        private String email;

        public Parameter(String templateName, String email) {
            this.templateName = templateName;
            this.email = email;
        }

        public String getTemplateName() {

            return templateName;
        }

        public String getEmail() {
            return email;
        }
    }
}
