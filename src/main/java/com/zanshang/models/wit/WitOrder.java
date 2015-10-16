package com.zanshang.models.wit;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by xuming on 15/9/17.
 */
@Document(collection = "wit_order")
public class WitOrder {

    @Id
    private ObjectId id;

    @Field("reward_id")
    private ObjectId rewardId;

    @Field("username")
    private String username;

    @Field("phone")
    private String phone;

    @Field("email")
    private String email;

    @Field("job")
    private String job;

    @Field("count")
    private int count;

    @Field("address")
    private String address;

    @Field("question")
    private String question;

    @Field("paid")
    private boolean paid;

    @Field("create_time")
    private Date createTime;

    public WitOrder(ObjectId rewardId, String username, String phone, String email, String job) {
        this.id = ObjectId.get();
        this.rewardId = rewardId;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.job = job;
        this.count = 1;
        this.paid = false;
        this.createTime = new Date();
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitOrder witOrder = (WitOrder) o;

        return !(id != null ? !id.equals(witOrder.id) : witOrder.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
