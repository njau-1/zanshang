package com.zanshang.services.project;

import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Project;
import com.zanshang.models.global.ProjectHome;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 7/10/15.
 */
public class ProjectHomeSaveActor extends AddToIndexActor<String, Project> {

    public ProjectHomeSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Project extractValue(Object o) {
        return (Project) o;
    }

    @Override
    public String extractKey(Object o) {
        return ProjectHome.GLOBAL_KEY;
    }

    @Override
    public Class<? extends BaseArrayIndex<String, Project>> indexClz() {
        return ProjectHome.class;
    }
}
