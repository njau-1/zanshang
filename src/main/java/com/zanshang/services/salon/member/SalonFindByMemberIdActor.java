package com.zanshang.services.salon.member;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindByIndexPageableActor;
import com.zanshang.models.Salon;
import com.zanshang.models.index.SalonIndexByMemberId;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 7/3/15.
 */
public class SalonFindByMemberIdActor extends FindByIndexPageableActor<ObjectId,Salon> {

    public SalonFindByMemberIdActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, Salon>> indexClz() {
        return SalonIndexByMemberId.class;
    }
}
