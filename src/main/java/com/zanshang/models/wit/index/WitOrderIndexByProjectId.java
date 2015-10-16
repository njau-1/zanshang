package com.zanshang.models.wit.index;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.wit.WitOrder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by xuming on 15/9/17.
 */
@Document(collection = "wit_order_indexby_project_id")
public class WitOrderIndexByProjectId extends BaseArrayIndex<ObjectId, WitOrder> {

    public WitOrderIndexByProjectId(ObjectId key) {
        super(key);
    }
}
