package com.zanshang.services.salon;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Salon;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Future;

/**
 * Created by Lookis on 6/25/15.
 */
public class SalonIsMemberActor extends BaseUntypedActor {

    ActorRef getActor;

    public SalonIsMemberActor(ApplicationContext spring) {
        super(spring);
        getActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(SalonGetActor.class, spring)));
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Parameters params = (Parameters) o;
        Future<Object> getFuture = Patterns.ask(getActor, params.getAuthorId(), ActorConstant.DEFAULT_TIMEOUT);
        ActorRef sender = getSender();
        ActorRef self = getSelf();
        getFuture.onSuccess(new OnSuccess<Object>(){
            @Override
            public void onSuccess(Object o) throws Throwable {
                Salon salon = (Salon) o;
                sender.tell(salon.getMembers().contains(params.getUid()), self);
            }
        }, getContext().dispatcher());
    }

    public static class Parameters{
        private ObjectId authorId;
        private ObjectId uid;

        public Parameters(ObjectId authorId, ObjectId uid) {
            this.authorId = authorId;
            this.uid = uid;
        }

        public ObjectId getAuthorId() {

            return authorId;
        }

        public ObjectId getUid() {
            return uid;
        }
    }
}
