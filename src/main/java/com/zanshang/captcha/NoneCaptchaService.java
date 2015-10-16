package com.zanshang.captcha;

import org.springframework.stereotype.Service;

/**
 * Created by Lookis on 9/29/15.
 */
@Service
public class NoneCaptchaService implements CaptchaService {

    @Override
    public boolean verify(String key, String inputCode) throws CaptchaException {
        return true;
    }
}
