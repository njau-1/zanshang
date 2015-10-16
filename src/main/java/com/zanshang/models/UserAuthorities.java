package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collection;

/**
 * Created by Lookis on 4/28/15.
 */
@Document(collection = "authorities")
public class UserAuthorities {

    @Id
    @Field("uid")
    private ObjectId uid;

    @Field("authorities")
    private Collection<String> authorities;

    private UserAuthorities() {
    }

    public UserAuthorities(ObjectId uid, Collection<String> authorities) {
        this.uid = uid;
        this.authorities = authorities;
    }

    public ObjectId getUid() {
        return uid;
    }

    public Collection<String> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserAuthorities that = (UserAuthorities) o;

        return uid.equals(that.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
