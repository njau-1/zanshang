package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 后台审核完创建
 * Created by Lookis on 6/5/15.
 */
@Document(collection = "publishers")
public class Publisher {
    @Id
    private ObjectId uid;

    public Publisher(ObjectId uid) {
        this.uid = uid;
    }

    public ObjectId getUid() {

        return uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Publisher publisher = (Publisher) o;

        return uid.equals(publisher.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
