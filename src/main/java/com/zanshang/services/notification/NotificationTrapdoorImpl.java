package com.zanshang.services.notification;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.TypedActor;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.index.IndexPageableParams;
import com.zanshang.models.Notification;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import scala.concurrent.Await;

import java.util.List;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/24/15.
 */
public class NotificationTrapdoorImpl implements com.zanshang.services.NotificationTrapdoor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ActorContext ctx = TypedActor.context();

    ActorRef saveActor;

    ActorRef markReadActor;

    ActorRef findByUidActor;

    public NotificationTrapdoorImpl(ApplicationContext spring) {
        saveActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(NotificationSaveActor.class, spring)));
        markReadActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(NotificationMarkReadActor.class, spring)));
        findByUidActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(NotificationFindByUidActor.class, spring)));
    }

    @Override
    public Page<Notification> findByUid(ObjectId uid, Pageable pageable) {
        try {
            Object result = Await.result(ask(findByUidActor, new IndexPageableParams(pageable, uid), ActorConstant
                    .DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Page<Notification>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " FindByUidActor error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Notification notification) {
        saveActor.tell(notification, ActorRef.noSender());
    }

    @Override
    public boolean hasNews(ObjectId uid) {
        try {
            Object result = Await.result(ask(findByUidActor, new IndexPageableParams(new PageRequest(0, 10), uid),
                    ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            List<Notification> notifications = ((Page<Notification>) result).getContent();
            for (Notification notification : notifications) {
                if(notification != null && !notification.isRead()){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " FindByUidActor error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void markRead(List<ObjectId> notificationIds) {
        markReadActor.tell(notificationIds, ActorRef.noSender());
    }
}
