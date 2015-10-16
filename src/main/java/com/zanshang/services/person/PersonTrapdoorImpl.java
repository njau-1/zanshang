package com.zanshang.services.person;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Person;
import com.zanshang.services.PersonTrapdoor;
import com.zanshang.framework.RepositoryTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/4/15.
 */
public class PersonTrapdoorImpl extends RepositoryTrapdoor<ObjectId, Person> implements PersonTrapdoor {

    public PersonTrapdoorImpl(ApplicationContext springContext) {
        super(springContext);
    }

    @Override
    public Class<? extends BaseUntypedActor> getClz() {
        return PersonGetActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> saveClz() {
        return PersonSaveActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> deleteClz() {
        return null;
    }
}
