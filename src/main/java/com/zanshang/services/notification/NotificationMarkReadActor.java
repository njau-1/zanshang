package com.zanshang.services.notification;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Notification;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;

/**
 * Created by Lookis on 6/24/15.
 */
public class NotificationMarkReadActor extends BaseUntypedActor {

    public NotificationMarkReadActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Collection<ObjectId> readedId = (Collection<ObjectId>) o;
        Query query = Query.query(Criteria.where("_id").in(readedId));
        Update update = Update.update(Notification.FIELD_READ, true);
        getMongoTemplate().updateMulti(query, update, Notification.class);
    }
}
