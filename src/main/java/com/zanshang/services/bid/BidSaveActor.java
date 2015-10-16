package com.zanshang.services.bid;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Bid;
import com.zanshang.utils.AkkaTrapdoor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Future;

import java.util.List;

/**
 * Created by Lookis on 6/19/15.
 */
public class BidSaveActor extends BaseUntypedActor {

    ActorRef getByProjectId;

    ActorRef indexByProjectIdSave;
    ActorRef indexByUidSave;

    public BidSaveActor(ApplicationContext spring) {
        super(spring);
        getByProjectId = getContext().actorOf(Props.create(AkkaTrapdoor.creator(BidFindByProjectIdActor.class, spring)));
        indexByProjectIdSave = getContext().actorOf(Props.create(AkkaTrapdoor.creator(BidIndexByProjectIdSaveActor.class, spring)));
        indexByUidSave = getContext().actorOf(Props.create(AkkaTrapdoor.creator(BidIndexByUidSaveActor.class, spring)));
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Bid bid = (Bid) o;
        Future<Object> byProjectIdFuture = Patterns.ask(getByProjectId, bid.getProjectId(), ActorConstant.DEFAULT_TIMEOUT);
        byProjectIdFuture.onSuccess(new OnSuccess<Object>(){
            @Override
            public void onSuccess(Object o) throws Throwable {
                List<Bid> list = (List<Bid>) o;
                if(CollectionUtils.isNotEmpty(list)){
                    for (Bid b : list) {
                        if (b.getUid().equals(bid.getUid())){
                            bid.setId(b.getId());
                        }
                    }
                }
                getMongoTemplate().save(bid);
                //index1
                indexByUidSave.tell(bid, getSelf());
                //index2
                indexByProjectIdSave.tell(bid, getSelf());
            }
        }, getContext().dispatcher());

    }
}
