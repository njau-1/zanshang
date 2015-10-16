package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Lookis on 7/21/15.
 */
@Document(collection = "book_id_project_id")
public class OldBookMapping {

    @Id
    private String bookId;

    @Field("project_id")
    private ObjectId projectId;

    public String getBookId() {
        return bookId;
    }

    public ObjectId getProjectId() {
        return projectId;
    }
}
