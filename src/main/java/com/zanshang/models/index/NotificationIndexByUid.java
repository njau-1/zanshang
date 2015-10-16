package com.zanshang.models.index;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Notification;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 通知索引，由 NotificationSaveActor创建
 * Created by Lookis on 6/24/15.
 */
@Document(collection = "notification_indexby_uid")
public class NotificationIndexByUid extends BaseArrayIndex<ObjectId, Notification>{

    public NotificationIndexByUid(ObjectId key) {
        super(key);
    }
}
