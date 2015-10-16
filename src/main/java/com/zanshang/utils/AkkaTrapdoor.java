package com.zanshang.utils;

import akka.actor.*;
import akka.japi.Creator;
import com.zanshang.framework.BaseUntypedActor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Created by Lookis on 6/4/15.
 */
@Service
public class AkkaTrapdoor implements ApplicationContextAware {

    private ApplicationContext springContext;

    @Autowired
    private ActorSystem actorSystem;

    public static Creator<Actor> creator(Class<? extends BaseUntypedActor> clz, ApplicationContext springContext) {
        return new MyCreator(clz, springContext);
    }

    public static ActorRef create(ActorRefFactory context, Class<? extends BaseUntypedActor> actorClass,
                                  ApplicationContext springContext) {
        return context.actorOf(Props.create(creator(actorClass, springContext)));
    }

    public <T> T createTrapdoor(Class<T> ifaceClz, Class<? extends T> implementClz) {
        return TypedActor.get(actorSystem).typedActorOf(new TypedProps<>(ifaceClz, () -> {
            return implementClz.getConstructor(ApplicationContext.class).newInstance(springContext);
        }));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.springContext = applicationContext;
    }

    public static class MyCreator implements Creator<Actor>{

        private Class<? extends BaseUntypedActor> clz;
        private ApplicationContext context;

        public MyCreator(Class<? extends BaseUntypedActor> clz, ApplicationContext context) {
            this.clz = clz;
            this.context = context;
        }

        @Override
        public Actor create() throws Exception {
            return clz.getConstructor(ApplicationContext.class).newInstance(context);
        }
    }
}
