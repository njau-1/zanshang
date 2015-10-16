package com.zanshang.controllers.web;

import com.zanshang.captcha.CaptchaException;
import com.zanshang.captcha.PhoneCaptchaService;
import com.zanshang.constants.Connect;
import com.zanshang.constants.WechatPlatform;
import com.zanshang.framework.CodeExpireException;
import com.zanshang.models.EmailAccount;
import com.zanshang.models.Person;
import com.zanshang.models.PhoneAccount;
import com.zanshang.models.Setting;
import com.zanshang.services.*;
import com.zanshang.services.author.AuthorTrapdoorImpl;
import com.zanshang.services.person.PersonTrapdoorImpl;
import com.zanshang.services.phone.PhoneCaptchaTrapdoorImpl;
import com.zanshang.services.setting.SettingTrapdoorImpl;
import com.zanshang.services.wechat.WechatAuthorizationTrapdoorImpl;
import com.zanshang.services.weibo.WeiboAuthorizationTrapdoorImpl;
import com.zanshang.utils.Ajax;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.PhoneValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Lookis on 7/5/15.
 */
@Controller
@RequestMapping("/settings/personal")
public class SettingPersonalController {

    public static final String SETTINGS_CONTROLLER_PATH = "/settings/person";

    public static final String WEIBO_CALLBACK = "/weibo";

    public static final String WECHAT_CALLBACK = "/wechat";

    protected final Log logger = LogFactory.getLog(this.getClass());

    PhoneCaptchaTrapdoor phoneService;

    PersonTrapdoor personService;

    AuthorTrapdoor authorService;

    SettingTrapdoor settingService;

    WechatAuthorizationTrapdoor wechatAuthorizationService;

    WeiboAuthorizationTrapdoor weiboAuthorizationService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @Autowired
    PhoneCaptchaService captchaService;

    @PostConstruct
    public void initActor() {
        phoneService = akkaTrapdoor.createTrapdoor(PhoneCaptchaTrapdoor.class, PhoneCaptchaTrapdoorImpl.class);
        personService = akkaTrapdoor.createTrapdoor(PersonTrapdoor.class, PersonTrapdoorImpl.class);
        authorService = akkaTrapdoor.createTrapdoor(AuthorTrapdoor.class, AuthorTrapdoorImpl.class);
        settingService = akkaTrapdoor.createTrapdoor(SettingTrapdoor.class, SettingTrapdoorImpl.class);
        wechatAuthorizationService = akkaTrapdoor.createTrapdoor(WechatAuthorizationTrapdoor.class,
                WechatAuthorizationTrapdoorImpl.class);
        weiboAuthorizationService = akkaTrapdoor.createTrapdoor(WeiboAuthorizationTrapdoor.class,
                WeiboAuthorizationTrapdoorImpl.class);
    }

    @RequestMapping(value = "/legalname", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object saveLegalName(@RequestParam("name") String legalName, Principal principal, Locale locale) {
        if (authorService.isVerified(new ObjectId(principal.getName()))) {
            return Ajax.failure(messageSource.getMessage("setting.author.verified", null, locale));
        } else {
            Person person = personService.get(new ObjectId(principal.getName()));
            if (person == null) {
                return Ajax.failure(messageSource.getMessage("setting.person.notexist", null, locale));
            } else {
                person.setLegalName(legalName);
                personService.save(person);
                return Ajax.ok();
            }
        }
    }

    @RequestMapping(value = "/identity", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object saveIdentityCode(@RequestParam("identity") String identity, Principal principal, Locale locale) {
        if (authorService.isVerified(new ObjectId(principal.getName()))) {
            return Ajax.failure(messageSource.getMessage("setting.author.verified", null, locale));
        } else {
            Person person = personService.get(new ObjectId(principal.getName()));
            if (person == null) {
                return Ajax.failure(messageSource.getMessage("setting.person.notexist", null, locale));
            } else {
                person.setIdentityCode(identity);
                personService.save(person);
                return Ajax.ok();
            }
        }
    }

    @RequestMapping(value = "/identity/front", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object saveIdentityFront(@RequestParam("image") String image, Principal principal, Locale locale) {
        if (authorService.isVerified(new ObjectId(principal.getName()))) {
            return Ajax.failure(messageSource.getMessage("setting.author.verified", null, locale));
        } else {
            Person person = personService.get(new ObjectId(principal.getName()));
            if (person == null) {
                return Ajax.failure(messageSource.getMessage("setting.person.notexist", null, locale));
            } else {
                person.setIdentityFront(image);
                personService.save(person);
                return Ajax.ok();
            }
        }
    }

    @RequestMapping(value = "/identity/back", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object saveIdentityBack(@RequestParam("image") String image, Principal principal, Locale locale) {
        if (authorService.isVerified(new ObjectId(principal.getName()))) {
            return Ajax.failure(messageSource.getMessage("setting.author.verified", null, locale));
        } else {
            Person person = personService.get(new ObjectId(principal.getName()));
            if (person == null) {
                return Ajax.failure(messageSource.getMessage("setting.person.notexist", null, locale));
            } else {
                person.setIdentityBack(image);
                personService.save(person);
                return Ajax.ok();
            }
        }
    }

    @RequestMapping(value = "/phone", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object savePhone(@RequestParam("phone") String phone, @RequestParam("code") String codeInput, Principal
            principal, Locale locale) {
        if (!PhoneValidator.isValid(phone)) {
            return Ajax.failure(messageSource.getMessage("setting.person.phone.format_error", null, locale));
        }
        Person person = personService.get(new ObjectId(principal.getName()));
        if (person == null) {
            return Ajax.failure(messageSource.getMessage("setting.person.notexist", null, locale));
        }

        try {
            if (captchaService.verify(phone, codeInput)) {
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
                return Ajax.ok();
            } else {
                return Ajax.failure(messageSource.getMessage("setting.person.phone.error_expire", null, locale));
            }
        } catch (CaptchaException e) {
            return Ajax.failure(messageSource.getMessage("setting.person.phone.error_expire", null, locale));
        }
    }

    @RequestMapping(value = "/qq", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object saveQQ(Principal principal, @RequestParam("qq") Long qq, Locale locale) {
        Person person = personService.get(new ObjectId(principal.getName()));
        if (person == null) {
            return Ajax.failure(messageSource.getMessage("setting.person.notexist", null, locale));
        } else {
            person.setQq(qq);
            personService.save(person);
            return Ajax.ok();
        }
    }

    //这个链接是从weibo的callback
    @RequestMapping(value = WEIBO_CALLBACK, method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Object saveWeibo(@RequestParam("code") String code, Principal principal, HttpServletRequest request) {
        Person person = personService.get(new ObjectId(principal.getName()));
        Map<String, String> foreignUser = weiboAuthorizationService.fetchAndSave(code, request.getContextPath() +
                SETTINGS_CONTROLLER_PATH +
                WEIBO_CALLBACK);
        if (foreignUser != null) {
            person.setWeiboId(foreignUser.get(Connect.WEIBO_ID_IN_ACCESSTOKEN));
            personService.save(person);
        }
        return "redirect:" + SETTINGS_CONTROLLER_PATH;
    }

    @RequestMapping(value = "/weibo", method = RequestMethod.DELETE)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object deleteWeibo(Principal principal, Locale locale) {
        Person person = personService.get(new ObjectId(principal.getName()));
        if (person != null) {
            weiboAuthorizationService.delete(person.getWeiboId());
            person.setWeiboId(null);
            personService.save(person);
            return Ajax.ok();
        } else {
            return Ajax.failure(messageSource.getMessage("setting.company.remove_connect", null, locale));
        }
    }

    //这个链接是从weixin的callback
    @RequestMapping(value = WECHAT_CALLBACK, method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Object saveWechat(@RequestParam("code") String code, Principal principal) {
        Person person = personService.get(new ObjectId(principal.getName()));
        try {
            Map<String, String> foreignUser = wechatAuthorizationService.fetchAndSave(code, WechatPlatform.OPEN, false);
            if (foreignUser != null) {
                person.setWechatId(foreignUser.get(Connect.WECHAT_ID_IN_ACCESSTOKEN));
                personService.save(person);
            }
        } catch (CodeExpireException e) {
            logger.debug("contains no accesstoken");
        }
        return "redirect:" + SETTINGS_CONTROLLER_PATH;
    }

    @RequestMapping(value = "/wechat", method = RequestMethod.DELETE)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object deleteWeixin(Principal principal, Locale locale) {
        Person person = personService.get(new ObjectId(principal.getName()));
        if (person != null) {
            wechatAuthorizationService.delete(person.getWeiboId());
            person.setWechatId(null);
            personService.save(person);
            return Ajax.ok();
        } else {
            return Ajax.failure(messageSource.getMessage("setting.company.remove_connect", null, locale));
        }
    }
}
