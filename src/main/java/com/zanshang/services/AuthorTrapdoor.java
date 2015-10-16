package com.zanshang.services;

import org.bson.types.ObjectId;

/**
 * Created by Lookis on 6/4/15.
 */
public interface AuthorTrapdoor {

    boolean isVerified(ObjectId uid);

    boolean isFilled(ObjectId uid);
}
