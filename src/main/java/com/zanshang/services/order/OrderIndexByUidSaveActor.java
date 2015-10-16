package com.zanshang.services.order;

import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Order;
import com.zanshang.models.index.OrderIndexByUid;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 7/3/15.
 */
public class OrderIndexByUidSaveActor extends AddToIndexActor<ObjectId, Order> {

    public OrderIndexByUidSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Order extractValue(Object o) {
        return (Order) o;
    }

    @Override
    public ObjectId extractKey(Object o) {
        Order order = (Order) o;
        return order.getUid();
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, Order>> indexClz() {
        return OrderIndexByUid.class;
    }
}
