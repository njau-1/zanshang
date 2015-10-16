package com.zanshang.services.publisher;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Publisher;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/10/15.
 */
public class PublisherVerifiedActor extends BaseUntypedActor {

    public PublisherVerifiedActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Publisher indexByUid = getMongoTemplate().findById(o.toString(), Publisher.class);
        getSender().tell(indexByUid != null, getSelf());
    }
}
