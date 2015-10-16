package com.zanshang.notify;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.zanshang.notify.config.NotifyConfig;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.notify.constants.NotifyParameter;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by xuming on 15/9/7.
 */
@Service
public class Notifier implements ApplicationContextAware{

    @Autowired
    NotifyConfig notifyConfig;

    ApplicationContext spring;

    public void notify(ObjectId uid, NotifyBusinessType t, Map<String, String> model) {
        ActorRef notifyService = notifyConfig.getNotifyService(t, spring);
        notifyService.tell(new NotifyParameter(uid, t, model), ActorRef.noSender());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        spring = applicationContext;
    }
}
