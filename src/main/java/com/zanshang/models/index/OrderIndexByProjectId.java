package com.zanshang.models.index;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**由OrderSaveActor创建 通过projectId 索引order
 * Created by Lookis on 6/14/15.
 */
@Document(collection = "order_indexby_project_id")
public class OrderIndexByProjectId extends BaseArrayIndex<ObjectId, Order>{

    public OrderIndexByProjectId(ObjectId key) {
        super(key);
    }
}
