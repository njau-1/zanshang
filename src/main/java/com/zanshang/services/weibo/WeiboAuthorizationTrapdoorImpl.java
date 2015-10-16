package com.zanshang.services.weibo;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.RepositoryTrapdoor;
import com.zanshang.services.WeiboAuthorizationTrapdoor;
import com.zanshang.utils.AkkaTrapdoor;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Await;

import java.util.HashMap;
import java.util.Map;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/7/15.
 */
public class WeiboAuthorizationTrapdoorImpl extends RepositoryTrapdoor<String, Map<String, String>> implements
        WeiboAuthorizationTrapdoor {

    public static final String ACCESS_TOKEN = "access_token";

    public static final String COLLECTION_NAME = "weibo_access_token";

    private ActorRef fetchActor;

    public WeiboAuthorizationTrapdoorImpl(ApplicationContext springContext) {
        super(springContext);
        fetchActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(WeiboAuthorizationFetchActor.class, springContext)
        ), getClass().getSimpleName() + "Fetch");
    }

    @Override
    public Map<String, String> fetchAndSave(String code, String callbackUrl) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put(WeiboAuthorizationFetchActor.CALLBACK_PARAMETER, callbackUrl);
        parameter.put(WeiboAuthorizationFetchActor.CODE_PARAMETER, code);
        try {
            Object result = Await.result(ask(fetchActor, parameter, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
            return (Map<String, String>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " Fetch error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<? extends BaseUntypedActor> getClz() {
        return WeiboAuthorizationGetActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> saveClz() {
        return WeiboAuthorizationSaveActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> deleteClz() {
        return WeiboAuthorizationDeleteActor.class;
    }
}
