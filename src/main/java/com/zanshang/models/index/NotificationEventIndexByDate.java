package com.zanshang.models.index;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.NotificationEvent;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 由NotificationEventCreateActor创建，这个是通知索引，以日期(yyyy-MM-dd)为id.便于查找当天要发送的通知信息.
 * Created by xuming on 15/8/26.
 */
@Document(collection = "notificationevent_indexby_date")
public class NotificationEventIndexByDate extends BaseArrayIndex<String, NotificationEvent> {

    public NotificationEventIndexByDate(String key) {
        super(key);
    }
}
