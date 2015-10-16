package com.zanshang.config.spring;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.zanshang.framework.spring.ObjectLoaded;
import com.zanshang.framework.spring.PriceReadConverter;
import com.zanshang.framework.spring.PriceWriteConverter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Lookis on 4/28/15.
 */
@Configuration
public class MongodbConfig {

    private
    @Value("${DB_HOST}")
    String host;

    private
    @Value("${DB_PORT}")
    int port;

    private
    @Value("${DB_USER}")
    String user;

    private
    @Value("${DB_PASSWORD}")
    String password;

    private
    @Value("${DB_DATABASE}")
    String database;

    public
    @Bean
    MongoClient mongo() throws Exception {
        if (StringUtils.isEmpty(user)) {
            return new MongoClient(new ServerAddress(host, port));
        } else {
            return new MongoClient(new ServerAddress(host, port), Collections.singletonList(MongoCredential
                    .createCredential(user, database, password.toCharArray())));
        }
    }

    public
    @Bean
    MongoTemplate mongoTemplate() throws Exception {
        SimpleMongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongo(), database);
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        converter.setCustomConversions(customConversions());
        MongoTemplate template =  new MongoTemplate(mongoDbFactory, converter);
        converter.afterPropertiesSet();;
        return template;
    }

    @Bean
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();
        converters.add(new PriceWriteConverter());
        converters.add(new PriceReadConverter());
        return new CustomConversions(converters);
    }
    public
    @Bean
    ApplicationListener<AfterConvertEvent> afterObjectConvetedListener() {
        return new ApplicationListener<AfterConvertEvent>() {
            @Override
            public void onApplicationEvent(AfterConvertEvent event) {
                Object source = event.getSource();
                if (source instanceof ObjectLoaded) {
                    ((ObjectLoaded) source).postLoaded();
                }
            }
        };
    }
}
