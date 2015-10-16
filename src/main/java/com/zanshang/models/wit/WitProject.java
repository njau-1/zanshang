package com.zanshang.models.wit;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * Created by xuming on 15/9/17.
 */
@Document(collection = "wit_project")
public class WitProject {
    @Id
    private ObjectId id;

    @Field("project_name")
    private String projectName;

    @Field("description")
    private String description; // 简介

    @Field("wit_rewards")
    private List<ObjectId> rewards; //回报

    @Field("create_date")
    private Date createDate;

    @Field("address")
    private String address;

    @Field("date")
    private String date;

    public WitProject(String projectName, String description, List<ObjectId> rewards, String address, String date) {
        this.id = ObjectId.get();
        this.projectName = projectName;
        this.description = description;
        this.rewards = rewards;
        this.address = address;
        this.createDate = new Date();
        this.date = date;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ObjectId> getRewards() {
        return rewards;
    }

    public void setRewards(List<ObjectId> rewards) {
        this.rewards = rewards;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
