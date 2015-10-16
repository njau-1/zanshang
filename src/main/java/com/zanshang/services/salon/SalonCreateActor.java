package com.zanshang.services.salon;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Salon;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 7/2/15.
 */
public class SalonCreateActor extends BaseUntypedActor {

    public SalonCreateActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Salon salon = (Salon) o;
        getMongoTemplate().save(salon);
    }
}
