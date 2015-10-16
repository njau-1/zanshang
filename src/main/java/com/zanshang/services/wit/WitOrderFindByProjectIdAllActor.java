package com.zanshang.services.wit;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindAllByIndexActor;
import com.zanshang.models.wit.WitOrder;
import com.zanshang.models.wit.index.WitOrderIndexByProjectId;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by xuming on 15/9/17.
 */
public class WitOrderFindByProjectIdAllActor extends FindAllByIndexActor<ObjectId, WitOrder> {

    public WitOrderFindByProjectIdAllActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, WitOrder>> indexClz() {
        return WitOrderIndexByProjectId.class;
    }
}
