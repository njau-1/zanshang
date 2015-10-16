package com.zanshang.notify.config;

import com.zanshang.notify.MessageTemplateEngine;
import com.zanshang.notify.NotifyService;
import com.zanshang.notify.service.ConsoleService;
import com.zanshang.notify.service.EmailService;
import com.zanshang.notify.service.NotificationService;
import com.zanshang.notify.service.SMSService;
import com.zanshang.notify.template.FreeMarkerTemplateEngine;
import com.zanshang.notify.template.StringTemplateEngine;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuming on 15/9/7.
 */
@Configuration
public class MessageTemplateConfig implements ApplicationContextAware {

    final Map<Class<? extends NotifyService>, MessageTemplateEngine> params = new HashMap<>();

    ApplicationContext spring;

    @Autowired
    FreeMarkerTemplateEngine freeMarkerTemplate;

    public MessageTemplateConfig() {
    }

    public MessageTemplateEngine getTemplate(Class <? extends NotifyService> clazz) {
        return params.get(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        spring = applicationContext;
        params.put(ConsoleService.class, new StringTemplateEngine());
        params.put(NotificationService.class, freeMarkerTemplate);
        params.put(EmailService.class, freeMarkerTemplate);
        params.put(SMSService.class, freeMarkerTemplate);
    }
//    @Bean(name = "stringTemplate")
//    public StringTemplateEngine getStringTemplate() {
//        return new StringTemplateEngine();
//    }


}
