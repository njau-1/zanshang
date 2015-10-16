package com.zanshang.notify.service;

import com.zanshang.constants.NotificationType;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.notify.MessageTemplateEngine;
import com.zanshang.notify.NotifyService;
import com.zanshang.notify.config.MessageTemplateConfig;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.notify.constants.NotifyParameter;
import com.zanshang.notify.constants.TemplateName;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Created by xuming on 15/9/7.
 */
public class ConsoleService extends BaseUntypedActor implements NotifyService {

    MessageTemplateConfig config;

    private final static NotificationType NOTIFICATION_TYPE = NotificationType.SMS;

    public ConsoleService(ApplicationContext spring) {
        super(spring);
        config = spring.getBean(MessageTemplateConfig.class);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        NotifyParameter parameter = (NotifyParameter) o;
        send(parameter.getUid(), parameter.getNotifyBusinessType(), parameter.getModel());
    }

    @Override
    public void send(ObjectId uid, NotifyBusinessType notifyBusinessType, Map<String, String> model) {
        System.out.println("*****************************************\n\n\n\n\n\n");
        notifyBusinessType.setNotificationType(NOTIFICATION_TYPE);
        String message = getTemplate().render(notifyBusinessType.getTemplateName(), model);
        System.out.println(message);
        System.out.println(getTemplate());
        System.out.println("\n\n\n\n\n\n*****************************************");
    }

    @Override
    public MessageTemplateEngine getTemplate() {
        return config.getTemplate(getClass());
    }
}
