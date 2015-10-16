package com.zanshang.services.address;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Address;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/28/15.
 */
public class AddressGetActor extends BaseUntypedActor {

    public AddressGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getSender().tell(getMongoTemplate().findById(o.toString(), Address.class), getSelf());
    }
}
