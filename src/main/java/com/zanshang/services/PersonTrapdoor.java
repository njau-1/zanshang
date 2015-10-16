package com.zanshang.services;

import com.zanshang.models.Person;
import org.bson.types.ObjectId;

/**
 * Created by Lookis on 6/4/15.
 */
public interface PersonTrapdoor {

    Person get(ObjectId uid);

    void save(Person person);

}
