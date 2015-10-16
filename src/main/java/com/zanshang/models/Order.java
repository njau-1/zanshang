package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by Lookis on 6/14/15.
 */
@Document(collection = "orders")
public class Order {

    @Id
    private ObjectId id;

    @Field("reward_id")
    private ObjectId rewardId;

    @Field("uid")
    private ObjectId uid;

    @Field("count")
    private int count;

    @Field("address")
    private String address;

    @Field("comment")
    private String comment;

    @Field("paid")
    private boolean paid;

    //仅供迁移时过渡使用，用于在第三方支付callback时查询，过后可删
    @Field("old_id")
    private String oldId;

    @Field("create_time")
    private Date createTime;

    @Field("sharer_id")
    private ObjectId sharerId;

    public Order(ObjectId uid, ObjectId rewardId, int count, String address, String comment) {
        this.id = ObjectId.get();
        this.uid = uid;
        this.rewardId = rewardId;
        this.count = count;
        this.address = address;
        this.comment = comment;
        this.paid = false;
        this.createTime = new Date();
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public ObjectId getUid() {
        return uid;
    }

    public void setUid(ObjectId uid) {
        this.uid = uid;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getRewardId() {
        return rewardId;
    }

    public void setRewardId(ObjectId rewardId) {
        this.rewardId = rewardId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public ObjectId getSharerId() {
        return sharerId;
    }

    public void setSharerId(ObjectId sharerId) {
        this.sharerId = sharerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Order order = (Order) o;

        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
