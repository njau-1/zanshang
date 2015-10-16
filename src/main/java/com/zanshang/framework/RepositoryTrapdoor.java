package com.zanshang.framework;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.TypedActor;
import com.zanshang.constants.ActorConstant;
import com.zanshang.utils.AkkaTrapdoor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Await;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/4/15.
 */
public abstract class RepositoryTrapdoor<K, R> extends TrapdoorSupervisor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ActorContext ctx = TypedActor.context();

    protected ActorRef get;

    protected ActorRef save;

    protected ActorRef delete;

    public RepositoryTrapdoor(ApplicationContext springContext) {
        save = saveClz() == null ? null : ctx.actorOf(Props.create(AkkaTrapdoor.creator(saveClz(), springContext)),
                getClass().getSimpleName() + "Save");
        get = getClz() == null ? null : ctx.actorOf(Props.create(AkkaTrapdoor.creator(getClz(), springContext)),
                getClass().getSimpleName() + "Get");
        delete = deleteClz() == null ? null : ctx.actorOf(Props.create(AkkaTrapdoor.creator(deleteClz(),
                springContext)), getClass().getSimpleName() + "Delete");
    }

    public void save(R toSave) {
        if (save != null)
            save.tell(toSave, ctx.self());
    }

    public R get(K key) {
        if (get == null)
            return null;
        try {
            Object result = Await.result(ask(get, key, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return (R) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " Get error.", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(K key) {
        if (delete != null)
            delete.tell(key, ctx.self());
    }

    public abstract Class<? extends BaseUntypedActor> getClz();

    public abstract Class<? extends BaseUntypedActor> saveClz();

    public abstract Class<? extends BaseUntypedActor> deleteClz();

}
