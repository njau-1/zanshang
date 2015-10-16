package com.zanshang.services.weibo;

import com.zanshang.framework.BaseUntypedActor;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Created by Lookis on 6/7/15.
 */
public class WeiboAuthorizationGetActor extends BaseUntypedActor {

    public WeiboAuthorizationGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getSender().tell(getMongoTemplate().findById(o.toString(), Map.class, WeiboAuthorizationTrapdoorImpl
                .COLLECTION_NAME), getSelf());
    }
}
