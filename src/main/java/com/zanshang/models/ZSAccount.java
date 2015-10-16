package com.zanshang.models;

import org.bson.types.ObjectId;

/**
 * Created by Lookis on 4/28/15.
 */
public interface ZSAccount {

    public EPPassword getEPPassword();

    public UserAuthorities getUserAuthorities();

    public ObjectId getUid();
}
