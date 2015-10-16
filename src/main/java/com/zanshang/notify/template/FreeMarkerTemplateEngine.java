package com.zanshang.notify.template;

import com.zanshang.notify.MessageTemplateEngine;
import com.zanshang.notify.constants.TemplateName;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import java.io.IOException;
import java.util.Map;

/**
 * Created by xuming on 15/9/8.
 */
@Component
public class FreeMarkerTemplateEngine implements MessageTemplateEngine, ApplicationContextAware{

    Logger logger = LoggerFactory.getLogger(getClass());

    public static final String DEFAULT_ENCODING = "utf-8";

    private Configuration templateConfiguration;

    ApplicationContext spring;

    @Override
    public String render(String templateName, Map<String, String> model) {
        String notificationContent = "";
        try {
            Template template;
            if (templateName.endsWith(".ftl")) {
                template = templateConfiguration.getTemplate(templateName);
            } else {
                template = templateConfiguration.getTemplate(templateName + ".ftl");
            }
            notificationContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                logger.error("Template IOException...", e);
            }
        } catch (TemplateException e) {
            if (logger.isDebugEnabled()) {
                logger.error("Template TemplateException...",e);
            }
        }
        return notificationContent;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.spring = applicationContext;
        Configuration conf = new Configuration(Configuration.VERSION_2_3_22);
        conf.setDefaultEncoding(DEFAULT_ENCODING);
        conf.setLocalizedLookup(true);
        conf.setTemplateLoader(new SpringTemplateLoader(spring, "/WEB-INF/notification/"));
        this.templateConfiguration = conf;
    }
}
