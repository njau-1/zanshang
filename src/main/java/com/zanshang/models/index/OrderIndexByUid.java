package com.zanshang.models.index;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 由OrderSaveActor创建，使用uid做索引
 * Created by Lookis on 6/14/15.
 */
@Document(collection = "order_indexby_uid")
public class OrderIndexByUid extends BaseArrayIndex<ObjectId, Order>{

    public OrderIndexByUid(ObjectId key) {
        super(key);
    }
}
