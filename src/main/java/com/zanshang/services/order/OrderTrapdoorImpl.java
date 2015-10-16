package com.zanshang.services.order;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.TypedActor;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.index.IndexPageableParams;
import com.zanshang.models.Order;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import scala.concurrent.Await;

import java.util.List;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/14/15.
 */
public class OrderTrapdoorImpl implements com.zanshang.services.OrderTrapdoor {

    private ActorRef byUidActor;

    private ActorRef byProjectIdActor;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ActorContext ctx = TypedActor.context();

    protected ActorRef get;

    protected ActorRef save;

    protected ActorRef findPaidByUidActor;

    public OrderTrapdoorImpl(ApplicationContext springContext) {
        byUidActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(OrderFindByUidActor.class, springContext)));
        byProjectIdActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(OrderFindByProjectIdAllActor.class,
                springContext)));
        get = ctx.actorOf(Props.create(AkkaTrapdoor.creator(OrderGetActor.class, springContext)));
        save = ctx.actorOf(Props.create(AkkaTrapdoor.creator(OrderSaveActor.class, springContext)));
        findPaidByUidActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(OrderFindPaidByUidActor.class,
                springContext)));
    }

    @Override
    public Page<Order> findByUid(ObjectId uid, Pageable pageable) {
        try {
            Object result = Await.result(ask(byUidActor, new IndexPageableParams(pageable, uid), ActorConstant
                    .DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Page<Order>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " findByUid error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Order> findPaidByUid(ObjectId uid, Pageable pageable) {
        try {
            Object result = Await.result(ask(findPaidByUidActor, new IndexPageableParams(pageable, uid),
                    ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Page<Order>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " findPaidByUid error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findByProjectId(ObjectId projectId) {
        try {
            Object result = Await.result(ask(byProjectIdActor, projectId, ActorConstant.DEFAULT_TIMEOUT),
                    ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (List<Order>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " findByProjectId error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean save(Order toSave) {
        try {
            return (boolean) Await.result(ask(save, toSave, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
        } catch (Exception e) {
            logger.warn("Ignore the save wait exception.", e);
            return false;
        }
    }

    public Order get(ObjectId key) {
        try {
            Object result = Await.result(ask(get, key, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return (Order) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " Get error.", e);
            throw new RuntimeException(e);
        }
    }
}
