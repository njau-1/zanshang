package com.zanshang.framework;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by Lookis on 5/27/15.
 */
public abstract class BaseAuditIndexStub {

    @Id
    private Object target;

    @Field("create_time")
    private Date createTime;

    protected BaseAuditIndexStub() {
    }

    public BaseAuditIndexStub(Object target) {
        this.target = target;
        this.createTime = new Date();
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
