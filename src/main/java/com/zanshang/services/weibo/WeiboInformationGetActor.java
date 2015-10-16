package com.zanshang.services.weibo;

import akka.actor.Status;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.WeiboInformation;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/7/15.
 */
public class WeiboInformationGetActor extends BaseUntypedActor {

    public WeiboInformationGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        WeiboInformation weiboInformation = getMongoTemplate().findById(o.toString(), WeiboInformation.class);
        getSender().tell(weiboInformation == null ? new Status.Success(null) : weiboInformation, getSelf());
    }
}
