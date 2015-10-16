package com.zanshang.services.company;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.RepositoryTrapdoor;
import com.zanshang.models.Company;
import com.zanshang.services.CompanyTrapdoor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Await;

import java.util.Collection;
import java.util.Map;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/4/15.
 */
public class CompanyTrapdoorImpl extends RepositoryTrapdoor<ObjectId, Company> implements CompanyTrapdoor {

    ActorRef findByIds;

    public CompanyTrapdoorImpl(ApplicationContext springContext) {
        super(springContext);
        findByIds = ctx.actorOf(Props.create(AkkaTrapdoor.creator(CompanyFindByIdsActor.class, springContext)));
    }

    @Override
    public Class<? extends BaseUntypedActor> getClz() {
        return CompanyGetActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> saveClz() {
        return CompanySaveActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> deleteClz() {
        return null;
    }

    @Override
    public Map<ObjectId, Company> findById(Collection<ObjectId> ids) {
        try {
            Object result = Await.result(ask(findByIds, ids, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return (Map<ObjectId, Company>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " FindByIds error.", e);
            throw new RuntimeException(e);
        }
    }
}
