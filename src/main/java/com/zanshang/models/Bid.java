package com.zanshang.models;

import com.zanshang.framework.Price;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Lookis on 6/19/15.
 */
@Document(collection = "bids")
public class Bid {

    @Id
    private ObjectId id;

    @Field("uid")
    private ObjectId uid;

    @Field("project_id")
    private ObjectId projectId;

    @Field("price")
    private Price price;

    public Bid(ObjectId uid, ObjectId projectId, Price price) {
        this.id = ObjectId.get();
        this.uid = uid;
        this.projectId = projectId;
        this.price = price;
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

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Bid bid = (Bid) o;

        return id.equals(bid.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
