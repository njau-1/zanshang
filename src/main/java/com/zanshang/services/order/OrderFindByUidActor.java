package com.zanshang.services.order;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindByIndexPageableActor;
import com.zanshang.models.Order;
import com.zanshang.models.index.OrderIndexByUid;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/14/15.
 */
public class OrderFindByUidActor extends FindByIndexPageableActor<ObjectId, Order> {

    public OrderFindByUidActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, Order>> indexClz() {
        return OrderIndexByUid.class;
    }
    
    @Override
    protected boolean reverse(){
        return true;
    }
}
