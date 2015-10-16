package com.zanshang.config.spring;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Lookis on 4/28/15.
 */
@Configuration
public class LocaleConfig {

    @Bean
    public LocaleChangeInterceptor localeChange(){
        return new LocaleChangeInterceptor();
    }

    @Bean
    public LocaleResolver localeResolver(){
        CookieLocaleResolver clr = new CookieLocaleResolver();
        clr.setDefaultLocale(Locale.CHINESE);
        clr.setDefaultTimeZone(TimeZone.getTimeZone("GMT+8"));
        clr.setCookieName("locale");
        return clr;
    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("utf-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setBasename("classpath:languages/messages");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean(){
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource());
        return validatorFactoryBean;
    }
}
