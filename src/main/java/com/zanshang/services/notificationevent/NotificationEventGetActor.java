package com.zanshang.services.notificationevent;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.NotificationEvent;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by xuming on 15/8/26.
 */
public class NotificationEventGetActor extends BaseUntypedActor {

    public NotificationEventGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        NotificationEvent notificationEvent = getMongoTemplate().findById((ObjectId) o, NotificationEvent.class);
        sender().tell(notificationEvent, getSelf());
    }
}
