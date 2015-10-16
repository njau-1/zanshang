package com.zanshang.captcha;

import com.qiniu.util.StringUtils;
import com.zanshang.services.PhoneCaptchaTrapdoor;
import com.zanshang.services.phone.PhoneCaptchaTrapdoorImpl;
import com.zanshang.utils.AkkaTrapdoor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Lookis on 9/29/15.
 */
@Service
public class PhoneCaptchaService implements com.zanshang.captcha.CaptchaService {

    Logger logger = LoggerFactory.getLogger(getClass());

    PhoneCaptchaTrapdoor phoneCaptchaTrapdoor;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @PostConstruct
    public void init() {
        phoneCaptchaTrapdoor = akkaTrapdoor.createTrapdoor(PhoneCaptchaTrapdoor.class, PhoneCaptchaTrapdoorImpl.class);
    }

    @Override
    public boolean verify(String key, String inputCode) throws CaptchaException {
        String code = phoneCaptchaTrapdoor.get(key);
        if (StringUtils.isNullOrEmpty(code)) {
            logger.error("Phone Verification Code Not exist." + key);
            throw new CaptchaException("Captcha Code not exist");
        } else {
            if (!code.equalsIgnoreCase(inputCode)) {
                return false;
            } else {
                //every thing is ok, continue
                phoneCaptchaTrapdoor.delete(key);
                return true;
            }
        }
    }
}
