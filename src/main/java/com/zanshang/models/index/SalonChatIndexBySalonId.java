package com.zanshang.models.index;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.SalonChat;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Lookis on 7/3/15.
 */
@Document(collection = "salon_chat_indexby_salon_id")
public class SalonChatIndexBySalonId extends BaseArrayIndex<ObjectId, SalonChat> {

    public SalonChatIndexBySalonId(ObjectId key) {
        super(key);
    }
}
