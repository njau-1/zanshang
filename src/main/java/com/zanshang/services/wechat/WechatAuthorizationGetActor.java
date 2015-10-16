package com.zanshang.services.wechat;

import com.zanshang.framework.BaseUntypedActor;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Created by Lookis on 6/7/15.
 */
public class WechatAuthorizationGetActor extends BaseUntypedActor {

    public WechatAuthorizationGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getSender().tell(getMongoTemplate().findById(o.toString(), Map.class, WechatAuthorizationTrapdoorImpl.COLLECTION_NAME), getSelf());
    }
}
