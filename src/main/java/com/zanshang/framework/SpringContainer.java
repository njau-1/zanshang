package com.zanshang.framework;

import com.zanshang.config.spring.MongodbConfig;
import org.springframework.beans.BeansException;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Lookis on 4/26/15.
 */
@Service
public class SpringContainer implements ApplicationContextAware{
    private static volatile SpringContainer instance = null;

    private ApplicationContext ctx = null;

    public static SpringContainer getInstance() {
        if(instance == null){
            instance = new SpringContainer();
            instance.ctx = new AnnotationConfigApplicationContext(MongodbConfig.class);
        }
        return instance;
    }

    private SpringContainer(){
    }

    public MongoTemplate getMongoTemplate(){
        return ctx.getBean(MongoTemplate.class);
    }

    public CacheManager getCacheManager(){
        return ctx.getBean(CacheManager.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    @PostConstruct
    public void inject(){
        this.instance = this.ctx.getBean(getClass());
    }
}
