package com.zanshang.services.notification;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindByIndexPageableActor;
import com.zanshang.models.Notification;
import com.zanshang.models.index.NotificationIndexByUid;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/24/15.
 */
public class NotificationFindByUidActor extends FindByIndexPageableActor<ObjectId, Notification> {

    public NotificationFindByUidActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, Notification>> indexClz() {
        return NotificationIndexByUid.class;
    }

    @Override
    protected boolean reverse() {
        return true;
    }
}