package com.zanshang.notify.service;

import akka.actor.ActorRef;
import com.zanshang.constants.NotificationType;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.notify.MessageTemplateEngine;
import com.zanshang.notify.NotifyService;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.notify.constants.NotifyParameter;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * Created by xuming on 15/9/8.
 */
public class CompositeService extends BaseUntypedActor implements NotifyService {

    final static NotificationType NOTIFICATION_TYPE = NotificationType.NOTIFICATION;

    List<ActorRef> actorRefs;

    public CompositeService(ApplicationContext spring, List<ActorRef> actorRefs) {
        super(spring);
        this.actorRefs = actorRefs;
    }
    public CompositeService(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        NotifyParameter parameter = (NotifyParameter) o;
        send(parameter.getUid(), parameter.getNotifyBusinessType(), parameter.getModel());
    }

    @Override
    public void send(ObjectId uid, NotifyBusinessType notifyBusinessType, Map<String, String> model) {
        for (ActorRef ns: actorRefs) {
            if (ns != null) {
                ns.tell(new NotifyParameter(uid, notifyBusinessType, model), getSelf());
            }
        }
    }

    @Override
    public MessageTemplateEngine getTemplate() {
        return null;
    }
}
