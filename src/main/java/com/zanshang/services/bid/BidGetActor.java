package com.zanshang.services.bid;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Bid;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/19/15.
 */
public class BidGetActor extends BaseUntypedActor {

    public BidGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getSender().tell(getMongoTemplate().findById(o.toString(), Bid.class), getSelf());
    }
}
