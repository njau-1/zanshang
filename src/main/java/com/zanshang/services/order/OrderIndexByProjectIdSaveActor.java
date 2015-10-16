package com.zanshang.services.order;

import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Order;
import com.zanshang.models.Reward;
import com.zanshang.models.index.OrderIndexByProjectId;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 7/3/15.
 */
public class OrderIndexByProjectIdSaveActor extends AddToIndexActor<ObjectId, Order> {

    public OrderIndexByProjectIdSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Order extractValue(Object o) {
        return (Order) o;
    }

    @Override
    public ObjectId extractKey(Object o) {
        Order order = (Order) o;
        ObjectId rewardId = order.getRewardId();
        Reward reward = getMongoTemplate().findById(rewardId, Reward.class);
        return reward.getProjectId();
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, Order>> indexClz() {
        return OrderIndexByProjectId.class;
    }
}
