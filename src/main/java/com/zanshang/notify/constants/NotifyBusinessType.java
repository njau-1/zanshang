package com.zanshang.notify.constants;

import com.zanshang.constants.NotificationType;

/**
 * Created by xuming on 15/9/7.
 */
public enum NotifyBusinessType {

    DEMO("helloWorldTemplate"),
    HelloWorldTemplate("helloWorldTemplate"),
    PROJECT_CREATE_SUCCESS("project_create_success"),
    PROJECT_FUNDING_SUCCESS("project_funding_success"),
    SALON_NEW_CHAT_AUTHOR("salon_new_chat_author"),
    SALON_NEW_CHAT_MEMBER("salon_new_chat_member"),
    SALON_NEW_TOPIC("salon_new_topic"),
    PROJECT_PROGRESS_HALF("project_progress_half"),
    PROJECT_PROGRESS_FIRST_WEEK("project_progress_first_week"),
    PROJECT_PROGRESS_LAST_WEEK("project_progress_last_week"),
    PERSONAL_NEW_CHAT("personal_new_chat"),
    PHONE_NOLOGIN_REGIST("phone_nologin_regist"),
    WIT("wit_payment");

    NotifyBusinessType(String templateName) {
        this.templateName = templateName;
    }

    String templateName;

    NotificationType notificationType;

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String buildTemplateName() {
        if (notificationType != null) {
            return notificationType.getType() + "/" + templateName;
        }
        return templateName;
    }
}
