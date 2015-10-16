package com.zanshang.services.salon.topic;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindByIndexPageableActor;
import com.zanshang.models.SalonTopic;
import com.zanshang.models.index.SalonTopicIndexBySalonId;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 7/3/15.
 */
public class SalonTopicFindBySalonIdPageableActor extends FindByIndexPageableActor<ObjectId, SalonTopic> {

    public SalonTopicFindBySalonIdPageableActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, SalonTopic>> indexClz() {
        return SalonTopicIndexBySalonId.class;
    }

    @Override
    protected boolean reverse() {
        return true;
    }
}
