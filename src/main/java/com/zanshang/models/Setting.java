package com.zanshang.models;

import org.apache.commons.lang.math.RandomUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lookis on 6/2/15.
 */
@Document(collection = "settings")
public class Setting {

    @Id
    private ObjectId uid;

    @Field("avatar")
    private String avatar;

    @Field("display_name")
    private String displayName;

    @Field("email")
    private String email;

    @Field("addresses")
    private List<ObjectId> addresses;

    private Setting() {
    }

    public Setting(ObjectId uid, String displayName, String email) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.addresses = new ArrayList<>();
        this.avatar = "/static/random"+ RandomUtils.nextInt(10)+".png";
    }

    public ObjectId getUid() {
        return uid;
    }

    public void setUid(ObjectId uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ObjectId> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<ObjectId> addresses) {
        this.addresses = addresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Setting setting = (Setting) o;

        return uid.equals(setting.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
