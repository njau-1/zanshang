package com.zanshang.notify.service;

import com.zanshang.constants.NotificationType;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Setting;
import com.zanshang.notify.MessageTemplateEngine;
import com.zanshang.notify.NotifyService;
import com.zanshang.notify.config.MessageTemplateConfig;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.notify.constants.NotifyParameter;
import com.zanshang.notify.constants.TemplateName;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Locale;
import java.util.Map;

/**
 * Created by xuming on 15/9/8.
 */
public class EmailService extends BaseUntypedActor implements NotifyService {

    public static final String DEFAULT_ENCODING = "utf-8";

    private static final Locale locale = Locale.CHINA;

    public String DEFAULT_FROM_ADDRESS;

    public String DEFAULT_SUBJECT;

    private JavaMailSender mailSender;

    MessageTemplateConfig config;

    MessageSource messageSource;

    private final static NotificationType NOTIFICATION_TYPE = NotificationType.EMAIL;

    public EmailService(ApplicationContext spring) {
        super(spring);
        config = spring.getBean(MessageTemplateConfig.class);
        messageSource = spring.getBean(MessageSource.class);

        DEFAULT_FROM_ADDRESS = getProperty("MAIL_DEFAULT_FROM");
        DEFAULT_SUBJECT = getProperty("MAIL_DEFAULT_SUBJECT");
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(getProperty("MAIL_SMTP_HOST") != null ? getProperty("MAIL_SMTP_HOST") : "localhost");
        mailSender.setPort(getProperty("MAIL_SMTP_PORT") != null ? Integer.parseInt(getProperty("MAIL_SMTP_PORT")) :
                25);
        mailSender.setUsername(getProperty("MAIL_SMTP_USERNAME"));
        mailSender.setPassword(getProperty("MAIL_SMTP_PASSWORD"));
        mailSender.setDefaultEncoding(DEFAULT_ENCODING);
        this.mailSender = mailSender;
    }

    @Override
    public void onReceive(Object o) throws Exception {
        NotifyParameter parameter = (NotifyParameter) o;
        send(parameter.getUid(), parameter.getNotifyBusinessType(), parameter.getModel());
    }

    @Override
    public void send(ObjectId uid, NotifyBusinessType notifyBusinessType, Map<String, String> model) {
        notifyBusinessType.setNotificationType(NOTIFICATION_TYPE);
        String message = getTemplate().render(notifyBusinessType.buildTemplateName(), model);
        Setting setting = getMongoTemplate().findById(uid, Setting.class);
        if (setting.getEmail() != null && !setting.getEmail().isEmpty()) {
            sendEmail(setting.getEmail(), message, notifyBusinessType);
        }
    }

    public void sendEmail(String mailto, String message, NotifyBusinessType notifyBusinessType) {
        Parameters parameters = getMailParameter(notifyBusinessType);
        String mailfrom = parameters == null || parameters.getFrom() == null ? DEFAULT_FROM_ADDRESS : parameters.getFrom();
        String subject = parameters == null || parameters.getSubject() == null ? DEFAULT_SUBJECT : parameters.getSubject();
        mailSender.send(mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, DEFAULT_ENCODING);
            mimeMessageHelper.setTo(mailto);
            mimeMessageHelper.setFrom(mailfrom);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message, true);
        });
        logger.info("Mail[" + notifyBusinessType.getTemplateName() + "] to:[" + mailto + "] sent");
    }

    private Parameters getMailParameter(NotifyBusinessType notifyBusinessType) {
        switch (notifyBusinessType) {
            case PROJECT_FUNDING_SUCCESS:return new Parameters(notifyBusinessType.getTemplateName());
            case SALON_NEW_TOPIC:return new Parameters(notifyBusinessType.getTemplateName());
            case PROJECT_PROGRESS_FIRST_WEEK:return new Parameters(notifyBusinessType.getTemplateName());
            case PROJECT_PROGRESS_HALF:return new Parameters(notifyBusinessType.getTemplateName());
            case PROJECT_PROGRESS_LAST_WEEK:return new Parameters(notifyBusinessType.getTemplateName());
            case WIT:return new Parameters(notifyBusinessType.getTemplateName());
            default: return null;
        }
    }

    private class Parameters {

        private String from;

        private String subject;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public Parameters(String from, String subject) {
            this.from = from;
            this.subject = subject;
        }

        public Parameters(String templateName) {
//            this.from = messageSource.getMessage(templateName.getTemplateName() + ".from", null, locale);
            this.from = DEFAULT_FROM_ADDRESS;
            this.subject = messageSource.getMessage(templateName + ".subject", null, locale);
        }
    }

    @Override
    public MessageTemplateEngine getTemplate() {
        return config.getTemplate(getClass());
    }
}
