package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by xuming on 15/9/1.
 */
@Document(collection = "share_reward")
public class ShareReward {

    @Id
    private ObjectId id;

    @Field(value = "buyer_id")
    private ObjectId buyerId;

    @Field(value = "sharer_id")
    private ObjectId sharerId;

    @Field(value = "order_id")
    private ObjectId orderId;

    public ShareReward(ObjectId buyerId, ObjectId sharerId, ObjectId orderId) {
        this.id = ObjectId.get();
        this.buyerId = buyerId;
        this.sharerId = sharerId;
        this.orderId = orderId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(ObjectId buyerId) {
        this.buyerId = buyerId;
    }

    public ObjectId getSharerId() {
        return sharerId;
    }

    public void setSharerId(ObjectId sharerId) {
        this.sharerId = sharerId;
    }

    public ObjectId getOrderId() {
        return orderId;
    }

    public void setOrderId(ObjectId orderId) {
        this.orderId = orderId;
    }
}
