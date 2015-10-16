package com.zanshang.services.wechat;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.WechatInformation;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by Lookis on 6/11/15.
 */
public class WechatInformationDeleteActor extends BaseUntypedActor {

    public WechatInformationDeleteActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getMongoTemplate().findAndRemove(Query.query(Criteria.where("_id").is(o.toString())), WechatInformation.class);
    }
}
