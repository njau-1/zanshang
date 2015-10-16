package com.zanshang.services;

import com.zanshang.models.Address;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Lookis on 6/24/15.
 */
public interface AddressTrapdoor {

    List<Address> findByUid(ObjectId uid);

    Address get(ObjectId id);

    void save(Address address, boolean isDefault);

    void delete(ObjectId addressId);
}
