package com.zanshang.models.index;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**由ProjectSaveActor创建，只要用户创建了项目就有索引
 * Created by Lookis on 6/10/15.
 */
@Document(collection = "project_indexby_uid")
public class ProjectIndexByUid extends BaseArrayIndex<ObjectId, Project>{

    public ProjectIndexByUid(ObjectId key) {
        super(key);
    }
}
