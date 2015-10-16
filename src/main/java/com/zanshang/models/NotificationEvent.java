package com.zanshang.models;


import com.zanshang.constants.NotificationConstants;
import com.zanshang.constants.NotificationType;
import com.zanshang.notify.constants.NotifyBusinessType;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xuming on 15/8/14.
 */
@Document(collection = "notification_event")
public class NotificationEvent {

    @Id
    private ObjectId id;

    @Field("uid")
    private ObjectId uid;

    @Field("notification_type")
    private NotifyBusinessType notifyBusinessType;

    @Field("param_map")
    private Map<String, String> paramMap;

    @Field("isSend")
    private Boolean isSend;

    @Field("exec_date")
    private String execDate;

    public NotificationEvent() {
    }

    public NotificationEvent(ObjectId uid, NotifyBusinessType notifyBusinessType, Map<String, String> paramMap, Date date) {
        this.uid = uid;
        this.notifyBusinessType = notifyBusinessType;
        this.paramMap = paramMap;
        this.isSend = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.execDate = sdf.format(date);
    }

    public NotificationEvent(ObjectId uid, NotifyBusinessType notifyBusinessType, Map<String, String> paramMap, String execDate) {
        this.uid = uid;
        this.notifyBusinessType = notifyBusinessType;
        this.paramMap = paramMap;
        this.isSend = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.execDate = execDate;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getUid() {
        return uid;
    }

    public void setUid(ObjectId uid) {
        this.uid = uid;
    }

    public Boolean getIsSend() {
        return isSend;
    }

    public void setIsSend(Boolean isSend) {
        this.isSend = isSend;
    }

    public NotifyBusinessType getNotifyBusinessType() {
        return notifyBusinessType;
    }

    public void setNotifyBusinessType(NotifyBusinessType notifyBusinessType) {
        this.notifyBusinessType = notifyBusinessType;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public String getExecDate() {
        return execDate;
    }

    public void setExecDate(String execDate) {
        this.execDate = execDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationEvent that = (NotificationEvent) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
