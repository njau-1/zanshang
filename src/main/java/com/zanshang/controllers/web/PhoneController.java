package com.zanshang.controllers.web;

import com.zanshang.captcha.CaptchaException;
import com.zanshang.captcha.ImageCaptchaService;
import com.zanshang.captcha.NoneCaptchaService;
import com.zanshang.framework.Ticket;
import com.zanshang.services.PhoneCaptchaTrapdoor;
import com.zanshang.services.phone.PhoneCaptchaTrapdoorImpl;
import com.zanshang.utils.Ajax;
import com.zanshang.utils.AkkaTrapdoor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Lookis on 5/19/15.
 */
@Controller
@RequestMapping(value = "/phone")
public class PhoneController {

    PhoneCaptchaTrapdoor phoneService;

    @Autowired
    ImageCaptchaService imageCaptchaService;

    @Autowired
    NoneCaptchaService noneCaptchaService;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @PostConstruct
    public void init() {
        phoneService = akkaTrapdoor.createTrapdoor(PhoneCaptchaTrapdoor.class, PhoneCaptchaTrapdoorImpl.class);
    }

    @RequestMapping(value = "/verification", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> verification(@RequestParam("phone") String phone, @RequestParam("captcha") String
            captcha, @Ticket String ticket, Locale locale) {
        try {
            if (imageCaptchaService.verify(ticket, captcha)) {
                phoneService.create(phone, messageSource.getMessage("phone.verification", null, locale));
                return Ajax.ok();
            } else {
                return Ajax.failure(messageSource.getMessage("captcha.missing_or_error", null, locale));
            }
        } catch (CaptchaException e) {
            return Ajax.failure(messageSource.getMessage("captcha.missing_or_error", null, locale));
        }
    }

    //FIXME: remove none verify
    @RequestMapping(value = "/verification1", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> verification_none(@RequestParam("phone") String phone, @RequestParam(value =
            "captcha", required = false) String captcha, @Ticket String ticket, Locale locale) {
        try {
            if (noneCaptchaService.verify(ticket, captcha)) {
                phoneService.create(phone, messageSource.getMessage("phone.verification", null, locale));
                return Ajax.ok();
            } else {
                return Ajax.failure(messageSource.getMessage("captcha.missing_or_error", null, locale));
            }
        } catch (CaptchaException e) {
            return Ajax.failure(messageSource.getMessage("captcha.missing_or_error", null, locale));
        }
    }
}
