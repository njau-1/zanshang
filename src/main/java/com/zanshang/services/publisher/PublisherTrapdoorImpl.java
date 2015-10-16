package com.zanshang.services.publisher;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.TypedActor;
import com.zanshang.constants.ActorConstant;
import com.zanshang.services.PublisherTrapdoor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Await;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/10/15.
 */
public class PublisherTrapdoorImpl implements PublisherTrapdoor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    ActorContext ctx = TypedActor.context();

    ActorRef publisherVerifiedActor;

    ActorRef publisherSubmitActor;

    public PublisherTrapdoorImpl(ApplicationContext springContext) {
        publisherVerifiedActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(PublisherVerifiedActor.class, springContext)), getClass().getSimpleName() + "Verified");
        publisherSubmitActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(PublisherSubmitActor.class, springContext)), getClass().getSimpleName() + "Submit");
    }

    @Override
    public boolean isVerified(ObjectId uid) {
        try {
            Object result = Await.result(ask(publisherVerifiedActor, uid, ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return Boolean.valueOf(result.toString());
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " Get Verified Publisher error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void submit(ObjectId uid) {
        publisherSubmitActor.tell(uid, ctx.self());
    }
}
