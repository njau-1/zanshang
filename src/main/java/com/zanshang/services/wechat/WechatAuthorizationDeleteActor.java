package com.zanshang.services.wechat;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Map;

/**
 * Created by Lookis on 6/11/15.
 */
public class WechatAuthorizationDeleteActor extends BaseUntypedActor {

    private ActorRef informationDeleteActor;

    public WechatAuthorizationDeleteActor(ApplicationContext spring) {
        super(spring);
        informationDeleteActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(WechatInformationDeleteActor
                .class, spring)));
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o != null) {
            informationDeleteActor.tell(o, getSelf());
            getMongoTemplate().findAndRemove(Query.query(Criteria.where("_id").is(o.toString())), Map.class,
                    WechatAuthorizationTrapdoorImpl.COLLECTION_NAME);
        }
    }
}
