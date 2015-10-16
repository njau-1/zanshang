package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by Lookis on 6/22/15.
 */
@Document(collection = "messages")
public class Message {

    @Id
    private ObjectId id;

    @Field("sender")
    private ObjectId sender;

    @Field("recipient")
    private ObjectId recipient;

    @Field("message")
    private String message;

    @Field("create_time")
    private Date time;

    public Message(ObjectId sender, ObjectId recipient, String message) {
        this.id = ObjectId.get();
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.time = new Date();
    }

    public Date getTime() {
        return time;
    }

    public ObjectId getId() {
        return id;
    }

    public ObjectId getSender() {
        return sender;
    }

    public ObjectId getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Message message = (Message) o;

        return id.equals(message.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
