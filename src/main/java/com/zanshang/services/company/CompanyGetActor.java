package com.zanshang.services.company;

import akka.actor.Status;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Company;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/4/15.
 */
public class CompanyGetActor extends BaseUntypedActor {

    public CompanyGetActor(ApplicationContext ctx) {
        super(ctx);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Company company = getMongoTemplate().findById(o.toString(), Company.class);
        getSender().tell(company == null ? new Status.Success(null) : company, getSelf());
    }
}
