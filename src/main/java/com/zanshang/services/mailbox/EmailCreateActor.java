package com.zanshang.services.mailbox;

import com.zanshang.framework.BaseUntypedActor;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import java.util.Map;

/**
 * Created by Lookis on 6/19/15.
 */
public class EmailCreateActor extends BaseUntypedActor {

    public static final String DEFAULT_ENCODING = "utf-8";

    public String DEFAULT_FROM_ADDRESS;

    public String DEFAULT_SUBJECT;

    private JavaMailSender mailSender;

    private Configuration templateConfiguration;

    public EmailCreateActor(ApplicationContext spring) {
        super(spring);
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

        Configuration conf = new Configuration(Configuration.VERSION_2_3_22);
        conf.setDefaultEncoding(DEFAULT_ENCODING);
        conf.setLocalizedLookup(true);
        conf.setTemplateLoader(new SpringTemplateLoader(getSpringResoueceLoader(), "/WEB-INF/mails/"));
        this.templateConfiguration = conf;
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Parameters parameters = (Parameters) o;
        String mailto = parameters.getTo();
        String templateName = parameters.getTemplate();
        String mailfrom = parameters.getFrom() == null ? DEFAULT_FROM_ADDRESS : parameters.getFrom();
        String subject = parameters.getSubject() == null ? DEFAULT_SUBJECT : parameters.getSubject();
        Template template;
        if (templateName.endsWith(".ftl")) {
            template = templateConfiguration.getTemplate(templateName);
        } else {
            template = templateConfiguration.getTemplate(templateName + ".ftl");
        }
        Map<String, String> model = parameters.getModel();
        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        //System.out.println(mailContent);
        mailSender.send(mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, DEFAULT_ENCODING);
            mimeMessageHelper.setTo(mailto);
            mimeMessageHelper.setFrom(mailfrom);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(mailContent, true);
        });
        logger.info("Mail[" + templateName + "] to:[" + mailto + "] sent");
    }

    static class Parameters {

        private String to;

        private String from;

        private String subject;

        private String template;

        private Map<String, String> model;

        public Parameters(String to, String template, Map<String, String> model) {
            this(to, null, null, template, model);
        }

        public Parameters(String to, String from, String subject, String template, Map<String, String> model) {
            this.to = to;
            this.from = from;
            this.subject = subject;
            this.template = template;
            this.model = model;
        }

        public String getTo() {
            return to;
        }

        public String getFrom() {
            return from;
        }

        public String getSubject() {
            return subject;
        }

        public String getTemplate() {
            return template;
        }

        public Map<String, String> getModel() {
            return model;
        }
    }
}
