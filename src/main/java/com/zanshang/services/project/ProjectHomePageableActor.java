package com.zanshang.services.project;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindByIndexPageableActor;
import com.zanshang.models.Project;
import com.zanshang.models.global.ProjectHome;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 7/3/15.
 */
public class ProjectHomePageableActor extends FindByIndexPageableActor<String, Project> {

    public ProjectHomePageableActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<String, Project>> indexClz() {
        return ProjectHome.class;
    }

    @Override
    protected boolean reverse() {
        return true;
    }
}
