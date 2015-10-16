package com.zanshang.services.publisher;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Company;
import com.zanshang.models.audit.AuditPublisher;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/10/15.
 */
public class PublisherSubmitActor extends BaseUntypedActor {

    public PublisherSubmitActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Company company = getMongoTemplate().findById(o.toString(), Company.class);
        //只有组织号可以申请出版社
        if(company != null) {
            getMongoTemplate().save(new AuditPublisher(o));
        }
    }
}
