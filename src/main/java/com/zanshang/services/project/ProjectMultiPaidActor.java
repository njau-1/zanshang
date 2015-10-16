package com.zanshang.services.project;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.Price;
import com.zanshang.framework.PriceUnit;
import com.zanshang.models.Order;
import com.zanshang.models.Reward;
import com.zanshang.services.order.OrderFindByProjectIdsAllActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import scala.concurrent.Future;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Lookis on 7/7/15.
 */
public class ProjectMultiPaidActor extends BaseUntypedActor {

    ActorRef orderFindAll;

    public ProjectMultiPaidActor(ApplicationContext spring) {
        super(spring);
        orderFindAll = getContext().actorOf(Props.create(AkkaTrapdoor.creator(OrderFindByProjectIdsAllActor.class,
                spring)));
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Collection<ObjectId> ids = (Collection<ObjectId>) o;
        Future<Object> ask = Patterns.ask(orderFindAll, ids, ActorConstant.DEFAULT_TIMEOUT);
        ActorRef sender = getSender();
        ActorRef self = getSelf();
        ask.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object o) throws Throwable {
                Map<ObjectId, List<Order>> orderMap = (Map<ObjectId, List<Order>>) o;
                Map<ObjectId, Price> paidMaps = new HashMap<ObjectId, Price>();
                orderMap.forEach((key, value) -> {
                    List<Order> orders = value;
                    List<ObjectId> rewardIds = new ArrayList<ObjectId>();
                    orders.forEach(order -> rewardIds.add(order.getRewardId()));
                    List<Reward> rewards = getMongoTemplate().find(Query.query(Criteria.where("_id").in(rewardIds)),
                            Reward.class);
                    Map<ObjectId, Reward> rewardMap = new HashMap<>();
                    for (Reward reward : rewards) {
                        rewardMap.put(reward.getId(), reward);
                    }
                    long paidInCent = orders.stream().collect(Collectors.summingLong((order -> {
                        if(order.isPaid()){
                            Reward reward = rewardMap.get(order.getRewardId());
                            Price price = reward.getPrice();
                            long cent = price.getUnit().toCent(price.getPrice());
                            return cent;
                        }else{
                            return 0;
                        }
                    })));
                    paidMaps.put(key, new Price(paidInCent, PriceUnit.CENT));
                });
                sender.tell(paidMaps, self);
            }
        }, getContext().dispatcher());
    }
}
