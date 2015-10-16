package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Lookis on 6/7/15.
 */
@Document(collection = "addresses")
public class Address {
    @Id
    private ObjectId id;

    @Field("uid")
    private ObjectId uid;

    @Field("recipient")
    private String recipient;

    @Field("telephone")
    private String telephone;

    @Field("post_code")
    private String postCode;

    @Field("address")
    private String address;

    public Address(ObjectId uid,String recipient, String telephone, String postCode, String address) {
        this.uid = uid;
        this.id = ObjectId.get();
        this.recipient = recipient;
        this.telephone = telephone;
        this.postCode = postCode;
        this.address = address;
    }

    public void setUid(ObjectId uid) {
        this.uid = uid;
    }

    public ObjectId getUid() {
        return uid;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Address address = (Address) o;

        return id.equals(address.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
