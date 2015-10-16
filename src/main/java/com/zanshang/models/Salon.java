package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * 沙龙
 * 新版沙龙不再按照项目来，而是按作者来标识，每一个作者有一个沙龙
 * Created by Lookis on 6/25/15.
 */
@Document(collection = "salons")
public class Salon {

    public static final String FIELD_MEMBERS = "members";

    //作者id
    @Id
    private ObjectId uid;

    @Field(FIELD_MEMBERS)
    private List<ObjectId> members;

    public Salon(ObjectId uid) {
        this.uid = uid;
        this.members = new ArrayList<>();
    }

    public ObjectId getUid() {
        return uid;
    }

    public List<ObjectId> getMembers() {
        return members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Salon salon = (Salon) o;

        return uid.equals(salon.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
