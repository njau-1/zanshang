package com.zanshang.services.payment;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Payment;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 9/18/15.
 */
public class PaymentGetActor extends BaseUntypedActor {

    public PaymentGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getSender().tell(getMongoTemplate().findById(o.toString(), Payment.class), getSelf());
    }
}
