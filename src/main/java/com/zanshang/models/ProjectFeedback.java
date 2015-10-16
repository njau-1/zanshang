package com.zanshang.models;

import com.zanshang.framework.Price;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 由后台创建
 * Created by Lookis on 5/28/15.
 */
@Document(collection = "project_feedback")
public class ProjectFeedback {

    @Id
    @Field("project_id")
    private ObjectId projectId;

    @Field("cost")
    private Price cost;

    @Field("goal")
    private Price goal;

    @Field("details")
    private String details;

    @Field("is_pass")
    private boolean isPass;

    protected ProjectFeedback() {
    }

    public ProjectFeedback(ObjectId projectId, Price cost, Price goal, String details, boolean isPass) {
        this.projectId = projectId;
        this.cost = cost;
        this.goal = goal;
        this.details = details;
        this.isPass = isPass;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setIsPass(boolean isPass) {
        this.isPass = isPass;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public Price getCost() {
        return cost;
    }

    public void setCost(Price cost) {
        this.cost = cost;
    }

    public Price getGoal() {
        return goal;
    }

    public void setGoal(Price goal) {
        this.goal = goal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ProjectFeedback feedback = (ProjectFeedback) o;

        return projectId.equals(feedback.projectId);
    }

    @Override
    public int hashCode() {
        return projectId.hashCode();
    }
}
