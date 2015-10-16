package com.zanshang.notify.service;

import akka.actor.ActorRef;
import com.zanshang.constants.NotificationType;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Notification;
import com.zanshang.notify.MessageTemplateEngine;
import com.zanshang.notify.NotifyService;
import com.zanshang.notify.config.MessageTemplateConfig;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.notify.constants.NotifyParameter;
import com.zanshang.notify.constants.TemplateName;
import com.zanshang.services.notification.NotificationSaveActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Created by xuming on 15/9/8.
 */
public class NotificationService extends BaseUntypedActor implements NotifyService {

    final static NotificationType NOTIFICATION_TYPE = NotificationType.NOTIFICATION;

    MessageTemplateConfig config;

    ActorRef notificationSaveActor;

    public NotificationService(ApplicationContext spring) {
        super(spring);
        config = spring.getBean(MessageTemplateConfig.class);
        notificationSaveActor = AkkaTrapdoor.create(getContext(), NotificationSaveActor.class, spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        NotifyParameter parameter = (NotifyParameter) o;
        send(parameter.getUid(), parameter.getNotifyBusinessType(), parameter.getModel());
    }

    @Override
    public void send(ObjectId uid, NotifyBusinessType notifyBusinessType, Map<String, String> model) {
        notifyBusinessType.setNotificationType(NOTIFICATION_TYPE);
        notificationSaveActor.tell(new Notification(uid, notifyBusinessType.buildTemplateName(), model), ActorRef.noSender());
    }

    @Override
    public MessageTemplateEngine getTemplate() {
        return config.getTemplate(getClass());
    }
}
