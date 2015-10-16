package com.zanshang.services.bid;

import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Bid;
import com.zanshang.models.index.BidIndexByProjectId;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 7/3/15.
 */
public class BidIndexByProjectIdSaveActor extends AddToIndexActor<ObjectId, Bid> {

    public BidIndexByProjectIdSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Bid extractValue(Object o) {
        return (Bid) o;
    }

    @Override
    public ObjectId extractKey(Object o) {
        Bid bid = (Bid) o;
        return bid.getProjectId();
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, Bid>> indexClz() {
        return BidIndexByProjectId.class;
    }
}
