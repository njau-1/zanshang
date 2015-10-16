package com.zanshang.models;

import com.zanshang.constants.NotificationConstants;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Lookis on 6/24/15.
 */
@Document(collection = "notifications")
public class Notification {

    public static final String FIELD_READ = "read";
    @Id
    private ObjectId id;

    @Field("uid")
    private ObjectId uid;

    @Field("notification_type")
    private String templateName;

    @Field("params")
    private Map<String, String> paramMap;

    @Field("create_time")
    private Date createTime;

    @Field(FIELD_READ)
    private boolean read;

    public Notification(ObjectId uid, String templateName, Map<String, String> paramMap) {
        this.uid = uid;
        this.templateName = templateName;
        this.paramMap = paramMap;
        this.read = false;
        this.createTime = new Date();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Notification that = (Notification) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
