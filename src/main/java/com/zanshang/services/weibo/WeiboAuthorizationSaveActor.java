package com.zanshang.services.weibo;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.zanshang.constants.Connect;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * <pre>
 * {
 * "access_token": "ACCESS_TOKEN",
 * "expires_in": 1234,
 * "remind_in":"798114",
 * "uid":"12341234"
 * }
 * </pre>
 * Created by Lookis on 6/7/15.
 */
public class WeiboAuthorizationSaveActor extends BaseUntypedActor {

    private ActorRef informationUpdateActor;

    public WeiboAuthorizationSaveActor(ApplicationContext spring) {
        super(spring);
        informationUpdateActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(WeiboInformationUpdateActor
                .class, spring)));
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Map<String, String> accessToken = (Map<String, String>) o;
        if (accessToken.get(WeiboAuthorizationTrapdoorImpl.ACCESS_TOKEN) != null) {
            informationUpdateActor.tell(accessToken, getSelf());
            accessToken.put("_id", accessToken.get(Connect.WEIBO_ID_IN_ACCESSTOKEN));
            getMongoTemplate().save(accessToken, WeiboAuthorizationTrapdoorImpl.COLLECTION_NAME);
        } else {
            logger.error("Try to save weibo accessToken without access token(code may be expired):" + accessToken);
        }
    }
}
