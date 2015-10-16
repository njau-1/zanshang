package com.zanshang.services.qrcode;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.QRCode;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/4/15.
 */
public class QRCodeGetActor extends BaseUntypedActor {

    public QRCodeGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        QRCode byId = getMongoTemplate().findById(o.toString(), QRCode.class);
        getSender().tell(byId, getSelf());
    }
}
