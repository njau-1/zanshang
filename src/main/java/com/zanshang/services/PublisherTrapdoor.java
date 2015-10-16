package com.zanshang.services;

import org.bson.types.ObjectId;

/**
 * Created by Lookis on 6/10/15.
 */
public interface PublisherTrapdoor {

    boolean isVerified(ObjectId uid);

    void submit(ObjectId uid);
}
