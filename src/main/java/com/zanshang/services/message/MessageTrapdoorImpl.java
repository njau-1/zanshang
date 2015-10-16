package com.zanshang.services.message;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.index.IndexPageableParams;
import com.zanshang.models.Message;
import com.zanshang.framework.RepositoryTrapdoor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import scala.concurrent.Await;

/**
 * Created by Lookis on 6/22/15.
 */
public class MessageTrapdoorImpl extends RepositoryTrapdoor<ObjectId, Message> implements com.zanshang.services
        .MessageTrapdoor {

    ActorRef findBySenderAndRecipientActor;

    public MessageTrapdoorImpl(ApplicationContext springContext) {
        super(springContext);
        findBySenderAndRecipientActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(MessageFindByCompositeIdActor
                .class, springContext)));
    }

    @Override
    public Page<Message> findBySenderAndRecipient(ObjectId sender, ObjectId recipient, Pageable pageable) {
        try {
            Object result = Await.result(Patterns.ask(findBySenderAndRecipientActor, new IndexPageableParams
                    (pageable, Utils.extractCompositeId(sender, recipient)), ActorConstant.DEFAULT_TIMEOUT),
                    ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Page<Message>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " FindBySenderAndRecipient error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<? extends BaseUntypedActor> getClz() {
        return null;
    }

    @Override
    public Class<? extends BaseUntypedActor> saveClz() {
        return MessageCreateActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> deleteClz() {
        return null;
    }
}
