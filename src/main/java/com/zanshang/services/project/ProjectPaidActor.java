package com.zanshang.services.project;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.Price;
import com.zanshang.framework.PriceUnit;
import com.zanshang.framework.PriceUtils;
import com.zanshang.models.Order;
import com.zanshang.models.Project;
import com.zanshang.models.Reward;
import com.zanshang.services.order.OrderFindByProjectIdAllActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import scala.concurrent.Future;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Lookis on 6/21/15.
 */
public class ProjectPaidActor extends BaseUntypedActor {

    ActorRef orderActor;

    ActorRef projectGetActor;

    public ProjectPaidActor(ApplicationContext spring) {
        super(spring);
        orderActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(OrderFindByProjectIdAllActor.class, spring)));
        projectGetActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(ProjectGetActor.class, spring)));
    }

    @Override
    public void onReceive(Object o) throws Exception {
        ObjectId projectId = new ObjectId(o.toString());
        Future<Object> future = Patterns.ask(orderActor, new ObjectId(o.toString()), ActorConstant.DEFAULT_TIMEOUT);
        ActorRef sender = getSender();
        ActorRef self = getSelf();
        future.onComplete(new OnComplete<Object>() {
            @Override
            public void onComplete(Throwable throwable, Object o) throws Throwable {
                if (o != null) {
                    List<Order> orders = (List<Order>) o;
                    Future<Object> projectFuture = Patterns.ask(projectGetActor, projectId, ActorConstant
                            .DEFAULT_TIMEOUT);
                    projectFuture.onComplete(new OnComplete<Object>() {
                        @Override
                        public void onComplete(Throwable throwable, Object o) throws Throwable {
                            if (o != null) {
                                Project project = (Project) o;
                                Collection<ObjectId> rewardIds = project.getRewards();
                                List<Reward> rewards = getMongoTemplate().find(Query.query(Criteria.where("_id").in
                                        (rewardIds)), Reward.class);
                                Map<ObjectId, Reward> rewardMap = new HashMap<>();
                                for (Reward reward : rewards) {
                                    rewardMap.put(reward.getId(), reward);
                                }
                                long paid = orders.stream().collect(Collectors.summingLong(order -> {
                                    if (order.isPaid()) {
                                        Reward reward = rewardMap.get(order.getRewardId());
                                        Price price = reward.getPrice();
                                        Price sum = PriceUtils.multi(price, order.getCount());
                                        long cent = price.getUnit().toCent(sum.getPrice());
                                        return cent;
                                    } else {
                                        return 0;
                                    }
                                }));
                                sender.tell(new Price(paid, PriceUnit.CENT), self);
                            } else {
                                logger.error("Ops.", throwable);
                            }
                        }
                    }, getContext().dispatcher());
                } else {
                    logger.error("Ops.", throwable);
                }
            }
        }, getContext().dispatcher());
    }
}
