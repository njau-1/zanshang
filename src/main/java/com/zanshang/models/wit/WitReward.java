package com.zanshang.models.wit;

import com.zanshang.framework.Price;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


/**
 * Created by xuming on 15/9/17.
 */
@Document(collection = "wit_reward")
public class WitReward {

    @Id
    @Field("id")
    private ObjectId id;

    @Field("reward_name")
    private String rewardName;

    @Field("project_id")
    private ObjectId projectId;

    @Field("description")
    private String description;

    @Field("count")
    private int count;

    @Field("price")
    private Price price;

    public WitReward(ObjectId projectId, String rewardName, String description, int count, Price price) {
        this.id = ObjectId.get();
        this.rewardName = rewardName;
        this.projectId = projectId;
        this.description = description;
        this.count = count;
        this.price = price;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WitReward witReward = (WitReward) o;

        return !(id != null ? !id.equals(witReward.id) : witReward.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
