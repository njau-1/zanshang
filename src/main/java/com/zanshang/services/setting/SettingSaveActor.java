package com.zanshang.services.setting;

import com.zanshang.framework.BaseUntypedActor;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/4/15.
 */
public class SettingSaveActor extends BaseUntypedActor{

    public SettingSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getMongoTemplate().save(o);
    }
}
