package com.zanshang.constants;

/**
 * Created by Lookis on 5/24/15.
 */
public enum BookType {
    STARTUP("booktype.startup"), INNOVATION("booktype.innovation"), CREATIVITY("booktype.creativity"),
    INTERNET("booktype.internet"), SCIENTIFIC("booktype.scientific"), EMOTION("booktype.emotion"),
    FEMALE("booktype.female"), PSYCHOLOGY("booktype.psychology"), CHILDREN("booktype.children"),
    HEALTH("booktype.health"), TRAVEL("booktype.travel"), URBAN("booktype.urban"),
    MUSIC("booktype.music"), FASHION("booktype.fashion"), FOOD("booktype.food"),
    ELITE("booktype.elite"), BIOGRAPHY("booktype.biography"), NOVEL("booktype.novel"),
    POETRY("booktype.poetry"), ESSAY("booktype.essay"), HISTORY("booktype.history"),
    THOUGHT("booktype.thought"), EDUCATION("booktype.education"), FINANCE("booktype.finance"),
    INVESTMENT("booktype.investment"), OTHER("booktype.other");

    BookType(String message) {
        this.message = message;
    }

    private String message;

    public String getMessageCode() {
        return message;
    }

}