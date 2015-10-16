package com.zanshang.services.project;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindByIndexPageableActor;
import com.zanshang.models.Project;
import com.zanshang.models.index.ProjectIndexByUid;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/21/15.
 */
public class ProjectFindByUidPageableActor extends FindByIndexPageableActor<ObjectId, Project> {

    public ProjectFindByUidPageableActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, Project>> indexClz() {
        return ProjectIndexByUid.class;
    }
    
    @Override
    protected boolean reverse() {
    	return true;
    }
}
