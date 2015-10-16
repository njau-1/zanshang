package com.zanshang.services.salon;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Salon;
import com.zanshang.services.author.AuthorVerifiedActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.util.*;

/**
 * Created by Lookis on 6/28/15.
 */
public class SalonFindByIdsActor extends BaseUntypedActor {

    ActorRef authorVerified;

    public SalonFindByIdsActor(ApplicationContext spring) {
        super(spring);
        authorVerified = getContext().actorOf(Props.create(AkkaTrapdoor.creator(AuthorVerifiedActor.class, spring)));
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Collection<ObjectId> ids = (Collection<ObjectId>) o;
        List<Salon> salons = getMongoTemplate().find(Query.query(Criteria.where("_id").in(ids)), Salon.class);
        Map<ObjectId, Salon> salonMap = new HashMap<>();
        List<Future<Object>> futures = new ArrayList<>();
        salons.forEach(salon -> salonMap.put(salon.getUid(), salon));

        for (ObjectId id : ids) {
            if (!salonMap.containsKey(id)) {
                Future<Object> authorFuture = Patterns.ask(authorVerified, id, ActorConstant.DEFAULT_TIMEOUT);
                futures.add(authorFuture.andThen(new OnComplete<Object>() {
                    @Override
                    public void onComplete(Throwable throwable, Object o) throws Throwable {
                        if (o != null && Boolean.valueOf(o.toString())) {
                            logger.warn("Found author without salon:" + id + " fixed.");
                            Salon salon = new Salon(id);
                            getMongoTemplate().save(salon);
                            salonMap.put(salon.getUid(), salon);
                        }
                    }
                }, getContext().dispatcher()));
            }
        }
        Await.result(Futures.sequence(futures, getContext().dispatcher()), ActorConstant
                .DEFAULT_TIMEOUT_DURATION);
        getSender().tell(salonMap, getSelf());
    }
}
