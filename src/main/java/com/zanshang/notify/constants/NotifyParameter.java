package com.zanshang.notify.constants;

import org.bson.types.ObjectId;

import java.util.Map;

/**
 * Created by xuming on 15/9/9.
 */
public class NotifyParameter {

    private ObjectId uid;

    private NotifyBusinessType notifyBusinessType;

    private Map<String, String> model;

    public NotifyParameter(ObjectId uid, NotifyBusinessType notifyBusinessType, Map<String, String> model) {
        this.uid = uid;
        this.notifyBusinessType = notifyBusinessType;
        this.model = model;
    }

    public ObjectId getUid() {
        return uid;
    }

    public void setUid(ObjectId uid) {
        this.uid = uid;
    }

    public NotifyBusinessType getNotifyBusinessType() {
        return notifyBusinessType;
    }

    public void setNotifyBusinessType(NotifyBusinessType notifyBusinessType) {
        this.notifyBusinessType = notifyBusinessType;
    }

    public Map<String, String> getModel() {
        return model;
    }

    public void setModel(Map<String, String> model) {
        this.model = model;
    }
}
