package com.zanshang.services.salon.chat;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindByIndexPageableActor;
import com.zanshang.models.SalonChat;
import com.zanshang.models.index.SalonChatIndexBySalonId;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 7/3/15.
 */
public class SalonChatFindBySalonIdPageableActor extends FindByIndexPageableActor<ObjectId, SalonChat>{

    public SalonChatFindBySalonIdPageableActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, SalonChat>> indexClz() {
        return SalonChatIndexBySalonId.class;
    }

    @Override
    protected boolean reverse() {
        return true;
    }
}
