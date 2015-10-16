package com.zanshang.services.order;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Order;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/14/15.
 */
public class OrderGetActor extends BaseUntypedActor {

    public OrderGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Order byId = getMongoTemplate().findById(o.toString(), Order.class);
        getSender().tell(byId, getSelf());
    }
}
