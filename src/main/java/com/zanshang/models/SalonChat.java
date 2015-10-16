package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "salon_chat")
public class SalonChat {

    @Id
    private ObjectId id;

    @Field("salon_id")
    private ObjectId salonId;

    @Field("uid")
    private ObjectId uid;

    @Field("chat")
    private String chat;

    @Field("createTime")
    private Date createTime;

    public SalonChat(ObjectId uid, ObjectId salonId, String chat) {
        this.id = ObjectId.get();
        this.salonId = salonId;
        this.createTime = new Date();
        this.uid = uid;
        this.chat = chat;
    }

    public ObjectId getSalonId() {
        return salonId;
    }

    public ObjectId getId() {
        return id;
    }

    public ObjectId getUid() {
        return uid;
    }

    public String getChat() {
        return chat;
    }

    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SalonChat salonChat = (SalonChat) o;

        return id.equals(salonChat.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}