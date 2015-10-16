package com.zanshang.services.order;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Order;
import com.zanshang.models.index.OrderIndexByProjectId;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lookis on 7/7/15.
 */
public class OrderFindByProjectIdsAllActor extends BaseUntypedActor {

    public OrderFindByProjectIdsAllActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Collection<ObjectId> ids = (Collection<ObjectId>) o;
        List<OrderIndexByProjectId> orderIndexes = getMongoTemplate().find(Query.query(Criteria.where("_id").in(ids)),
                OrderIndexByProjectId.class);
        Map<ObjectId, List<Order>> retMap = new HashMap<>();
        orderIndexes.forEach(orderIndexByProjectId -> retMap.put(orderIndexByProjectId.getKey(), orderIndexByProjectId.getValue()));
        getSender().tell(retMap, getSelf());
    }
}
