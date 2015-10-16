package com.zanshang.services.qrcode;

import com.zanshang.framework.BaseUntypedActor;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/4/15.
 */
public class QRCodeSaveActor extends BaseUntypedActor {

    public QRCodeSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getMongoTemplate().save(o);
        getSender().tell(true, getSelf());
    }
}
