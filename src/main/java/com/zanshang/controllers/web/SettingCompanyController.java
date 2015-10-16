package com.zanshang.controllers.web;

import com.zanshang.captcha.CaptchaException;
import com.zanshang.captcha.PhoneCaptchaService;
import com.zanshang.models.Company;
import com.zanshang.services.CompanyTrapdoor;
import com.zanshang.services.PhoneCaptchaTrapdoor;
import com.zanshang.services.PublisherTrapdoor;
import com.zanshang.services.company.CompanyTrapdoorImpl;
import com.zanshang.services.phone.PhoneCaptchaTrapdoorImpl;
import com.zanshang.services.publisher.PublisherTrapdoorImpl;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.Locale;

/**
 * Created by Lookis on 7/5/15.
 */
@Controller
@RequestMapping("/settings/company")
public class SettingCompanyController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    PhoneCaptchaService captchaService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    PublisherTrapdoor publisherService;

    CompanyTrapdoor companyService;

    PhoneCaptchaTrapdoor phoneService;

    @PostConstruct
    protected void init() {
        publisherService = akkaTrapdoor.createTrapdoor(PublisherTrapdoor.class, PublisherTrapdoorImpl.class);
        companyService = akkaTrapdoor.createTrapdoor(CompanyTrapdoor.class, CompanyTrapdoorImpl.class);
        phoneService = akkaTrapdoor.createTrapdoor(PhoneCaptchaTrapdoor.class, PhoneCaptchaTrapdoorImpl.class);
    }

    @RequestMapping(value = "/code", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object modifyCompanyCode(@RequestParam("code") String code, Principal principal, Locale locale) {
        ObjectId uid = new ObjectId(principal.getName());
        if (publisherService.isVerified(uid)) {
            return Ajax.failure(messageSource.getMessage("setting.publisher.verified", null, locale));
        } else {
            Company company = companyService.get(uid);
            company.setCompanyCode(code);
            companyService.save(company);
            return Ajax.ok();
        }
    }

    @RequestMapping(value = "/license", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object modifyLicense(@RequestParam("license") String license, Principal principal, Locale locale) {
        ObjectId uid = new ObjectId(principal.getName());
        if (publisherService.isVerified(uid)) {
            return Ajax.failure(messageSource.getMessage("setting.publisher.verified", null, locale));
        } else {
            Company company = companyService.get(uid);
            company.setLicense(license);
            companyService.save(company);
            return Ajax.ok();
        }
    }

    @RequestMapping(value = "/phone", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object modifyContactPhone(@RequestParam("phone") String phone, @RequestParam("code") String codeInput,
                                     Principal principal, Locale locale) {
        if (!PhoneValidator.isValid(phone)) {
            return Ajax.failure(messageSource.getMessage("setting.company.phone.format_error", null, locale));
        }

        //verify code
        try {
            if (captchaService.verify(phone, codeInput)) {
                //every thing is ok, continue
                Company company = companyService.get(new ObjectId(principal.getName()));
                company.setContactPhone(phone);
                companyService.save(company);
                phoneService.delete(phone);
                return Ajax.ok();
            } else {

                return Ajax.failure(messageSource.getMessage("setting.company.phone.error_expire", null, locale));
            }
        } catch (CaptchaException e) {

            return Ajax.failure(messageSource.getMessage("setting.company.phone.error_expire", null, locale));
        }
    }

    @RequestMapping(value = "/name", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object modifyContactName(@RequestParam("name") String name, Principal principal, Locale locale) {
        ObjectId uid = new ObjectId(principal.getName());
        Company company = companyService.get(uid);
        company.setCompanyName(name);
        companyService.save(company);
        return Ajax.ok();
    }
}
