package com.zanshang.services;

import com.zanshang.models.Bid;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Lookis on 6/5/15.
 */
public interface BidTrapdoor {

    Bid get(ObjectId bid);

    List<Bid> findByProjectId(ObjectId projectId);

    void save(Bid bid);

    void delete(ObjectId id);
}
