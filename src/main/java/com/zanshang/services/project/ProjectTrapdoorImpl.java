package com.zanshang.services.project;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.Price;
import com.zanshang.framework.index.IndexPageableParams;
import com.zanshang.models.Project;
import com.zanshang.models.global.ProjectHome;
import com.zanshang.services.ProjectTrapdoor;
import com.zanshang.framework.RepositoryTrapdoor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import scala.concurrent.Await;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Lookis on 6/5/15.
 */
public class ProjectTrapdoorImpl extends RepositoryTrapdoor<ObjectId, Project> implements ProjectTrapdoor {

    ActorRef findByUid;

    ActorRef findByPublisherId;

    ActorRef findByIds;

    ActorRef paidActor;

    ActorRef projectHomePageable;

    ActorRef paidsActor;

    public ProjectTrapdoorImpl(ApplicationContext springContext) {
        super(springContext);
        findByUid = ctx.actorOf(Props.create(AkkaTrapdoor.creator(ProjectFindByUidPageableActor.class, springContext)));
        findByPublisherId = ctx.actorOf(Props.create(AkkaTrapdoor.creator(ProjectFindByPublisherIdPageableActor
                .class, springContext)));
        findByIds = ctx.actorOf(Props.create(AkkaTrapdoor.creator(ProjectFindByIdsActor.class, springContext)));
        paidActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(ProjectPaidActor.class, springContext)));
        paidsActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(ProjectMultiPaidActor.class, springContext)));
        projectHomePageable = ctx.actorOf(Props.create(AkkaTrapdoor.creator(ProjectHomePageableActor.class,
                springContext)));
    }

    @Override
    public Page<Project> globalProject(Pageable pageable) {
        try {
            Object result = Await.result(Patterns.ask(projectHomePageable, new IndexPageableParams(pageable,
                    ProjectHome.GLOBAL_KEY), ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Page<Project>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " ProjectHome error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Project> findByUid(ObjectId uid, Pageable pageable) {
        try {
            Object result = Await.result(Patterns.ask(findByUid, new IndexPageableParams(pageable, uid),
                    ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Page<Project>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " FindByUid error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Project> findByPublisherId(ObjectId uid, Pageable pageable) {
        try {
            Object result = Await.result(Patterns.ask(findByPublisherId, new IndexPageableParams(pageable, uid),
                    ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Page<Project>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " FindByPublisherId error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Project> findByIds(List<ObjectId> ids, Pageable pageable) {
        try {
            Object result = Await.result(Patterns.ask(findByIds, new Object[]{ids, pageable}, ActorConstant
                    .DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Page<Project>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " FindByIds error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<ObjectId, Project> findByIds(List<ObjectId> ids) {
        try {
            Object result = Await.result(Patterns.ask(findByIds, ids, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return (Map<ObjectId, Project>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " FindByIds error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Price getPaid(ObjectId projectId) {
        try {
            Object result = Await.result(Patterns.ask(paidActor, projectId, ActorConstant.DEFAULT_TIMEOUT),
                    ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Price) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " PaidActor error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<ObjectId, Price> getPaids(Collection<ObjectId> projectIds) {
        try {
            Object result = Await.result(Patterns.ask(paidsActor, projectIds, ActorConstant.DEFAULT_TIMEOUT),
                    ActorConstant.DEFAULT_TIMEOUT_DURATION);
            return (Map<ObjectId, Price>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " PaidsActor error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<? extends BaseUntypedActor> getClz() {
        return ProjectGetActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> saveClz() {
        return ProjectSaveActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> deleteClz() {
        return null;
    }
}
