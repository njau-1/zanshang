package com.zanshang.models.index;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Bid;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 由BidSaveActor负责创建
 * Created by Lookis on 6/19/15.
 */
@Document(collection = "bid_indexby_project_id")
public class BidIndexByProjectId extends BaseArrayIndex<ObjectId, Bid> {

    public BidIndexByProjectId(ObjectId key) {
        super(key);
    }
}
