package com.zanshang.services.author;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.TypedActor;
import com.zanshang.constants.ActorConstant;
import com.zanshang.services.AuthorTrapdoor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Await;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/4/15.
 */
public class AuthorTrapdoorImpl implements AuthorTrapdoor{

    protected Logger logger = LoggerFactory.getLogger(getClass());

    ActorRef authorVerifiedActor;

    ActorRef authorFilledActor;

    public AuthorTrapdoorImpl(ApplicationContext springContext) {
        ActorContext ctx = TypedActor.context();
        authorVerifiedActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator
                (AuthorVerifiedActor.class, springContext)), getClass().getSimpleName() + "Verified");
        authorFilledActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator
                (AuthorFilledActor.class, springContext)), getClass().getSimpleName() + "Filled");
    }

    @Override
    public boolean isVerified(ObjectId uid) {
        try {
            Object result = Await.result(ask(authorVerifiedActor, uid, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return Boolean.valueOf(result.toString());
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " Get Verified Author Information error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isFilled(ObjectId uid){
        try {
            Object result = Await.result(ask(authorFilledActor, uid, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return Boolean.valueOf(result.toString());
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " Get Filled Author Information error.", e);
            throw new RuntimeException(e);
        }
    }

}
