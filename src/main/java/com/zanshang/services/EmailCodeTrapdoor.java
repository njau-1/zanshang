package com.zanshang.services;

import java.util.Map;

/**
 * Created by Lookis on 6/4/15.
 */
public interface EmailCodeTrapdoor {

    void create(String email, String templateName, String subject, Map<String,String> model);
    void create(String email, String templateName, Map<String,String> model);

    String get(String email, String templateName);

    void delete(String email, String templateName);
}
