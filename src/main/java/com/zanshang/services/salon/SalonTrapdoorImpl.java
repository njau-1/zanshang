package com.zanshang.services.salon;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.TypedActor;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.index.IndexPageableParams;
import com.zanshang.models.Project;
import com.zanshang.models.Salon;
import com.zanshang.models.SalonChat;
import com.zanshang.models.SalonTopic;
import com.zanshang.models.global.SalonHome;
import com.zanshang.services.salon.chat.SalonChatCreateActor;
import com.zanshang.services.salon.chat.SalonChatFindBySalonIdPageableActor;
import com.zanshang.services.salon.member.SalonAddMemberActor;
import com.zanshang.services.salon.member.SalonFindByMemberIdActor;
import com.zanshang.services.salon.topic.SalonTopicCreateActor;
import com.zanshang.services.salon.topic.SalonTopicFindBySalonIdPageableActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import scala.concurrent.Await;

import java.util.Collection;
import java.util.Map;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/25/15.
 */
public class SalonTrapdoorImpl implements com.zanshang.services.SalonTrapdoor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ActorContext ctx = TypedActor.context();

    protected ActorRef get;

    protected ActorRef addMember;

    protected ActorRef createTopic;

    protected ActorRef chat;

    protected ActorRef isMemberActor;

    protected ActorRef findByMemberId;

    protected ActorRef findByIds;

    protected ActorRef globalSalonPageable;

    protected ActorRef salonTopicFindPageable;

    protected ActorRef salonChatFindPageable;

    public SalonTrapdoorImpl(ApplicationContext spring) {
        get = ctx.actorOf(Props.create(AkkaTrapdoor.creator(SalonGetActor.class, spring)), getClass().getSimpleName()
                + "Get");

        addMember = ctx.actorOf(Props.create(AkkaTrapdoor.creator(SalonAddMemberActor.class, spring)), getClass()
                .getSimpleName() + "AddMember");
        createTopic = ctx.actorOf(Props.create(AkkaTrapdoor.creator(SalonTopicCreateActor.class, spring)), getClass()
                .getSimpleName() + "CreateTopic");
        chat = ctx.actorOf(Props.create(AkkaTrapdoor.creator(SalonChatCreateActor.class, spring)), getClass()
                .getSimpleName() + "Chat");

        isMemberActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(SalonIsMemberActor.class, spring)), getClass()
                .getSimpleName() + "isMember");
        findByMemberId = ctx.actorOf(Props.create(AkkaTrapdoor.creator(SalonFindByMemberIdActor.class, spring)),
                getClass().getSimpleName() + "findByMemberId");
        findByIds = ctx.actorOf(Props.create(AkkaTrapdoor.creator(SalonFindByIdsActor.class, spring)), getClass()
                .getSimpleName() + "findByIds");
        globalSalonPageable = ctx.actorOf(Props.create(AkkaTrapdoor.creator(SalonHomePageableActor.class, spring)),
                getClass().getSimpleName() + "salonHome");
        salonTopicFindPageable = ctx.actorOf(Props.create(AkkaTrapdoor.creator(SalonTopicFindBySalonIdPageableActor
                .class, spring)), getClass().getSimpleName() + "salonTopic");
        salonChatFindPageable = ctx.actorOf(Props.create(AkkaTrapdoor.creator(SalonChatFindBySalonIdPageableActor
                .class, spring)), getClass().getSimpleName() + "salonChat");
    }

    @Override
    public Page<Salon> findByMemberId(ObjectId uid, Pageable pageable) {
        try {
            Object result = Await.result(Patterns.ask(findByMemberId, new IndexPageableParams(pageable, uid),
                    ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Page<Salon>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " FindByMemberId error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<SalonTopic> getTopic(ObjectId salonId, Pageable pageable) {
        try {
            Object result = Await.result(Patterns.ask(salonTopicFindPageable, new IndexPageableParams(pageable,
                    salonId), ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Page<SalonTopic>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " FindTopicBySalonId error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<SalonChat> getChat(ObjectId salonId, Pageable pageable) {
        try {
            Object result = Await.result(Patterns.ask(salonChatFindPageable, new IndexPageableParams(pageable,
                    salonId), ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Page<SalonChat>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " FindChatBySalonId error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isMember(ObjectId authorId, ObjectId uid) {
        try {
            Object result = Await.result(Patterns.ask(isMemberActor, new SalonIsMemberActor.Parameters(authorId, uid)
                    , ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Boolean) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " isMember error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean createTopic(SalonTopic topic) {
        try {
            Await.ready(Patterns.ask(createTopic, topic, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return true;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " CreateTopic error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Project> globalSalon(Pageable pageable) {
        try {
            Object result = Await.result(Patterns.ask(globalSalonPageable, new IndexPageableParams(pageable,
                    SalonHome.GLOBAL_KEY), ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Page<Project>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " SalonHome error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chat(SalonChat salonChat) {
        chat.tell(salonChat, ActorRef.noSender());
    }

    @Override
    public Salon get(ObjectId authorId) {
        try {
            Object result = Await.result(ask(get, authorId, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return (Salon) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " Get error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<ObjectId, Salon> findByIds(Collection<ObjectId> salonIds) {
        try {
            Object result = Await.result(ask(findByIds, salonIds, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return (Map<ObjectId, Salon>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " Get error.", e);
            throw new RuntimeException(e);
        }
    }
}
