package com.zanshang.services;

import com.zanshang.models.Company;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Lookis on 6/4/15.
 */
public interface CompanyTrapdoor {

    Map<ObjectId, Company> findById(Collection<ObjectId> ids);

    Company get(ObjectId uid);

    void save(Company company);

}
