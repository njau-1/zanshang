package com.zanshang.services.bid;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Bid;
import com.zanshang.services.BidTrapdoor;
import com.zanshang.framework.RepositoryTrapdoor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Await;

import java.util.List;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/19/15.
 */
public class BidTrapdoorImpl extends RepositoryTrapdoor<ObjectId, Bid> implements BidTrapdoor {

    ActorRef findByProjectIdActor;

    ActorRef findByUidActor;

    public BidTrapdoorImpl(ApplicationContext springContext) {
        super(springContext);
        findByProjectIdActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(BidFindByProjectIdActor.class,
                springContext)));
        findByUidActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(BidFindByUidActor.class, springContext)));
    }

    @Override
    public Class<? extends BaseUntypedActor> getClz() {
        return BidGetActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> saveClz() {
        return BidSaveActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> deleteClz() {
        return null;
    }

    @Override
    public List<Bid> findByProjectId(ObjectId projectId) {
        try {
            Object result = Await.result(ask(findByProjectIdActor, projectId, ActorConstant.DEFAULT_TIMEOUT),
                    ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (List<Bid>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " FindByProjectId error.", e);
            throw new RuntimeException(e);
        }
    }

//    @Override
    public List<Bid> findByUid(ObjectId uid) {
        try {
            Object result = Await.result(ask(findByUidActor, uid, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return (List<Bid>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " FindByUid error.", e);
            throw new RuntimeException(e);
        }
    }
}
