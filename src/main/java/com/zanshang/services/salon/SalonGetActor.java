package com.zanshang.services.salon;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Salon;
import com.zanshang.services.author.AuthorVerifiedActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Future;

/**
 * Created by Lookis on 6/25/15.
 */
public class SalonGetActor extends BaseUntypedActor {

    ActorRef authorVerified;

    ActorRef salonCreateActor;

    public SalonGetActor(ApplicationContext spring) {
        super(spring);
        authorVerified = getContext().actorOf(Props.create(AkkaTrapdoor.creator(AuthorVerifiedActor.class, spring)));
        salonCreateActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(SalonCreateActor.class, spring)));
    }

    @Override
    public void onReceive(Object o) throws Exception {
        ObjectId uid = new ObjectId(o.toString());
        Salon salon = getMongoTemplate().findById(uid, Salon.class);
        ActorRef sender = getSender();
        ActorRef self = getSelf();
        if(salon == null){
            Future<Object> authorFuture = Patterns.ask(authorVerified, uid, ActorConstant.DEFAULT_TIMEOUT);
            authorFuture.onSuccess(new OnSuccess<Object>(){
                @Override
                public void onSuccess(Object o) throws Throwable {
                    if(Boolean.valueOf(o.toString())){
                        logger.warn("Found author without salon:" + uid + " fixed.");
                        salonCreateActor.tell(new Salon(uid), getSelf());
                        sender.tell(salon, self);
                    }else{
                        sender.tell(null, self);
                    }
                }
            }, getContext().dispatcher());
        }else{
            sender.tell(salon, self);
        }
    }
}
