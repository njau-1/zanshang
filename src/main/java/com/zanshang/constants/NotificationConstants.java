package com.zanshang.constants;

/**
 * Created by xuming on 15/8/19.
 */
public enum NotificationConstants {

    PROJECT_CREATE_SUCCESS("project_create_success", new ParamKey[] {ParamKey.BOOKNAME, ParamKey.PROJECTID}),
    PROJECT_FUNDING_SUCCESS("project_funding_success", new ParamKey[] {ParamKey.BOOKNAME, ParamKey.PROJECTID}),
    SALON_NEW_CHAT_AUTHOR("salon_new_chat_author", new ParamKey[] {ParamKey.BOOKNAME, ParamKey.UID, ParamKey.AUTHOR}),
    SALON_NEW_CHAT_MEMBER("salon_new_chat_member", new ParamKey[] {ParamKey.BOOKNAME, ParamKey.UID, ParamKey.SENDERNAME}),
    SALON_NEW_TOPIC("salon_new_topic", new ParamKey[] {ParamKey.BOOKNAME, ParamKey.UID, ParamKey.AUTHOR}),
    PROJECT_PROGRESS_HALF("project_progress_half", new ParamKey[] {ParamKey.BOOKNAME, ParamKey.PROJECTID, ParamKey.DEADLINE}),
    PROJECT_PROGRESS_FIRST_WEEK("project_progress_first_week", new ParamKey[] {ParamKey.BOOKNAME, ParamKey.PROJECTID, ParamKey.DEADLINE}),
    PROJECT_PROGRESS_LAST_WEEK("project_progress_last_week", new ParamKey[] {ParamKey.BOOKNAME, ParamKey.PROJECTID, ParamKey.DEADLINE}),
    PERSONAL_NEW_CHAT("personal_new_chat", new ParamKey[] {ParamKey.SENDERNAME, ParamKey.SENDERUID});

    NotificationConstants(String templateName, ParamKey[] paramArray) {
        this.templateName = templateName;
        this.paramArray = paramArray;
    }
    private String templateName;

    private ParamKey[] paramArray;

    public String getTemplateName() {
        return templateName;
    }

    public ParamKey[] getParamArray() {
        return paramArray;
    }

    public enum ParamKey {
        PROJECTID("projectId"), UID("uid"), BOOKNAME("bookName"), AUTHOR("author"), FIRSTAUTHORDESCRIPTION("firstAuthorDescription"),
        DESCRIPTION("description"), SENDERUID("senderUid"), SENDERNAME("senderName"), DEADLINE("deadline");

        ParamKey(String key) {
            this.key = key;
        }

        private String key;

        public String getKey() {
            return key;
        }
    }
}
