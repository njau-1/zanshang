package com.zanshang.services.project;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Project;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/5/15.
 */
public class ProjectGetActor extends BaseUntypedActor {

    public ProjectGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Project byId = getMongoTemplate().findById(o.toString(), Project.class);
        getSender().tell(byId, getSelf());
    }
}
