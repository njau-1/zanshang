package com.zanshang.services.qrcode;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.QRCode;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by Lookis on 6/4/15.
 */
public class QRCodeDeleteActor extends BaseUntypedActor {


    public QRCodeDeleteActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getMongoTemplate().findAndRemove(Query.query(Criteria.where("_id").is(o.toString())), QRCode.class);
    }
}
