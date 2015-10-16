package com.zanshang.config.spring;

import com.zanshang.framework.HeaderAwaredInterceptor;
import com.zanshang.framework.SavedRequestAwareRegisterSuccessHandler;
import com.zanshang.framework.spring.RequestTimeArgumentResolver;
import com.zanshang.framework.spring.TicketArgumentResolver;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.List;
import java.util.Properties;

/**
 * Created by Lookis on 4/29/15.
 */
@Configuration
@EnableWebMvc
@Import(LocaleConfig.class)
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    LocalValidatorFactoryBean validatorFactoryBean;

    @Value("${IMAGE_CONTEXT}")
    String imageContext;

    @Value("${ENVIRONMENT}")
    String ENVIRONMENT;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler("/static/font/**").addResourceLocations("/static/font/").setCachePeriod(60 * 10);
    }
    //
    //    @Override
    //    public void addFormatters(FormatterRegistry registry) {
    //        registry.addConverter(new PriceReadConverter());
    //        registry.addConverter(new PriceWriteConverter());
    //    }

    @Override
    public Validator getValidator() {
        return validatorFactoryBean;
    }

    @Bean
    public DeviceResolverHandlerInterceptor handlerInterceptor() {
        return new DeviceResolverHandlerInterceptor();
    }

    @Bean
    public HandlerInterceptor headerAwaredInterceptor() {
        return new HeaderAwaredInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor());
        registry.addInterceptor(headerAwaredInterceptor());
    }

    public static final String DEFAULT_PAGE_PARAMETER = "page";

    public static final String DEFAULT_SIZE_PARAMETER = "size";

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new
                PageableHandlerMethodArgumentResolver();
        pageableHandlerMethodArgumentResolver.setPageParameterName(DEFAULT_PAGE_PARAMETER);
        pageableHandlerMethodArgumentResolver.setSizeParameterName(DEFAULT_SIZE_PARAMETER);
        argumentResolvers.add(pageableHandlerMethodArgumentResolver);
        argumentResolvers.add(new DeviceHandlerMethodArgumentResolver());
        argumentResolvers.add(timeArgumentResolver());
        argumentResolvers.add(new TicketArgumentResolver());
    }

    @Bean
    public HandlerMethodArgumentResolver timeArgumentResolver() {
        return new RequestTimeArgumentResolver();
    }

    @Bean
    public ViewResolver viewResolver() {
        ViewResolver delegateViewResolver = freeMarkerViewResolver();
        LiteDeviceDelegatingViewResolver viewResolver = new LiteDeviceDelegatingViewResolver(delegateViewResolver);
        viewResolver.setMobilePrefix("mobile/");
        viewResolver.setEnableFallback(true);
        return viewResolver;
    }

    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
        freeMarkerViewResolver.setCache(false);
        freeMarkerViewResolver.setPrefix("/pages/");
        freeMarkerViewResolver.setSuffix(".ftl");
        freeMarkerViewResolver.setRequestContextAttribute("requestContext");
        freeMarkerViewResolver.setContentType("text/html;charset=UTF-8");
        Properties properties = new Properties();
        properties.setProperty("imageRoot", imageContext);
        freeMarkerViewResolver.setAttributes(properties);
        return freeMarkerViewResolver;
    }

    @Bean(name = "freemarkerConfig")
    public FreeMarkerConfigurer getFreemarkerConfig() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        freemarker.template.Configuration conf = new freemarker.template.Configuration(freemarker.template
                .Configuration.VERSION_2_3_22);
        if (!StringUtils.equals(ENVIRONMENT, "PRODUCTION")) {
            conf.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        } else {
            conf.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        }
        configurer.setConfiguration(conf);
        return configurer;
    }

    @Bean
    public SavedRequestAwareRegisterSuccessHandler getSavedRequestAwareRegisterSuccessHandler() {
        return new SavedRequestAwareRegisterSuccessHandler();
    }
}
