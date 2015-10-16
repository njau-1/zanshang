package com.zanshang.controllers.web;

import com.zanshang.captcha.CaptchaException;
import com.zanshang.captcha.PhoneCaptchaService;
import com.zanshang.constants.EmailConstants;
import com.zanshang.framework.spring.MongodbUserDetailsManager;
import com.zanshang.models.EPPassword;
import com.zanshang.models.EmailAccount;
import com.zanshang.models.PhoneAccount;
import com.zanshang.services.EmailCodeTrapdoor;
import com.zanshang.services.PhoneCaptchaTrapdoor;
import com.zanshang.services.mailbox.EmailCodeTrapdoorImpl;
import com.zanshang.services.phone.PhoneCaptchaTrapdoorImpl;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.PhoneValidator;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Lookis on 6/26/15.
 */
@Controller
@RequestMapping(value = "/account/password")
public class AccountPasswordController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    PhoneCaptchaService captchaService;

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
        phoneService = akkaTrapdoor.createTrapdoor(PhoneCaptchaTrapdoor.class, PhoneCaptchaTrapdoorImpl.class);
        emailService = akkaTrapdoor.createTrapdoor(EmailCodeTrapdoor.class, EmailCodeTrapdoorImpl.class);
    }

    //找回密码表单页
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public ModelAndView passwordResetForm() {
        ModelAndView mav = new ModelAndView("2_2_1_1");
        mav.addObject("passwordPhoneForm", new PasswordPhoneForm());
        mav.addObject("passwordEmailForm", new PasswordEmailForm());
        return mav;
    }

    //用手机号直接修改密码
    @RequestMapping(params = "phone", method = RequestMethod.PUT)
    public Object resetPasswordByPhone(@Valid PasswordPhoneForm passwordPhoneForm, BindingResult result, Map<String,
            Object> model, Locale locale) {
        model.put("passwordEmailForm", new PasswordEmailForm());
        if (result.hasErrors()) {
            return "2_2_1_1";
        } else {
            boolean valid = PhoneValidator.isValid(passwordPhoneForm.getPhone());
            if (!valid) {
                result.rejectValue("phone", "account.password.reset.phone.format_error");
                return "2_2_1_1";
            } else {
                try {
                    if(captchaService.verify(passwordPhoneForm.getPhone(), passwordPhoneForm.getCode())){
                        phoneService.delete(passwordPhoneForm.getPhone());
                        PhoneAccount phoneAccount = mongoTemplate.findById(passwordPhoneForm.getPhone(), PhoneAccount.class);
                        if (phoneAccount == null) {
                            result.rejectValue("phone", "account.password.reset.phone.notexist");
                            return "2_2_1_1";
                        }
                        EPPassword password = phoneAccount.getEPPassword();
                        password.setPassword(encoder.encode(passwordPhoneForm.getRawPassword()));
                        userDetailsManager.updateUser(phoneAccount);
                        ModelAndView mav = new ModelAndView("return");
                        mav.addObject("title", messageSource.getMessage("return.title.ok", null, locale));
                        mav.addObject("content", messageSource.getMessage("return.content.phone.resetpassword", null, locale));
                        return mav;
                    }else{
                        result.rejectValue("code", "account.password.reset.code.expire_or_error");
                        return "2_2_1_1";
                    }
                } catch (CaptchaException e) {
                    result.rejectValue("code", "account.password.reset.code.expire_or_error");
                    return "2_2_1_1";
                }
            }
        }
    }

    //创建找回密码的邮件
    @RequestMapping(value = "/reset", params = "email", method = RequestMethod.POST)
    public Object createPasswordReset(@Valid PasswordEmailForm passwordEmailForm, BindingResult result, Map<String,
            Object> model, Locale locale) {
        model.put("passwordPhoneForm", new PasswordPhoneForm());
        if (result.hasErrors()) {
            return "2_2_1_1";
        } else {
            String email = passwordEmailForm.getEmail();
            EmailAccount emailAccount = mongoTemplate.findById(email, EmailAccount.class);
            if (emailAccount != null) {
                Map<String, String> emailModel = new HashMap<>();
                emailModel.put("email", email);
                emailService.create(email, EmailConstants.EMAIL_RESET_PASSWORD_TEMPLATENAME, messageSource.getMessage
                        ("email.title.resetpassword", null, locale), emailModel);
                ModelAndView mav = new ModelAndView("return");
                mav.addObject("title", messageSource.getMessage("return.title.ok", null, locale));
                mav.addObject("content", messageSource.getMessage("return.content.email.send", null, locale));
                return mav;
            } else {
                result.rejectValue("email", "account.password.reset.email.notexist");
                return "2_2_1_1";
            }
        }
    }

    //重新发送: 创建找回密码的邮件
    @RequestMapping(value = "/reset", params = "email", method = RequestMethod.GET)
    public Object recreatePasswordReset(@RequestParam("email") String email, Locale locale) {
        EmailAccount emailAccount = mongoTemplate.findById(email, EmailAccount.class);
        ModelAndView mav = new ModelAndView("return");
        if (emailAccount != null) {
            Map<String, String> emailModel = new HashMap<>();
            emailModel.put("email", email);
            emailService.create(email, EmailConstants.EMAIL_RESET_PASSWORD_TEMPLATENAME, messageSource.getMessage
                    ("email.title.resetpassword", null, locale), emailModel);
            mav.addObject("title", messageSource.getMessage("return.title.ok", null, locale));
            mav.addObject("content", messageSource.getMessage("return.content.email.send", null, locale));
        } else {
            mav.addObject("title", messageSource.getMessage("return.title.error", null, locale));
            mav.addObject("content", messageSource.getMessage("account.password.reset.email.notexist", null, locale));
        }
        return mav;
    }

    //邮件返回的新密码表单页
    @RequestMapping(value = "/{email}/reset", method = RequestMethod.GET)
    public Object getResetPassword(@PathVariable("email") String email, @RequestParam("code") String code, HttpServletRequest request, Locale locale) {
        try {
            String savedCode = emailService.get(email, EmailConstants.EMAIL_RESET_PASSWORD_TEMPLATENAME);
            if (StringUtils.equals(savedCode, code)) {
                ModelAndView mav = new ModelAndView("2_2_2");
                mav.addObject("email", email);
                mav.addObject("code", code);
                return mav;
            } else {
                ModelAndView mav = new ModelAndView("return");
                mav.addObject("title", messageSource.getMessage("return.title.error", null, locale));
                mav.addObject("content", messageSource.getMessage("return.content.email.code.error", new String[]{request
                        .getContextPath() + "/account/password/reset?email=" + email}, locale));
                return mav;
            }
        } catch (Exception e) {
            ModelAndView mav = new ModelAndView("return");
            mav.addObject("title", messageSource.getMessage("return.title.error", null, locale));
            mav.addObject("content", messageSource.getMessage("return.content.email.code.error",  new String[]{request
                    .getContextPath() + "/account/password/reset?email=" + email}, locale));
            return mav;
        }
    }

    //设定新密码
    @RequestMapping(params = "email", method = RequestMethod.PUT)
    public Object resetPasswordByEmail(@RequestParam("code") String code, @RequestParam("email") String email,
                                       @RequestParam("password") String rawPassword, Locale locale) {
        String savedCode = emailService.get(email, EmailConstants.EMAIL_RESET_PASSWORD_TEMPLATENAME);
        if (StringUtils.equals(code, savedCode)) {
            EmailAccount emailAccount = mongoTemplate.findById(email, EmailAccount.class);
            if (emailAccount != null) {
                emailService.delete(email, EmailConstants.EMAIL_RESET_PASSWORD_TEMPLATENAME);
                EPPassword password = emailAccount.getEPPassword();
                password.setPassword(encoder.encode(rawPassword));
                emailAccount.setEnabled(true);
                userDetailsManager.updateUser(emailAccount);
                ModelAndView mav = new ModelAndView("return");
                mav.addObject("title", messageSource.getMessage("return.title.ok", null, locale));
                mav.addObject("content", messageSource.getMessage("return.content.email.resetpassword", null, locale));
                return mav;
            } else {
                ModelAndView mav = new ModelAndView("return");
                mav.addObject("title", messageSource.getMessage("return.title.ok", null, locale));
                mav.addObject("content", messageSource.getMessage("return.content.email.notexist", null, locale));
                return mav;
            }
        } else {
            ModelAndView mav = new ModelAndView("return");
            mav.addObject("title", messageSource.getMessage("return.title.error", null, locale));
            mav.addObject("content", messageSource.getMessage("return.content.email.error", null, locale));
            return mav;
        }
    }


    static class PasswordPhoneForm {

        @NotEmpty(message = "{account.password.reset.phone.notempty}")
        private String phone;

        @NotEmpty(message = "{account.password.reset.code.notempty}")
        private String code;

        @NotEmpty(message = "{account.password.reset.password.notempty}")
        @Size(min = 6, message = "{account.password.reset.password.minlength}")
        private String rawPassword;

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setRawPassword(String rawPassword) {
            this.rawPassword = rawPassword;
        }

        public String getPhone() {

            return phone;
        }

        public String getCode() {
            return code;
        }

        public String getRawPassword() {
            return rawPassword;
        }
    }

    static class PasswordEmailForm {

        @NotEmpty(message = "{account.password.reset.email.notempty}")
        @Email(message = "{account.password.reset.email.format_error}")
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
