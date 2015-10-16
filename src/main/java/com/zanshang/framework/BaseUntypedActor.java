package com.zanshang.framework;

import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Lookis on 6/4/15.
 */
public abstract class BaseUntypedActor extends UntypedActor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private Properties properties;

    protected ApplicationContext spring;

    public BaseUntypedActor(ApplicationContext spring) {
        this.spring = spring;
        properties = new Properties();
        try {
            properties.load(new ClassPathResource("environment.properties").getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MongoTemplate getMongoTemplate() {
        spring.getApplicationName();
        MongoTemplate mongoTemplate = spring.getBean(MongoTemplate.class);
        return mongoTemplate;
    }

    public CacheManager getCacheManager() {
        return spring.getBean(CacheManager.class);
    }

    public String getProperty(String key) {
        String val = resolveSystemProperty(key);
        if (val == null) {
            return properties.getProperty(key);
        } else {
            return val;
        }
    }

    public ResourceLoader getSpringResoueceLoader() {
        return spring;
    }

    protected String resolveSystemProperty(String key) {
        try {
            String value = System.getProperty(key);
            if (value == null) {
                value = System.getenv(key);
            }
            return value;
        } catch (Throwable ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not access system property '" + key + "': " + ex);
            }
            return null;
        }
    }
}
