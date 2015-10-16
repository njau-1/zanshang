package com.zanshang.services.company;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Company;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lookis on 7/2/15.
 */
public class CompanyFindByIdsActor extends BaseUntypedActor {

    public CompanyFindByIdsActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Collection<ObjectId> ids = (Collection<ObjectId>) o;
        List<Company> id = getMongoTemplate().find(Query.query(Criteria.where("_id").in(ids)), Company.class);
        Map<ObjectId, Company> companyMap = new HashMap<>();
        id.forEach(company -> companyMap.put(company.getUid(), company));
        getSender().tell(companyMap, getSelf());

    }
}
