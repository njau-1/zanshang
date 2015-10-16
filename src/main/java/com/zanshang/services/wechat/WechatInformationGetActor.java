package com.zanshang.services.wechat;

import akka.actor.Status;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.WechatInformation;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/7/15.
 */
public class WechatInformationGetActor extends BaseUntypedActor {

    public WechatInformationGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        WechatInformation wechatInformation = getMongoTemplate().findById(o.toString(), WechatInformation.class);
        getSender().tell(wechatInformation == null ? new Status.Success(null): wechatInformation, getSelf());
    }
}
