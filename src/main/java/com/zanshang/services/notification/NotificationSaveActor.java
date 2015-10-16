package com.zanshang.services.notification;

import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Notification;
import com.zanshang.models.index.NotificationIndexByUid;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/24/15.
 */
public class NotificationSaveActor extends AddToIndexActor<ObjectId,Notification> {

    public NotificationSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getMongoTemplate().save(o);
        super.onReceive(o);
    }

    @Override
    public Notification extractValue(Object o) {
        return (Notification) o;
    }

    @Override
    public ObjectId extractKey(Object o) {
        Notification notification = (Notification) o;
        return notification.getUid();
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, Notification>> indexClz() {
        return NotificationIndexByUid.class;
    }
}
