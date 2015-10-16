package com.zanshang.services.mailbox;

import akka.actor.InvalidMessageException;
import akka.actor.Status;
import com.zanshang.framework.BaseUntypedActor;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/4/15.
 */
public class EmailCodeGetActor extends BaseUntypedActor {

    public EmailCodeGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Parameter params = (Parameter) o;
        Cache verificationCache = getCacheManager().getCache(Utils.cacheName(params.getTemplateName()));
        String code = verificationCache.get(params.getEmail(), String.class);
        if (code == null) {
            getSender().tell(new Status.Failure(new RuntimeException()), getSelf());
        } else {
            getSender().tell(code, getSelf());
        }
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
