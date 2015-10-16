package com.zanshang.models.index;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.SalonTopic;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Lookis on 7/3/15.
 */
@Document(collection = "salon_topic_indexby_salon_id")
public class SalonTopicIndexBySalonId extends BaseArrayIndex<ObjectId, SalonTopic> {

    public SalonTopicIndexBySalonId(ObjectId key) {
        super(key);
    }
}
