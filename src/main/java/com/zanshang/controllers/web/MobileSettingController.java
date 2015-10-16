package com.zanshang.controllers.web;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.constants.EmailConstants;
import com.zanshang.framework.spring.MongodbUserDetailsManager;
import com.zanshang.models.*;
import com.zanshang.services.EmailCodeTrapdoor;
import com.zanshang.services.PersonTrapdoor;
import com.zanshang.services.PhoneCaptchaTrapdoor;
import com.zanshang.services.SettingTrapdoor;
import com.zanshang.services.mailbox.EmailCodeTrapdoorImpl;
import com.zanshang.services.person.PersonTrapdoorImpl;
import com.zanshang.services.phone.PhoneCaptchaTrapdoorImpl;
import com.zanshang.services.setting.SettingTrapdoorImpl;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.PhoneValidator;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by xuming on 15/8/18.
 */
@Controller
@RequestMapping(MobileSettingController.MOBILE_SETTINGS_CONTROLLER_PATH)
public class MobileSettingController {

    public static final String MOBILE_SETTINGS_CONTROLLER_PATH = "/mobile/settings";

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @Autowired
    MongodbUserDetailsManager userDetailsManager;

    @Autowired
    MessageSource messageSource;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    PasswordEncoder encoder;

    EmailCodeTrapdoor emailService;

    SettingTrapdoor settingService;

    PersonTrapdoor personService;

    PhoneCaptchaTrapdoor phoneService;

    @PostConstruct
    public void initActor() {
        settingService = akkaTrapdoor.createTrapdoor(SettingTrapdoor.class, SettingTrapdoorImpl.class);
        emailService = akkaTrapdoor.createTrapdoor(EmailCodeTrapdoor.class, EmailCodeTrapdoorImpl.class);
        personService = akkaTrapdoor.createTrapdoor(PersonTrapdoor.class, PersonTrapdoorImpl.class);
        phoneService = akkaTrapdoor.createTrapdoor(PhoneCaptchaTrapdoor.class, PhoneCaptchaTrapdoorImpl.class);
    }

    @RequestMapping(value = "/name", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Object name() {
        return "7_1_1";
    }

    @RequestMapping(value = "/name", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public Object saveDisplayName(@RequestParam("name") String displayName, Principal principal) {
        Setting setting = settingService.get(new ObjectId(principal.getName()));
        setting.setDisplayName(displayName);
        settingService.save(setting);
        return "redirect:" + SettingController.SETTINGS_CONTROLLER_PATH;
    }


    @RequestMapping(value = "/email", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Object email() {
        return "7_1_2";
    }

    //发送邮件
    @RequestMapping(value = "/mails/activation", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public Object sendActivateMail(@RequestParam("email") String newEmail, Principal principal, Locale locale) {
        if (userDetailsManager.userExists(newEmail)) {
            ModelAndView mav = new ModelAndView("7_1_2");
            mav.addObject("error", messageSource.getMessage("setting.email.duplicate", null, locale));
            return mav;
        } else {
            Cache cache = cacheManager.getCache(CacheConfig.CACHE_NAME_EMAIL_VERIFICATION_MATCHED_EMAIL);
            cache.put(principal.getName(), newEmail);
            Map<String, String> mailModel = new HashMap<>();
            mailModel.put("email", newEmail);
            emailService.create(newEmail, EmailConstants.EMAIL_UPDATE_TEMPLATENAME, messageSource.getMessage("email"
                    + ".title.updateemail", null, locale), mailModel);
            return "redirect:" + SettingController.SETTINGS_CONTROLLER_PATH;
        }
    }

    @RequestMapping(value = "/phone", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Object phone() {
        return "7_1_3";
    }

    @RequestMapping(value = "/phone", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public Object savePhone(@RequestParam("phone") String phone, @RequestParam("code") String codeInput, Principal
            principal, Locale locale) {
        ModelAndView mav = new ModelAndView("7_1_3");
        if (!PhoneValidator.isValid(phone)) {
            mav.addObject("error", messageSource.getMessage("setting.person.phone.format_error", null, locale));
            return mav;
        }
        Person person = personService.get(new ObjectId(principal.getName()));
        if (person == null) {
            mav.addObject("error", messageSource.getMessage("setting.person.notexist", null, locale));
            return mav;
        }
        //verify code
        String code = phoneService.get(phone);
        if (StringUtils.isEmpty(code)) {
            logger.error("Phone Verification Code Not exist." + phone);
            mav.addObject("error", messageSource.getMessage("setting.person.phone.error_expire", null, locale));
            return mav;
        } else {
            if (!code.equalsIgnoreCase(codeInput)) {
                mav.addObject("error", messageSource.getMessage("setting.person.phone.error_expire", null, locale));
                return mav;
            } else {
                //every thing is ok, continue
                phoneService.delete(phone);
                PhoneAccount phoneAccount = null;
                if (person.getPhone() != null) {
                    phoneAccount = mongoTemplate.findById(person.getPhone(), PhoneAccount.class);
                }
                PhoneAccount newAccount;
                if (phoneAccount != null) {
                    //两种情况：1 原手机号是自己的，2. 原手机号不是自己的， 无论哪种情况都把原来的号清掉，给新的用户
                    newAccount = new PhoneAccount(new ObjectId(principal.getName()), phoneAccount.getUserAuthorities
                            (), phoneAccount.getEPPassword(), phone, phoneAccount.isAccountNonExpired(), phoneAccount
                            .isAccountNonLocked(), phoneAccount.isCredentialsNonExpired(), phoneAccount.isEnabled());

                    mongoTemplate.remove(phoneAccount);
                } else {
                    Setting setting = settingService.get(new ObjectId(principal.getName()));
                    EmailAccount emailAccount = mongoTemplate.findById(setting.getEmail(), EmailAccount.class);
                    newAccount = new PhoneAccount(emailAccount.getUid(), emailAccount.getUserAuthorities(),
                            emailAccount.getEPPassword(), phone, emailAccount.isAccountNonExpired(), emailAccount
                            .isAccountNonLocked(), emailAccount.isCredentialsNonExpired(), emailAccount.isEnabled());
                }
                mongoTemplate.save(newAccount);
                person.setPhone(phone);
                personService.save(person);
                return "redirect:" + SettingController.SETTINGS_CONTROLLER_PATH;
            }
        }
    }

    @RequestMapping(value = "/password", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Object password() {
        return "7_1_4";
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public Object savePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("password") String
            rawPassword, Principal principal, Locale locale) {
        EPPassword password = mongoTemplate.findById(new ObjectId(principal.getName()), EPPassword.class);
        String originPassword = password.getPassword();
        if (encoder.matches(oldPassword, originPassword)) {
            password.setPassword(encoder.encode(rawPassword));
            mongoTemplate.save(password);
            return "redirect:" + SettingController.SETTINGS_CONTROLLER_PATH;
        } else {
            ModelAndView mav = new ModelAndView("7_1_4");
            mav.addObject("error", messageSource.getMessage("setting.password.notmatch", null, locale));
            return mav;
        }
    }
}
