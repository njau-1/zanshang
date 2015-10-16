package com.zanshang.services.bid;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindAllByIndexActor;
import com.zanshang.models.Bid;
import com.zanshang.models.index.BidIndexByProjectId;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/19/15.
 */
public class BidFindByProjectIdActor extends FindAllByIndexActor<ObjectId,Bid> {

    public BidFindByProjectIdActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, Bid>> indexClz() {
        return BidIndexByProjectId.class;
    }
}
