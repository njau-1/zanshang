package com.zanshang.constants;

/**
 * Created by xuming on 15/8/14.
 */
public enum NotificationType {
    EMAIL("email"), SMS("sms"), WECHAT("wechat"), NOTIFICATION("notification"), ALL("all"), OFF("off");

    NotificationType(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }
}
