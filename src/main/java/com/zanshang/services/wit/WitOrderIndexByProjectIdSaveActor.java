package com.zanshang.services.wit;

import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.wit.WitOrder;
import com.zanshang.models.wit.WitReward;
import com.zanshang.models.wit.index.WitOrderIndexByProjectId;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by xuming on 15/9/17.
 */
public class WitOrderIndexByProjectIdSaveActor extends AddToIndexActor<ObjectId, WitOrder> {

    public WitOrderIndexByProjectIdSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public WitOrder extractValue(Object o) {
        return (WitOrder) o;
    }

    @Override
    public ObjectId extractKey(Object o) {
        WitOrder witOrder = extractValue(o);
        WitReward witReward = getMongoTemplate().findById(witOrder.getRewardId(), WitReward.class);
        return witReward.getProjectId();
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, WitOrder>> indexClz() {
        return WitOrderIndexByProjectId.class;
    }
}
