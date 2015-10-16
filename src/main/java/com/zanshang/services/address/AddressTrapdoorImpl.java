package com.zanshang.services.address;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.TypedActor;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.TrapdoorSupervisor;
import com.zanshang.models.Address;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Await;

import java.util.List;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/24/15.
 */
public class AddressTrapdoorImpl extends TrapdoorSupervisor implements com.zanshang.services.AddressTrapdoor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ActorContext ctx = TypedActor.context();

    ActorRef saveActor;

    ActorRef deleteActor;

    ActorRef listByUid;

    ActorRef getActor;

    public AddressTrapdoorImpl(ApplicationContext spring) {
        getActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(AddressGetActor.class,spring)));
        saveActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(AddressSaveActor.class, spring)));
        listByUid = ctx.actorOf(Props.create(AkkaTrapdoor.creator(AddressListActor.class, spring)));
        deleteActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(AddressDeleteActor.class, spring)));
    }

    @Override
    public List<Address> findByUid(ObjectId uid){
        try {
            Object result = Await.result(ask(listByUid, uid, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return (List<Address>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " List error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Address address, boolean isDefault){
        saveActor.tell(new Object[]{address, isDefault}, ActorRef.noSender());
    }

    @Override
    public void delete(ObjectId addressId) {
        deleteActor.tell(addressId, ActorRef.noSender());
    }

    @Override
    public Address get(ObjectId id){
        try{
            Object result = Await.result(ask(getActor, id, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return (Address) result;
        }catch (Exception e ){
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " Get error.", e);
            throw new RuntimeException(e);
        }
    }
}
