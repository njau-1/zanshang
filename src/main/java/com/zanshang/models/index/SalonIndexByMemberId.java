package com.zanshang.models.index;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Salon;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 由SalonAddMemberActor创建，“我加入的沙龙”
 * Created by Lookis on 6/25/15.
 */
@Document(collection = "salon_indexby_memberid")
public class SalonIndexByMemberId extends BaseArrayIndex<ObjectId, Salon> {

    public SalonIndexByMemberId(ObjectId key) {
        super(key);
    }
}
