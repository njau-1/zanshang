package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Lookis on 6/3/15.
 */
@Document(collection = "persons")
public class Person {

    @Id
    private ObjectId uid;

    @Field("phone")
    private String phone;

    @Field("qq")
    private Long qq;

    @Field("legal_name")
    private String legalName;

    @Field("identity_code")
    private String identityCode;

    @Field("identity_front")
    private String identityFront;

    @Field("identity_back")
    private String identityBack;

    @Field("wechat")
    private String wechatId;

    @Field("weibo")
    private String weiboId;

    private Person() {
    }

    public Person(ObjectId uid, String phone) {
        this.uid = uid;
        this.phone = phone;
        qq = null;
    }

    public ObjectId getUid() {
        return uid;
    }

    public void setUid(ObjectId uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getQq() {
        return qq;
    }

    public void setQq(long qq) {
        this.qq = qq;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public String getIdentityFront() {
        return identityFront;
    }

    public void setIdentityFront(String identityFront) {
        this.identityFront = identityFront;
    }

    public String getIdentityBack() {
        return identityBack;
    }

    public void setIdentityBack(String identityBack) {
        this.identityBack = identityBack;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Person person = (Person) o;

        return uid.equals(person.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
