package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lookis on 6/25/15.
 */
@Document(collection = "salon_topic")
public class SalonTopic {

    @Id
    private ObjectId id;

    @Field("salon_id")
    private ObjectId salonId;

    @Field("title")
    private String title;

    @Field("images")
    private List<String> images;

    @Field("content")
    private String content;

    @Field("create_time")
    private Date createTime;

    public SalonTopic(ObjectId salonId, String title, String content, List<String> images) {
        this.title = title;
        this.salonId = salonId;
        if (images == null) {
            this.images = new ArrayList<>();
        } else {
            this.images = images;
        }
        this.id = ObjectId.get();
        this.content = content;
        this.createTime = new Date();
    }

    public String getContent() {
        return content;
    }

    public ObjectId getSalonId() {
        return salonId;
    }

    public ObjectId getId() {

        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getImages() {
        return images;
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

        SalonTopic that = (SalonTopic) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
