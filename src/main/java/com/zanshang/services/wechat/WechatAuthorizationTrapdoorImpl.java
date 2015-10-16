package com.zanshang.services.wechat;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.zanshang.constants.ActorConstant;
import com.zanshang.constants.WechatPlatform;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.RepositoryTrapdoor;
import com.zanshang.services.WechatAuthorizationTrapdoor;
import com.zanshang.utils.AkkaTrapdoor;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Await;

import java.util.Map;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 6/7/15.
 */
public class WechatAuthorizationTrapdoorImpl extends RepositoryTrapdoor<String, Map<String, String>> implements
        WechatAuthorizationTrapdoor {

    public static final String COLLECTION_NAME = "wechat_access_token";

    private ActorRef fetchActor;

    public WechatAuthorizationTrapdoorImpl(ApplicationContext springContext) {
        super(springContext);
        fetchActor = ctx.actorOf(Props.create(AkkaTrapdoor.creator(WechatAuthorizationFetchActor.class,
                springContext)), getClass().getSimpleName() + "Fetch");
    }

    @Override
    public Map<String, String> fetchAndSave(String code, WechatPlatform platform, boolean baseApi) {
        try {
            Object result = Await.result(ask(fetchActor, new WechatAuthorizationFetchActor.CodeAndPlatform(code,
                    platform, !baseApi), ActorConstant.REMOTE_CALL_TIMEOUT), ActorConstant.REMOTE_CALL_DURATION);
            return (Map<String, String>) result;
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " Fetch error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<? extends BaseUntypedActor> getClz() {
        return WechatAuthorizationGetActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> saveClz() {
        return WechatAuthorizationSaveActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> deleteClz() {
        return WechatAuthorizationDeleteActor.class;
    }
}
