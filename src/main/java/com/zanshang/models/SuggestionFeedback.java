package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by xuming on 15/9/24.
 */
@Document(collection = "suggestfeedback")
public class SuggestionFeedback {

    @Id
    private ObjectId id;

    @Field
    private String content;

    @Field
    private String contact;

    @Field
    private String ticket;

    @Field
    private ObjectId uid;

    @Field
    private Date createDate;

    public SuggestionFeedback(String content, String contact, String ticket, ObjectId uid) {
        this.content = content;
        this.contact = contact;
        this.ticket = ticket;
        this.uid = uid;
        this.createDate = new Date();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public ObjectId getUid() {
        return uid;
    }

    public void setUid(ObjectId uid) {
        this.uid = uid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
