package com.zanshang.notify.constants;

/**
 * Created by xuming on 15/9/11.
 */
public enum FreeMarkerModelParamKey {
    PROJECTID("projectId"), UID("uid"), BOOKNAME("bookName"), AUTHOR("author"), FIRSTAUTHORDESCRIPTION("firstAuthorDescription"),
    DESCRIPTION("description"), SENDERUID("senderUid"), SENDERNAME("senderName"), DEADLINE("deadLine"), CURRENTBALANCE("currentBalance"), GOAL("goal"), COVER("cover");

    FreeMarkerModelParamKey(String key) {
        this.key = key;
    }

    private String key;

    public String getKey() {
        return key;
    }
}
