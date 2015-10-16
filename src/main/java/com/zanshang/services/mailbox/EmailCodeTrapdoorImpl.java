package com.zanshang.services.mailbox;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.TypedActor;
import com.zanshang.constants.ActorConstant;
import com.zanshang.services.EmailCodeTrapdoor;
import com.zanshang.utils.AkkaTrapdoor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Await;

import java.util.Map;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/23/15.
 */
public class EmailCodeTrapdoorImpl implements EmailCodeTrapdoor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ActorContext ctx = TypedActor.context();

    ActorRef createActor;

    ActorRef deleteActor;

    ActorRef getActor;

    public EmailCodeTrapdoorImpl(ApplicationContext springContext) {
        createActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(EmailCodeCreateActor.class, springContext)));
        deleteActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(EmailCodeDeleteActor.class, springContext)));
        getActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(EmailCodeGetActor.class, springContext)));
    }

    @Override
    public void create(String email, String templateName, String subject, Map<String, String> model) {
        createActor.tell(new EmailCodeCreateActor.Parameters(templateName, email, subject, model), ActorRef.noSender());
    }

    @Override
    public void create(String email, String templateName, Map<String, String> model) {
        createActor.tell(new EmailCodeCreateActor.Parameters(templateName, email, model), ActorRef.noSender());
    }

    @Override
    public String get(String email, String templateName) {
        try {
            Object result = Await.result(ask(getActor, new EmailCodeGetActor.Parameter(templateName, email),
                    ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (String) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " Get error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String email, String templateName) {
        deleteActor.tell(new EmailCodeDeleteActor.Parameter(templateName, email), ActorRef.noSender());
    }
}
