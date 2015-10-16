package com.zanshang.notify.config;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.Creator;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.notify.service.*;
import com.zanshang.utils.AkkaTrapdoor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuming on 15/9/7.
 */
@Configuration
public class NotifyConfig {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ActorSystem actorSystem;

    public ActorRef getNotifyService(NotifyBusinessType t, ApplicationContext applicationContext) {
        switch (t) {
            case DEMO: return build(applicationContext, ConsoleService.class);
            case PROJECT_CREATE_SUCCESS: return build(applicationContext, NotificationService.class);
            case PROJECT_FUNDING_SUCCESS: return builds(applicationContext, EmailService.class);
            case PERSONAL_NEW_CHAT: return build(applicationContext, NotificationService.class);
            case PROJECT_PROGRESS_FIRST_WEEK: return build(applicationContext, EmailService.class);
            case PROJECT_PROGRESS_HALF: return build(applicationContext, EmailService.class);
            case PROJECT_PROGRESS_LAST_WEEK: return build(applicationContext, EmailService.class);
            case SALON_NEW_CHAT_AUTHOR: return build(applicationContext, NotificationService.class);
            case SALON_NEW_CHAT_MEMBER: return build(applicationContext, NotificationService.class);
            case WIT: return build(applicationContext, EmailService.class);
            case SALON_NEW_TOPIC: return builds(applicationContext, EmailService.class, NotificationService.class);
            case PHONE_NOLOGIN_REGIST: return build(applicationContext, SMSService.class);
//            case COMPOSITE: return builds(applicationContext, ConsoleService.class, NotificationService.class);
            default: return null;
        }
    }

    private ActorRef build(ApplicationContext spring, Class clazz) {
        return actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(clazz, spring)));
    }

    private ActorRef builds(ApplicationContext spring, Class... args) {
        try {
            List<ActorRef> actorRefs = new ArrayList<>();
            for (Class clazz: args) {
                actorRefs.add(actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(clazz, spring))));
            }
            return actorSystem.actorOf(Props.create(new CompositeCreator(CompositeService.class, spring, actorRefs)));
        } catch (Exception e) {
            logger.debug("get Composite Notification Service Exception...", e);
        }
        return null;
    }


    public static class CompositeCreator implements Creator<Actor> {

        private Class<? extends BaseUntypedActor> clz;
        private ApplicationContext context;
        private List<ActorRef> actorRefs;

        public CompositeCreator(Class<? extends BaseUntypedActor> clz, ApplicationContext context, List<ActorRef> actorRefs) {
            this.clz = clz;
            this.context = context;
            this.actorRefs = actorRefs;
        }

        @Override
        public Actor create() throws Exception {
            return clz.getConstructor(ApplicationContext.class, List.class).newInstance(context, actorRefs);
        }
    }
}
