package com.zanshang.controllers.web;

import com.zanshang.constants.EmailConstants;
import com.zanshang.framework.spring.MongodbUserDetailsManager;
import com.zanshang.models.EmailAccount;
import com.zanshang.services.EmailCodeTrapdoor;
import com.zanshang.services.PhoneCaptchaTrapdoor;
import com.zanshang.services.mailbox.EmailCodeTrapdoorImpl;
import com.zanshang.services.phone.PhoneCaptchaTrapdoorImpl;
import com.zanshang.utils.AkkaTrapdoor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Lookis on 5/19/15.
 */
@Controller
@RequestMapping(value = "/account")
public class AccountController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MessageSource messageSource;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    MongodbUserDetailsManager userDetailsManager;

    PhoneCaptchaTrapdoor phoneService;

    EmailCodeTrapdoor emailService;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @PostConstruct
    protected void init() {
        emailService = akkaTrapdoor.createTrapdoor(EmailCodeTrapdoor.class, EmailCodeTrapdoorImpl.class);
        phoneService = akkaTrapdoor.createTrapdoor(PhoneCaptchaTrapdoor.class, PhoneCaptchaTrapdoorImpl.class);
    }

    @RequestMapping(value = "/mails/activation", method = RequestMethod.GET)
    public Object getMailActivate(@RequestParam("email") String email, Locale locale) {
        Map<String, String> mailModel = new HashMap<>();
        mailModel.put("email", email);
        emailService.create(email, EmailConstants.EMAIL_ACTIVE_TEMPLATENAME, messageSource
                .getMessage("email.title.activation", null, locale), mailModel);
        ModelAndView mav = new ModelAndView("return");
        mav.addObject("title", messageSource.getMessage("email.activation.resend.title", null, locale));
        mav.addObject("content", messageSource.getMessage("email.activation.resend.content", null, locale));
        return mav;
    }

    /**
     * 注册激活，没激活前账号已经生成，只是不可登陆，所以这里只是真激活一下Account
     *
     * @param email
     * @param codeInput
     * @return
     */
    @RequestMapping(value = "/mails/activate", method = RequestMethod.GET)
    public Object mailActivate(@RequestParam("email") String email, @RequestParam("code") String codeInput, Locale
            locale) {
        EmailAccount emailAccount = mongoTemplate.findById(email, EmailAccount.class);
        if (emailAccount == null || emailAccount.isEnabled()) {
            return "redirect:/";
        }
        String code = emailService.get(email, EmailConstants.EMAIL_ACTIVE_TEMPLATENAME);
        if (StringUtils.isEmpty(code)) {
            logger.error("Email Verification Code Not exist." + email);
            ModelAndView mav = new ModelAndView("return");
            mav.addObject("title", messageSource.getMessage("return.title.error", null, locale));
            mav.addObject("content", messageSource.getMessage("return.content.email.error", null, locale));
            return mav;
        } else {
            if (!codeInput.equalsIgnoreCase(code)) {
                ModelAndView mav = new ModelAndView("return");
                mav.addObject("title", messageSource.getMessage("return.title.error", null, locale));
                mav.addObject("content", messageSource.getMessage("return.content.email.error", null, locale));
                return mav;
            } else {
                //every thing is ok, continue
                emailService.delete(email, EmailConstants.EMAIL_UPDATE_TEMPLATENAME);
                emailAccount.setEnabled(true);
                userDetailsManager.updateUser(emailAccount);
                ModelAndView mav = new ModelAndView("return");
                mav.addObject("title", messageSource.getMessage("return.title.ok", null, locale));
                mav.addObject("content", messageSource.getMessage("return.content.email.activated", null, locale));
                return mav;
            }
        }
    }
}
