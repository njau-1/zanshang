package com.zanshang.services.notificationevent;

import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.NotificationEvent;
import com.zanshang.models.index.NotificationEventIndexByDate;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by xuming on 15/8/26.
 */
public class NotificationEventSaveActor extends AddToIndexActor<String, NotificationEvent> {

    public NotificationEventSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getMongoTemplate().save(extractValue(o));
        super.onReceive(o);
    }

    @Override
    public NotificationEvent extractValue(Object o) {
        return (NotificationEvent) o;
    }

    @Override
    public String extractKey(Object o) {
        NotificationEvent notificationEvent = (NotificationEvent) o;
        return notificationEvent.getExecDate();
    }

    @Override
    public Class<? extends BaseArrayIndex<String, NotificationEvent>> indexClz() {
        return NotificationEventIndexByDate.class;
    }
}
