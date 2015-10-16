package com.zanshang.controllers.web;

import com.qiniu.util.StringUtils;
import com.zanshang.captcha.CaptchaException;
import com.zanshang.captcha.PhoneCaptchaService;
import com.zanshang.config.spring.CacheConfig;
import com.zanshang.constants.BusinessType;
import com.zanshang.constants.Connect;
import com.zanshang.constants.EmailConstants;
import com.zanshang.framework.SavedRequestAwareRegisterSuccessHandler;
import com.zanshang.framework.Ticket;
import com.zanshang.framework.spring.MongodbUserDetailsManager;
import com.zanshang.framework.wechat.WechatOAuth2AuthenticationFilter;
import com.zanshang.framework.weibo.WeiboOAuth2AuthenticationFilter;
import com.zanshang.models.*;
import com.zanshang.models.audit.AuditCompany;
import com.zanshang.services.*;
import com.zanshang.services.company.CompanyTrapdoorImpl;
import com.zanshang.services.mailbox.EmailCodeTrapdoorImpl;
import com.zanshang.services.oauth.OAuthUrlProcessService;
import com.zanshang.services.person.PersonTrapdoorImpl;
import com.zanshang.services.phone.PhoneCaptchaTrapdoorImpl;
import com.zanshang.services.setting.SettingTrapdoorImpl;
import com.zanshang.services.wechat.WechatAuthorizationTrapdoorImpl;
import com.zanshang.services.weibo.WeiboAuthorizationTrapdoorImpl;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.Json;
import com.zanshang.utils.PhoneValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Lookis on 6/26/15.
 */
@Controller
@RequestMapping(RegisterController.REGISTER_PATH)
public class RegisterController {

    public static final String CONNECT_PATH = "/connect";

    public static final String REGISTER_PATH = "/register";

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SavedRequestAwareRegisterSuccessHandler handler;

    @Autowired
    OAuthUrlProcessService oAuthUrlProcessService;

    @Autowired
    PhoneCaptchaService captchaService;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    MongodbUserDetailsManager userDetailsManager;

    @Autowired
    MessageSource messageSource;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RequestCache requestCache;

    PhoneCaptchaTrapdoor phoneService;

    CompanyTrapdoor companyService;

    SettingTrapdoor settingService;

    EmailCodeTrapdoor emailService;

    PersonTrapdoor personService;

    WechatAuthorizationTrapdoor wechatService;

    WeiboAuthorizationTrapdoor weiboService;

    @PostConstruct
    public void initActor() {
        companyService = akkaTrapdoor.createTrapdoor(CompanyTrapdoor.class, CompanyTrapdoorImpl.class);
        settingService = akkaTrapdoor.createTrapdoor(SettingTrapdoor.class, SettingTrapdoorImpl.class);
        personService = akkaTrapdoor.createTrapdoor(PersonTrapdoor.class, PersonTrapdoorImpl.class);
        emailService = akkaTrapdoor.createTrapdoor(EmailCodeTrapdoor.class, EmailCodeTrapdoorImpl.class);
        wechatService = akkaTrapdoor.createTrapdoor(WechatAuthorizationTrapdoor.class,
                WechatAuthorizationTrapdoorImpl.class);
        weiboService = akkaTrapdoor.createTrapdoor(WeiboAuthorizationTrapdoor.class, WeiboAuthorizationTrapdoorImpl
                .class);
        phoneService = akkaTrapdoor.createTrapdoor(PhoneCaptchaTrapdoor.class, PhoneCaptchaTrapdoorImpl.class);
        handler.setTargetUrlParameter("return");
        handler.setRequestCache(requestCache);
    }

    //正常注册用表单
    @RequestMapping(method = RequestMethod.GET)
    public String showRegister(Map<String, Object> model) {
        model.put("personalForm", new PersonalForm());
        model.put("companyForm", new CompanyForm());
        return "2_1_2_2";
    }

    @RequestMapping(value = "/company", method = RequestMethod.POST)
    public Object companyRegister(@Valid CompanyForm companyForm, BindingResult result, Map<String, Object> model,
                                  Locale locale) {
        if (result.hasErrors()) {
            model.put("personalForm", new PersonalForm());
            return "2_1_2_2";
        } else {
            EmailAccount account = new EmailAccount(companyForm.getEmail(), encoder.encode(companyForm.getPassword()));
            account.setEnabled(false);//需要激活才可启用
            account.setNonLocked(false);//企业用户需要后台管理员验证
            try {
                userDetailsManager.createUser(account);
            } catch (DuplicateKeyException e) {
                model.put("personalForm", new PersonalForm());
                result.rejectValue("email", "register.email.duplicate");
                return "2_1_2_2";
            }
            Map<String, String> mailModel = new HashMap<>();
            mailModel.put("email", companyForm.getEmail());
            emailService.create(companyForm.getEmail(), EmailConstants.EMAIL_ACTIVE_TEMPLATENAME, messageSource
                    .getMessage("email.title.activation", null, locale), mailModel);
            Setting setting = new Setting(account.getUid(), companyForm.getContact(), companyForm.getEmail());

            Company company = new Company(account.getUid(), companyForm.getCompanyName(), companyForm.getCompanyCode
                    (), companyForm.getContact(), companyForm.getContactPhone(), companyForm.getLicense());
            companyService.save(company);
            AuditCompany auditCompany = new AuditCompany(companyForm.getEmail());
            mongoTemplate.save(auditCompany);
            settingService.save(setting);
            ModelAndView mav = new ModelAndView("return");
            mav.addObject("title", messageSource.getMessage("register.email.verification.title", null, locale));
            mav.addObject("content", messageSource.getMessage("register.email.verification.content", null, locale));
            return mav;
        }
    }
    @RequestMapping(value = "/personal/email", method = RequestMethod.POST)
    public Object emailRegister(@Valid PersonalForm personalForm, BindingResult result, Map<String, Object> model,
                                Locale locale, HttpServletRequest request, HttpServletResponse response, @Ticket String ticket) {
        if (result.hasErrors()) {
            model.put("companyForm", new CompanyForm());
            return "2_1_2_2";
        } else {
            EmailAccount account = new EmailAccount(personalForm.getEmail(), encoder.encode(personalForm.getPassword
                    ()));
            account.setEnabled(false);//需要激活才可启用
            try {
                userDetailsManager.createUser(account);
            } catch (DuplicateKeyException e) {
                model.put("companyForm", new CompanyForm());
                result.rejectValue("email", "register.email.duplicate");
                return "2_1_2_2";
            }
            Map<String, String> mailModel = new HashMap<>();
            mailModel.put("email", personalForm.getEmail());
            emailService.create(personalForm.getEmail(), EmailConstants.EMAIL_ACTIVE_TEMPLATENAME, messageSource
                    .getMessage("email.title.activation", null, locale), mailModel);
            Person person = new Person(account.getUid(), null);
            Setting setting = new Setting(account.getUid(), personalForm.getName(), personalForm.getEmail());
            personService.save(person);
            settingService.save(setting);
            ModelAndView mav = new ModelAndView("return");
            mav.addObject("title", messageSource.getMessage("register.email.verification.title", null, locale));
            mav.addObject("content", messageSource.getMessage("register.email.verification.content", null, locale));
            //register
            String returnUrl = oAuthUrlProcessService.getParamByKey(BusinessType.LOGIN, ticket, "return");
            if (returnUrl != null && !returnUrl.isEmpty()) {
                handler.setDefaultTargetUrl(returnUrl);
            }
            returnUrl = handler.onRegisterSuccess(request, response);
            mav.addObject("goto", returnUrl);
            return mav;
        }
    }

    @RequestMapping(value = "/personal/phone", method = RequestMethod.POST)
    public Object phoneRegister(@Valid PersonalForm personalForm, BindingResult result, Map<String, Object> model,
                                Locale locale, HttpServletRequest request, HttpServletResponse response, @Ticket String ticket) {
        if (result.hasErrors()) {
            model.put("companyForm", new CompanyForm());
            return "2_1_2_2";
        } else {
            if (!PhoneValidator.isValid(personalForm.getPhone())) {
                model.put("companyForm", new CompanyForm());
                result.rejectValue("phone", "register.personal.phone.format_error");
                return "2_1_2_2";
            }
            //verify code
            try {
                if(captchaService.verify(personalForm.getPhone(), personalForm.getCode())){
                    phoneService.delete(personalForm.getPhone());
                }else{
                    model.put("companyForm", new CompanyForm());
                    result.rejectValue("code", "register.connect.phone.error_expire");
                    return "2_1_2_2";
                }
            } catch (CaptchaException e) {
                logger.error("Phone Verification Code Not exist." + personalForm.getPhone());
                model.put("companyForm", new CompanyForm());
                result.rejectValue("code", "register.connect.phone.error_expire");
                return "2_1_2_2";
            }

            PhoneAccount account = new PhoneAccount(personalForm.getPhone(), encoder.encode(personalForm.getPassword
                    ()));
            try {
                userDetailsManager.createUser(account);
            } catch (DuplicateKeyException e) {
                model.put("companyForm", new CompanyForm());
                result.rejectValue("phone", "register.phone.duplicate");
                return "2_1_2_2";
            }
            Setting setting = new Setting(account.getUid(), personalForm.getName(), null);
            settingService.save(setting);
            Person person = new Person(account.getUid(), personalForm.getPhone());
            personService.save(person);
            ModelAndView mav = new ModelAndView("registersuccess");
//            mav.addObject("title", messageSource.getMessage("register.phone.verification.title", null, locale));
//            mav.addObject("content", messageSource.getMessage("register.phone.verification.content", null, locale));
            //register
            String returnUrl = oAuthUrlProcessService.getParamByKey(BusinessType.LOGIN, ticket, "return");
            if (returnUrl != null && !returnUrl.isEmpty()) {
                handler.setDefaultTargetUrl(returnUrl);
            }
            returnUrl = handler.onRegisterSuccess(request, response);
            mav.addObject("return", returnUrl);
            return mav;
        }
    }

    //第三方账号注册用表单
    //从相应的controller跳转过来
    //no cache! 本应是post的，可是这里是第三方跳转
    @RequestMapping(value = CONNECT_PATH + "/{platform}", method = RequestMethod.GET)
    public Object connectRegister(@PathVariable("platform") String platform, HttpServletRequest request, @Ticket
    String ticket) {
        ModelAndView mav = new ModelAndView("2_1_2_1_1");
        mav.addObject("platform", platform);
        return mav;
    }

    @RequestMapping(value = CONNECT_PATH + "/{platform}", method = RequestMethod.POST)
    public Object checkRegister(@RequestParam("username") String username, @PathVariable("platform") String platform,
                                HttpServletRequest request, @Ticket String ticket, Locale locale) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("platform", platform);
        try {
            ZSAccount userDetails = (ZSAccount) userDetailsManager.loadUserByUsername(username);
            Person person = personService.get(userDetails.getUid());
            if (person == null) {
                //尝试connect一个company账号
                mav.setViewName("2_1_2_1_1");
                mav.addObject("message", messageSource.getMessage("connect.company", null, locale));
                return mav;
            }
            mav.setViewName("2_1_2_1_2");
            mav.addObject("username", username);
            return mav;
        } catch (UsernameNotFoundException e) {
            boolean isEmail = EmailValidator.getInstance().isValid(username);
            boolean isPhone = PhoneValidator.isValid(username);
            if (isEmail) {
                //新用户邮箱
                PersonalForm personalForm = new PersonalForm();
                personalForm.setEmail(username);
                mav.addObject("personalForm", personalForm);
                mav.setViewName("2_1_2_1_3");
                return mav;
            } else if (isPhone) {
                //新用户手机
                mav.setViewName("2_1_2_1_4");
                PersonalPhoneForm form = new PersonalPhoneForm();
                form.setPhone(username);
                mav.addObject("personalPhoneForm", form);
                mav.addObject("phone", username);
                return mav;
            } else {
                mav.setViewName("2_1_2_1_1");
                mav.addObject("message", messageSource.getMessage("connect.username.format_error", null, locale));
                return mav;
            }
        }
    }

    //老用户connect
    @RequestMapping(value = CONNECT_PATH + "/{platform}/account", method = RequestMethod.POST)
    public Object accountExist(@PathVariable("platform") String platform, @RequestParam("username") String username,
                               @RequestParam("password") String rawPassword, @Ticket String ticket,
                               HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
        String encPassword = userDetails.getPassword();
        boolean matches = encoder.matches(rawPassword, encPassword);
        if (matches) {
            Person person = personService.get(new ObjectId(userDetails.getUsername()));
            //company账号不能connect登陆
            Assert.notNull(person);
            connectAccount(platform, ticket, person);
            authenticateUser(username, rawPassword, request);
            //register
            String returnUrl = oAuthUrlProcessService.getParamByKey(BusinessType.LOGIN, ticket, "return");
            if (returnUrl != null && !returnUrl.isEmpty()) {
                return "redirect:" + returnUrl;
            }
            handler.setDefaultTargetUrl("redirect:/");
            return handler.onRegisterSuccess(request, response);
        } else {
            ModelAndView mav = new ModelAndView("2_1_2_1_2");
            mav.addObject("username", username);
            mav.addObject("platform", platform);
            mav.addObject("bad_credentials", true);
            return mav;
        }
    }

    //新用户注册Email
    @RequestMapping(value = CONNECT_PATH + "/{platform}/email", method = RequestMethod.POST)
    public Object createConnectEmailAccount(@PathVariable("platform") String platform, @Valid PersonalForm
            personalForm, BindingResult result, Map<String, Object> model, HttpServletRequest request,HttpServletResponse response, @Ticket String
            ticket, Locale locale, Device device) {
        model.put("platform", platform);
        if (result.hasErrors()) {
            return "2_1_2_1_3";
        } else {
            EmailAccount account = new EmailAccount(personalForm.getEmail(), encoder.encode(personalForm.getPassword
                    ()));
            account.setEnabled(true);//需要激活才可启用
            try {
                userDetailsManager.createUser(account);
            } catch (DuplicateKeyException e) {
                result.rejectValue("email", "register.email.duplicate");
                return "2_1_2_1_1";
            }
            Map<String, String> mailModel = new HashMap<>();
            mailModel.put("email", personalForm.getEmail());
            emailService.create(personalForm.getEmail(), EmailConstants.EMAIL_ACTIVE_TEMPLATENAME, messageSource
                    .getMessage("email.title.activation", null, locale), mailModel);
            Setting setting = new Setting(account.getUid(), personalForm.getName(), personalForm.getEmail());
            settingService.save(setting);
            Person person = new Person(account.getUid(), null);
            personService.save(person);
            connectAccount(platform, ticket, person);
            authenticateUser(personalForm.getEmail(), personalForm.getPassword(), request);
            String targetUrl;
            if (device.isMobile()) {
                targetUrl = "redirect:/projects";
            } else {
                targetUrl = "redirect:/";
            }
            //register
            String returnUrl = oAuthUrlProcessService.getParamByKey(BusinessType.LOGIN, ticket, "return");
            if (returnUrl != null && !returnUrl.isEmpty()) {
                return "redirect:" + returnUrl;
            }
            handler.setDefaultTargetUrl(targetUrl);
            return handler.onRegisterSuccess(request, response);
        }
    }

    //新用户注册Phone
    @RequestMapping(value = CONNECT_PATH + "/{platform}/phone", method = RequestMethod.POST)
    public Object createConnectPhoneAccount(@PathVariable("platform") String platform, @Valid PersonalPhoneForm
            personalPhoneForm, BindingResult result, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response, @Ticket
    String ticket) {
        model.put("platform", platform);
        model.put("phone", personalPhoneForm.getPhone());
        if (result.hasErrors()) {
            return "2_1_2_1_4";
        } else {
            if (!PhoneValidator.isValid(personalPhoneForm.getPhone())) {
                result.rejectValue("phone", "register.personal.phone.format_error");
                return "2_1_2_1_4";
            }
            //verify code
            try {
                if (captchaService.verify(personalPhoneForm.getPhone(), personalPhoneForm.getCode())) {
                    //pass
                }else{
                    result.rejectValue("code", "register.connect.phone.error_expire");
                    return "2_1_2_1_4";
                }
            } catch (CaptchaException e) {
                logger.error("Phone Verification Code Not exist." + personalPhoneForm.getPhone());
                result.rejectValue("code", "register.connect.phone.error_expire");
                return "2_1_2_1_4";
            }

            //create phone account
            PhoneAccount account = new PhoneAccount(personalPhoneForm.getPhone(), encoder.encode(personalPhoneForm
                    .getPassword()));
            try {
                userDetailsManager.createUser(account);
            } catch (DuplicateKeyException e) {
                logger.error("Register with same phone number which already exist in system." + personalPhoneForm
                        .getPhone());
                result.rejectValue("phone", "register.phone.duplicate");
                return "2_1_2_1_4";
            }
            Setting setting = new Setting(account.getUid(), personalPhoneForm.getName(), null);
            settingService.save(setting);
            Person person = new Person(account.getUid(), personalPhoneForm.getPhone());
            personService.save(person);
            authenticateUser(personalPhoneForm.getPhone(), personalPhoneForm.getPassword(), request);
            connectAccount(platform, ticket, person);
            //register
            String returnUrl = oAuthUrlProcessService.getParamByKey(BusinessType.LOGIN, ticket, "return");
            if (returnUrl != null && !returnUrl.isEmpty()) {
                return "redirect:" + returnUrl;
            }
            handler.setDefaultTargetUrl("redirect:/");
            return handler.onRegisterSuccess(request, response);
        }
    }

    private void authenticateUser(String username, String password, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        token.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }

    private void connectAccount(String platform, String ticket, Person person) {
        Cache cache = cacheManager.getCache(CacheConfig.CACHE_NAME_PRE_REGISTER_ACCESSTOKEN);
        String jsonMap = cache.get(ticket, String.class);
        Map<String, String> foreignUser = Json.fromJson(jsonMap, Map.class);
        if (foreignUser != null) {
            switch (platform) {
                case WechatOAuth2AuthenticationFilter.PLATFORM:
                    wechatService.save(foreignUser);
                    person.setWechatId((String) foreignUser.get(Connect.WECHAT_ID_IN_ACCESSTOKEN));
                    break;
                case WeiboOAuth2AuthenticationFilter.PLATFORM:
                    weiboService.save(foreignUser);
                    person.setWeiboId(foreignUser.get(Connect.WEIBO_ID_IN_ACCESSTOKEN));
                default:
                    logger.error("Connect with platform not supported! " + platform);
            }
        }
        personService.save(person);
    }

    static class CompanyForm {

        @NotEmpty(message = "{register.company.companyName.notempty}")
        private String companyName;

        @NotEmpty(message = "{register.company.companyCode.notempty}")
        private String companyCode;

        @NotEmpty(message = "{register.company.contact.notempty}")
        private String contact;

        @NotEmpty(message = "{register.company.contactPhone.notempty}")
        private String contactPhone;

        @NotEmpty(message = "{register.company.email.notempty}")
        @Email(message = "{register.company.email.format_error}")
        private String email;

        @NotEmpty(message = "{register.company.password.notempty}")
        @Size(min = 6, message = "{register.company.password.minlength}")
        private String password;

        @NotEmpty(message = "{register.company.license.notempty}")
        private String license;

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }
    }

    static class PersonalPhoneForm {

        @NotEmpty(message = "{register.personal.phone.notempty}")
        private String phone;

        @NotEmpty(message = "{register.personal.password.notempty}")
        @Size(min = 6, message = "{register.personal.password.minlength}")
        private String password;

        @NotEmpty(message = "{register.personal.name.notempty}")
        private String name;

        @NotEmpty(message = "{register.personal.phonecode.notempty}")
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class PersonalForm {

        @Email(message = "{register.personal.email.format_error}")
        private String email;

        @Size(min = 8, message = "{register.personal.phone.format_error}")
        private String phone;

        private String code;

        @NotEmpty(message = "{register.personal.password.notempty}")
        @Size(min = 6, message = "{register.personal.password.minlength}")
        private String password;

        @NotEmpty(message = "{register.personal.name.notempty}")
        private String name;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "PersonalForm{" +
                    "email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
