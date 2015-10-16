package com.zanshang.services.phone;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.TypedActor;
import com.zanshang.constants.ActorConstant;
import com.zanshang.services.PhoneCaptchaTrapdoor;
import com.zanshang.utils.AkkaTrapdoor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Await;

import java.util.HashMap;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/4/15.
 */
public class PhoneCaptchaTrapdoorImpl implements PhoneCaptchaTrapdoor {

    Logger logger = LoggerFactory.getLogger(getClass());

    ActorContext ctx = TypedActor.context();

    ActorRef get;

    ActorRef create;

    ActorRef delete;

    public PhoneCaptchaTrapdoorImpl(ApplicationContext springContext) {
        create = ctx.actorOf(Props.create(AkkaTrapdoor.creator(PhoneCaptchaSaveActor.class, springContext)), getClass()
                .getSimpleName() + "Save");
        get = ctx.actorOf(Props.create(AkkaTrapdoor.creator(PhoneCaptchaGetActor.class, springContext)), getClass().getSimpleName
                () + "Get");
        delete = ctx.actorOf(Props.create(AkkaTrapdoor.creator(PhoneCaptchaDeleteActor.class, springContext)), getClass().getSimpleName()
                + "Delete");
    }

    @Override
    public void create(String phone, String messageFormat) {
        HashMap<String, String> map = new HashMap<>();
        map.put(phone, messageFormat);
        create.tell(map.entrySet().iterator().next(), ActorRef.noSender());
    }

    @Override
    public String get(String phone) {
        try {
            Object result = Await.result(ask(get, phone, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return result.toString();
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " Get error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String phone) {
        ctx.self().tell(phone, delete);
    }
}
