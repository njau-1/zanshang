package com.zanshang.config.spring;

import akka.actor.ActorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Created by Lookis on 5/13/15.
 */
@Configuration
public class GlobalConfig {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ActorSystem system;

    @Bean
    public static PropertyPlaceholderConfigurer propertyConfigurer() throws IOException {
        PropertyPlaceholderConfigurer props = new PropertyPlaceholderConfigurer();
        props.setLocations(new Resource[]{new ClassPathResource("environment.properties")});
        props.setSearchSystemEnvironment(true);
        props.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        return props;
    }

    @PreDestroy
    public void destoryContext() {
        if (system!=null && !system.isTerminated()) {
            logger.info("Shutting down ActorSystem...");
            system.shutdown();
            system.awaitTermination();
        }
    }
}
