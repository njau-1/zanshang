package com.zanshang.services.notificationevent;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindAllByIndexActor;
import com.zanshang.models.NotificationEvent;
import com.zanshang.models.index.NotificationEventIndexByDate;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by xuming on 15/8/26.
 */
public class NotificationEventFindByDateActor extends FindAllByIndexActor<String, NotificationEvent> {

    public NotificationEventFindByDateActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<String, NotificationEvent>> indexClz() {
        return NotificationEventIndexByDate.class;
    }
}
