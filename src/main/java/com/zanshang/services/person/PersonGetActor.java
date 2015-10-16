package com.zanshang.services.person;

import akka.actor.Status;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Person;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/4/15.
 */
public class PersonGetActor extends BaseUntypedActor {

    public PersonGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Person byId = getMongoTemplate().findById(o.toString(), Person.class);
        if(byId != null){
            getSender().tell(byId, getSelf());
        }else{
            getSender().tell(new Status.Success(null), getSelf());
        }

    }
}
