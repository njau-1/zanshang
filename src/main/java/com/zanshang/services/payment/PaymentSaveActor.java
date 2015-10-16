package com.zanshang.services.payment;

import com.zanshang.framework.BaseUntypedActor;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 9/18/15.
 */
public class PaymentSaveActor extends BaseUntypedActor {

    public PaymentSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getMongoTemplate().save(o);
        getSender().tell(true, getSelf());
    }
}
