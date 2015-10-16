package com.zanshang.services.mailbox;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.zanshang.config.spring.CacheConfig;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lookis on 6/23/15.
 */
public class EmailCodeCreateActor extends BaseUntypedActor {

    private Cache duplicateCache;

    ActorRef emailActor;

    public EmailCodeCreateActor(ApplicationContext spring) {
        super(spring);
        emailActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(EmailCreateActor.class, spring)));
        duplicateCache = getCacheManager().getCache(CacheConfig.CACHE_NAME_EMAIL_DUPLICATE);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Parameters params = (Parameters) o;
        String email = params.getEmail();
        Cache.ValueWrapper valueWrapper = duplicateCache.get(Utils.duplicateCacheKey(params.getTemplateName(), email));
        if (valueWrapper == null || valueWrapper.get() == null) {
            String cacheName = Utils.cacheName(params.getTemplateName());
            Cache verificationCache = getCacheManager().getCache(cacheName);
            String code = verificationCache.get(email, String.class);
            if (code == null) {
                code = RandomStringUtils.randomAlphanumeric(10);
                verificationCache.put(email, code);
            }
            Map<String, String> model = new HashMap<>(params.getModel());
            model.put("code", code);
            emailActor.tell(new EmailCreateActor.Parameters(email, null, params.getSubject(), params.getTemplateName
                    (), model), getSelf());
            duplicateCache.put(Utils.duplicateCacheKey(params.getTemplateName(), email), Boolean.TRUE.toString());
        }
    }

    public static class Parameters {

        private String subject;

        private String templateName;

        private String email;

        private Map<String, String> model;

        public Parameters(String templateName, String email, Map<String, String> model) {
            this.templateName = templateName;
            this.subject = null;
            this.email = email;
            this.model = model;
        }

        public Parameters(String templateName, String email, String subject, Map<String, String> model) {
            this.templateName = templateName;
            this.email = email;
            this.subject = subject;
            if (model == null) {
                this.model = new HashMap<>();
            } else
                this.model = model;
        }

        public String getSubject() {
            return subject;
        }

        public Map<String, String> getModel() {

            return model;
        }

        public String getTemplateName() {

            return templateName;
        }

        public String getEmail() {
            return email;
        }
    }
}
