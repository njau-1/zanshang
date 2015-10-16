package com.zanshang.models;

import org.bson.types.ObjectId;

/**
 * Created by xuming on 15/8/16.
 */
public class Receiver {

    private ObjectId uid;

    private String name;

    private String email;

    private String phone;

    public Receiver(ObjectId uid, String name, String email, String phone) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public ObjectId getUid() {
        return uid;
    }

    public void setUid(ObjectId uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
