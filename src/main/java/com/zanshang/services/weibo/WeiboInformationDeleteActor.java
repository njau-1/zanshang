package com.zanshang.services.weibo;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.WeiboInformation;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by Lookis on 6/11/15.
 */
public class WeiboInformationDeleteActor extends BaseUntypedActor {

    public WeiboInformationDeleteActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getMongoTemplate().findAndRemove(Query.query(Criteria.where("_id").is(o.toString())), WeiboInformation.class);
    }
}
