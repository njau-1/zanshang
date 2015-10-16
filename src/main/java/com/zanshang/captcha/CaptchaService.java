package com.zanshang.captcha;

/**
 * Created by Lookis on 9/29/15.
 */
public interface CaptchaService {

    boolean verify(String key, String inputCode) throws CaptchaException;
}
