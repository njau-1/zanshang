package com.zanshang.services.setting;

import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Setting;
import com.zanshang.services.SettingTrapdoor;
import com.zanshang.framework.RepositoryTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Await;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/4/15.
 */
public class SettingTrapdoorImpl extends RepositoryTrapdoor<ObjectId, Setting> implements SettingTrapdoor {



    public SettingTrapdoorImpl(ApplicationContext springContext) {
        super(springContext);
    }

    @Override
    public Map<ObjectId, Setting> findByIds(Collection<ObjectId> ids) {
        try {
            Object result = Await.result(ask(get, ids, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return (Map<ObjectId, Setting>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " Get error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Setting get(ObjectId key) {
        return findByIds(Collections.singleton(key)).get(key);
    }

    @Override
    public Class<? extends BaseUntypedActor> getClz() {
        return SettingGetActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> saveClz() {
        return SettingSaveActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> deleteClz() {
        return null;
    }
}
