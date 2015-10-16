package com.zanshang.services.project;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.zanshang.constants.ProjectState;
import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Project;
import com.zanshang.models.SalonTopic;
import com.zanshang.models.audit.AuditProject;
import com.zanshang.models.index.ProjectIndexByUid;
import com.zanshang.services.salon.SalonHomeSaveActor;
import com.zanshang.services.salon.topic.SalonTopicCreateActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lookis on 6/5/15.
 */
public class ProjectSaveActor extends AddToIndexActor<ObjectId, Project> {

    ActorRef projectHomeActor;

    ActorRef salonHomeActor;

    ActorRef salonTopicCreateActor;

    public ProjectSaveActor(ApplicationContext spring) {
        super(spring);
        projectHomeActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(ProjectHomeSaveActor.class, spring)));
        salonHomeActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(SalonHomeSaveActor.class, spring)));
        salonTopicCreateActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(SalonTopicCreateActor.class, spring)));
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Project project = (Project) o;
        Project origin = getMongoTemplate().findById(project.getId(), Project.class);
        if (origin == null || (origin.getState() == ProjectState.REVIEWING)) {
            AuditProject auditProject = new AuditProject(project.getId());
            getMongoTemplate().save(auditProject);
        }
        super.onReceive(o);
        if (origin != null && origin.getState() == ProjectState.PRICING && project.getState() == ProjectState.FUNDING) {
            salonTopicCreateActor.tell(new SalonTopic(project.getUid(), project.getBookName(), project.getDraft(), new ArrayList<String>()), getSelf());
            projectHomeActor.tell(project, getSelf());
            salonHomeActor.tell(project, getSelf());
        }
        getMongoTemplate().save(o);
    }

    @Override
    public Project extractValue(Object o) {
        return (Project) o;
    }

    @Override
    public ObjectId extractKey(Object o) {
        Project project = (Project) o;
        return project.getUid();
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, Project>> indexClz() {
        return ProjectIndexByUid.class;
    }
}
