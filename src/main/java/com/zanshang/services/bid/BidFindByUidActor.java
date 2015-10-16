package com.zanshang.services.bid;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindAllByIndexActor;
import com.zanshang.models.Bid;
import com.zanshang.models.index.BidIndexByUid;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/19/15.
 */
public class BidFindByUidActor extends FindAllByIndexActor<ObjectId, Bid> {


    public BidFindByUidActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, Bid>> indexClz() {
        return BidIndexByUid.class;
    }
}
