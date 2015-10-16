package com.zanshang.services;

/**
 * Created by Lookis on 6/4/15.
 */
public interface PhoneCaptchaTrapdoor {

    void create(String phone, String messageFormat);

    String get(String phone);

    void delete(String phone);
}
