package com.zanshang.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Lookis on 6/7/15.
 */
@Document(collection = "wechat_informations")
public class WechatInformation {

    @Id
    private String unionId;

    @Field("name")
    private String name;

    @Field("avatar")
    private String avatar;

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public WechatInformation(String unionId, String name, String avatar) {
        this.unionId = unionId;
        this.name = name;
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        WechatInformation that = (WechatInformation) o;

        return unionId.equals(that.unionId);
    }

    @Override
    public int hashCode() {
        return unionId.hashCode();
    }
}
