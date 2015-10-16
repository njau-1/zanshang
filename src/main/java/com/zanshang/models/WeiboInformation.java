package com.zanshang.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Lookis on 6/7/15.
 */
@Document(collection = "weibo_informations")
public class WeiboInformation {

    @Id
    private String weiboId;

    @Field("name")
    private String name;

    @Field("avatar")
    private String avatar;


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

    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    public WeiboInformation(String weiboId, String name, String avatar) {

        this.weiboId = weiboId;
        this.name = name;
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        WeiboInformation that = (WeiboInformation) o;

        return weiboId.equals(that.weiboId);
    }

    @Override
    public int hashCode() {
        return weiboId.hashCode();
    }
}
