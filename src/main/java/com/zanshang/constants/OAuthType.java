package com.zanshang.constants;

/**
 * Created by xuming on 15/8/25.
 */
public enum OAuthType {
    WECHAT_MP("weichat_mp"), WEIBO("weibo"), WECHAT("wechat");

    OAuthType(String platform) {
        this.platform = platform;
    }
    private String platform;

    public String getPlatform() {
        return platform;
    }
}
