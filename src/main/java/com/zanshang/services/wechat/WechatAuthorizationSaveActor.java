package com.zanshang.services.wechat;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.zanshang.constants.Connect;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * <pre>
 *     {
 *      "access_token":"ACCESS_TOKEN",
 *       "expires_in":7200,
 *       "refresh_token":"REFRESH_TOKEN",
 *       "openid":"OPENID",
 *       "scope":"SCOPE",
 *       "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
 *      }
 * </pre>
 * Created by Lookis on 6/7/15.
 */
public class WechatAuthorizationSaveActor extends BaseUntypedActor {

    private ActorRef informationUpdateActor;

    public WechatAuthorizationSaveActor(ApplicationContext spring) {
        super(spring);
        informationUpdateActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(WechatInformationUpdateActor
                .class, spring)));
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Map<String, String> accessToken = (Map<String, String>) o;
        if (accessToken.get(Connect.WECHAT_ID_IN_ACCESSTOKEN) != null) {
            informationUpdateActor.tell(accessToken, getSelf());
            accessToken.put("_id", accessToken.get(Connect.WECHAT_ID_IN_ACCESSTOKEN));
            getMongoTemplate().save(accessToken, WechatAuthorizationTrapdoorImpl.COLLECTION_NAME);
        } else {
            logger.error("Try to save wechat accessToken without accessToken(code may be expired):" + accessToken);
        }
    }
}
