package com.zanshang.services.order;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindAllByIndexActor;
import com.zanshang.models.Order;
import com.zanshang.models.index.OrderIndexByProjectId;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/14/15.
 */
public class OrderFindByProjectIdAllActor extends FindAllByIndexActor<ObjectId, Order> {

    public OrderFindByProjectIdAllActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, Order>> indexClz() {
        return OrderIndexByProjectId.class;
    }
}
