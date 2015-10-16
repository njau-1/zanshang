package com.zanshang.models.index;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 由后台锁定了出版社之后创建
 * Created by Lookis on 6/22/15.
 */
@Document(collection = "project_indexby_publisherid")
public class ProjectIndexByPublisherId extends BaseArrayIndex<ObjectId, Project>{

    public ProjectIndexByPublisherId(ObjectId key) {
        super(key);
    }
}
