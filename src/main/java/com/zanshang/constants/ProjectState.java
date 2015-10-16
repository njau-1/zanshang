package com.zanshang.constants;

/**
 * Created by Lookis on 5/26/15.
 */
public enum ProjectState {

    REVIEWING("project.state.reviewing"),
    PRICING("project.state.pricing"),
    FUNDING("project.state.funding"),
    PUBLISHING("project.state.publishing"),
    DELIVERING("project.state.delivering"),
    SUCCESS("project.state.success"),
    REFUNDING("project.state.refunding"),
    FAILURE("project.state.failure");

    ProjectState(String message) {
        this.message = message;
    }

    private String message;

    public String getMessageCode() {
        return message;
    }
}

