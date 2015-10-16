package com.zanshang.models.index;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 个人只能在平台里拥有一个账号，所以这里用身份证号作为主键，由审核后台创建
 * Created by Lookis on 5/27/15.
 */
@Document(collection = "author_information_indexby_code")
public class AuthorInformationIndexByIdentity {
    @Id
    @Field("identity")
    private String identity;

    @Field("uid")
    private ObjectId uid;

    private AuthorInformationIndexByIdentity() {
    }

    /**
     * 后台审核完后创建对象
     * @param identity 身份证号或者公司代码
     * @param uid 对应的用户id
     */
    public AuthorInformationIndexByIdentity(String identity, ObjectId uid) {
        this.identity = identity;
        this.uid = uid;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public ObjectId getUid() {
        return uid;
    }

    public void setUid(ObjectId uid) {
        this.uid = uid;
    }
}
