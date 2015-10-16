package com.zanshang.controllers.web;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.constants.EmailConstants;
import com.zanshang.framework.spring.MongodbUserDetailsManager;
import com.zanshang.models.*;
import com.zanshang.services.*;
import com.zanshang.services.address.AddressTrapdoorImpl;
import com.zanshang.services.author.AuthorTrapdoorImpl;
import com.zanshang.services.company.CompanyTrapdoorImpl;
import com.zanshang.services.mailbox.EmailCodeTrapdoorImpl;
import com.zanshang.services.person.PersonTrapdoorImpl;
import com.zanshang.services.publisher.PublisherTrapdoorImpl;
import com.zanshang.services.setting.SettingTrapdoorImpl;
import com.zanshang.services.wechat.WechatInformationTrapdoorImpl;
import com.zanshang.services.weibo.WeiboInformationTrapdoorImpl;
import com.zanshang.utils.Ajax;
import com.zanshang.utils.AkkaTrapdoor;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Lookis on 7/5/15.
 */
@Controller
@RequestMapping(SettingController.SETTINGS_CONTROLLER_PATH)
public class SettingController {

    public static final String SETTINGS_CONTROLLER_PATH = "/settings";

    @Value("${WECHAT_APPID}")
    String WECHAT_APPID;

    @Value("${WEIBO_APPID}")
    String WEIBO_APPID;

    @Value("${SERVER_CONTEXT}")
    String SERVER_CONTEXT;

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

    CompanyTrapdoor companyService;

    PersonTrapdoor personService;

    PublisherTrapdoor publisherService;

    AddressTrapdoor addressService;

    WechatInformationTrapdoor weixinService;

    WeiboInformationTrapdoor weiboService;

    AuthorTrapdoor authorService;

    @PostConstruct
    public void initActor() {
        settingService = akkaTrapdoor.createTrapdoor(SettingTrapdoor.class, SettingTrapdoorImpl.class);
        companyService = akkaTrapdoor.createTrapdoor(CompanyTrapdoor.class, CompanyTrapdoorImpl.class);
        personService = akkaTrapdoor.createTrapdoor(PersonTrapdoor.class, PersonTrapdoorImpl.class);
        addressService = akkaTrapdoor.createTrapdoor(AddressTrapdoor.class, AddressTrapdoorImpl.class);
        emailService = akkaTrapdoor.createTrapdoor(EmailCodeTrapdoor.class, EmailCodeTrapdoorImpl.class);
        publisherService = akkaTrapdoor.createTrapdoor(PublisherTrapdoor.class, PublisherTrapdoorImpl.class);
        weixinService = akkaTrapdoor.createTrapdoor(WechatInformationTrapdoor.class, WechatInformationTrapdoorImpl
                .class);
        weiboService = akkaTrapdoor.createTrapdoor(WeiboInformationTrapdoor.class, WeiboInformationTrapdoorImpl.class);
        authorService = akkaTrapdoor.createTrapdoor(AuthorTrapdoor.class, AuthorTrapdoorImpl.class);
    }

    //设置页面
    @RequestMapping(method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Object settings(HttpServletRequest request, Principal principal) {
        ObjectId uid = new ObjectId(principal.getName());
        Setting setting = settingService.get(uid);

        List<Address> addressList = addressService.findByUid(uid);
        Person person = personService.get(uid);
        ModelAndView mav = new ModelAndView();
        mav.addObject("email", setting.getEmail());
        mav.addObject("displayName", setting.getDisplayName());
        mav.addObject("avatar", setting.getAvatar());
        mav.addObject("uid", setting.getUid());
        mav.addObject("addresses", addressList);
        if (person != null) {
            mav.addObject("verified", authorService.isVerified(uid));
            String weixinId = person.getWechatId();
            String weiboId = person.getWeiboId();
            mav.addObject("phone", person.getPhone());
            mav.addObject("qq", person.getQq());
            mav.addObject("legalName", person.getLegalName());
            mav.addObject("identityCode", person.getIdentityCode());
            mav.addObject("identityFront", person.getIdentityFront());
            mav.addObject("identityBack", person.getIdentityBack());
            if (weixinId != null) {
                WechatInformation wechatInformation = weixinService.get(weixinId);
                if (wechatInformation != null) {
                    mav.addObject("wechat", true);
                    mav.addObject("wechat_information", wechatInformation);
                } else {
                    mav.addObject("wechat", false);
                    mav.addObject("unbind_wechat_uri", request.getContextPath() + WechatController.UNBIND_PATH);
                }
            } else {
                mav.addObject("wechat_appid", WECHAT_APPID);
                mav.addObject("wechat_redirect_uri", URLEncoder.encode(SERVER_CONTEXT + request.getContextPath() +
                        WechatController.CALLBACK_REQUEST_PATH));
            }
            if (weiboId != null) {
                WeiboInformation weiboInformation = weiboService.get(weiboId);
                if (weiboInformation != null) {
                    mav.addObject("weibo", true);
                    mav.addObject("weibo_information", weiboInformation);
                } else {
                    mav.addObject("weibo", false);
                    mav.addObject("unbind_weibo_uri", request.getContextPath() + WeiboController.UNBIND_PATH);
                }
            } else {
                mav.addObject("weibo_appid", WEIBO_APPID);
                mav.addObject("weibo_redirect_uri", URLEncoder.encode(SERVER_CONTEXT + request.getContextPath() +
                        WeiboController.CALLBACK_REQUEST_PATH));
            }
            mav.setViewName("7_1");
            return mav;
        } else {
            mav.addObject("verified", publisherService.isVerified(uid));
            Company company = companyService.get(uid);
            mav.addObject("contactPhone", company.getContactPhone());
            mav.addObject("license", company.getLicense());
            mav.addObject("companyName", company.getCompanyName());
            mav.addObject("companyCode", company.getCompanyCode());
            mav.setViewName("7_2");
            return mav;
        }
    }

    @RequestMapping(value = "/name", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object saveDisplayName(@RequestParam("name") String displayName, Principal principal) {
        Setting setting = settingService.get(new ObjectId(principal.getName()));
        setting.setDisplayName(displayName);
        settingService.save(setting);
        return Ajax.ok();
    }

    //发送邮件
    @RequestMapping(value = "/mails/activation", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object sendActivateMail(@RequestParam("email") String newEmail, Principal principal, Locale locale) {
        if (userDetailsManager.userExists(newEmail)) {
            return Ajax.failure(messageSource.getMessage("setting.email.duplicate", null, locale));
        } else {
            Setting setting = settingService.get(new ObjectId(principal.getName()));
            String originEmail = setting.getEmail();
            Cache cache = cacheManager.getCache(CacheConfig.CACHE_NAME_EMAIL_VERIFICATION_MATCHED_EMAIL);
            cache.put(principal.getName(), newEmail);
            Map<String, String> mailModel = new HashMap<>();
            mailModel.put("email", newEmail);
            emailService.create(newEmail, EmailConstants.EMAIL_UPDATE_TEMPLATENAME, messageSource.getMessage("email"
                    + ".title.updateemail", null, locale), mailModel);
            return Ajax.ok();
        }
    }

    /**
     * 设置新邮件，与AccountController里激活邮件不一样在于这里在没激活前没有任何数据修改，只是标记一下
     * 如果激活了需要删除原来的EmailAccount， 创建新的EmailAccount，修改Setting
     *
     * @return
     * @see AccountController
     */
    @RequestMapping(value = "/mails/activation", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Object activateEmail(@RequestParam("email") String newEmail, @RequestParam("code") String codeInput,
                                Principal principal, Locale locale) {
        ModelAndView mav = new ModelAndView("return");
        if (userDetailsManager.userExists(newEmail)) {
            mav.addObject("title", messageSource.getMessage("setting.email.title", null, locale));
            mav.addObject("content", messageSource.getMessage("setting.email.duplicate", null, locale));
            return mav;
        } else {
            String code = emailService.get(newEmail, EmailConstants.EMAIL_UPDATE_TEMPLATENAME);
            Cache cache = cacheManager.getCache(CacheConfig.CACHE_NAME_EMAIL_VERIFICATION_MATCHED_EMAIL);
            Setting setting = settingService.get(new ObjectId(principal.getName()));
            String originEmail = setting.getEmail();
            String savedNewEmail = cache.get(principal.getName(), String.class);
            if (StringUtils.equals(savedNewEmail, newEmail) && StringUtils.equals(code, codeInput)) {
                EmailAccount originAccount = mongoTemplate.findById(originEmail, EmailAccount.class);
                if (originAccount == null) {
                    Person person = mongoTemplate.findById(new ObjectId(principal.getName()), Person.class);
                    PhoneAccount phoneAccount = mongoTemplate.findById(person.getPhone(), PhoneAccount.class);
                    emailService.delete(newEmail, EmailConstants.EMAIL_UPDATE_TEMPLATENAME);
                    EmailAccount newAccount;
                    if (phoneAccount != null) {
                        newAccount = new EmailAccount(new ObjectId(principal.getName()),
                            phoneAccount.getUserAuthorities(), phoneAccount.getEPPassword(), savedNewEmail,
                            true, true, true, true);
                        mongoTemplate.save(newAccount);
                        setting.setEmail(savedNewEmail);
                        settingService.save(setting);
                    }
                    return "redirect:" + SETTINGS_CONTROLLER_PATH;
                } else if (StringUtils.equals(originAccount.getUsername(), principal.getName())) {
                    emailService.delete(newEmail, EmailConstants.EMAIL_UPDATE_TEMPLATENAME);
                    EmailAccount newAccount = new EmailAccount(originAccount.getUid(), originAccount
                            .getUserAuthorities(), originAccount.getEPPassword(), savedNewEmail, originAccount
                            .isAccountNonExpired(), originAccount.isAccountNonLocked(), originAccount
                            .isCredentialsNonExpired(), originAccount.isEnabled());
                    mongoTemplate.save(newAccount);
                    mongoTemplate.remove(originAccount);
                    setting.setEmail(savedNewEmail);
                    settingService.save(setting);
                    return "redirect:" + SETTINGS_CONTROLLER_PATH;
                } else {
                    logger.error("Someone update a email with error EmailAccount(belong to another one), " +
                            "notfix. uid:" + principal.getName());
                    mav.addObject("title", messageSource.getMessage("setting.email.title", null, locale));
                    mav.addObject("content", messageSource.getMessage("setting.email.error", null, locale));
                    return mav;
                }
            } else {
                mav.addObject("title", messageSource.getMessage("setting.email.title", null, locale));
                mav.addObject("content", messageSource.getMessage("setting.email.error", null, locale));
                return mav;
            }
        }
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object savePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("password") String
            rawPassword, Principal principal, Locale locale) {
        EPPassword password = mongoTemplate.findById(new ObjectId(principal.getName()), EPPassword.class);
        String originPassword = password.getPassword();
        if (encoder.matches(oldPassword, originPassword)) {
            password.setPassword(encoder.encode(rawPassword));
            mongoTemplate.save(password);
            return Ajax.ok();
        } else {
            return Ajax.failure(messageSource.getMessage("setting.password.notmatch", null, locale));
        }
    }

    //申请成为出版社
    @RequestMapping(value = {"/publisher/apply", "/application/publisher"}, method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object applyPublisher(Principal principal, Locale locale) {
        ObjectId uid = new ObjectId(principal.getName());
        Company company = companyService.get(uid);
        if (company == null) {
            return Ajax.failure(messageSource.getMessage("setting.personal.apply_publisher", null, locale));
        } else {
            publisherService.submit(uid);
            return Ajax.ok();
        }
    }

    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_USER")
    public Object settingsAvatar(@RequestParam("image") String avatar, Principal principal) {
        Setting setting = settingService.get(new ObjectId(principal.getName()));
        setting.setAvatar(avatar);
        settingService.save(setting);
        return Ajax.ok();
    }
}
