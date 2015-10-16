package com.zanshang.config.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lookis on 4/30/15.
 */
@Configuration
public class CacheConfig {

    public static final String CACHE_NAME_AUTHENTICATION = CacheKey.AUTHENTICATION.getName();

    public static final String COUNT_PRICE = CacheKey.COUNT_PRICE.getName();

    public static final String OAUTH_PARAMS = CacheKey.OAUTH_PARAMS.getName();

    public static final String CACHE_NAME_CSRFTOKEN = CacheKey.CSRFTOKEN.getName();

    public static final String CACHE_NAME_PRE_REGISTER_ACCESSTOKEN = CacheKey.PRE_REGISTER_ACCESSTOKEN.getName();

    public static final String CACHE_NAME_PHONE_VERIFICATION_CODE = CacheKey.PHONE_VERIFICATION_CODE.getName();

    public static final String CACHE_NAME_PHONE_VERIFICATION_DUPLICATE = CacheKey.PHONE_VERIFICATION_DUPLICATE
            .getName();

    public static final String CACHE_NAME_WECHAT_QR_ORDER = CacheKey.WECHAT_QR_ORDER.getName();

    public static final String CACHE_NAME_EMAIL_DUPLICATE = CacheKey.EMAIL_DUPLICATE.getName();

    //激活验证码
    public static final String CACHE_NAME_EMAIL_VERIFICATION_CODE = CacheKey.EMAIL_VERIFICATION_CODE.getName();

    //修改邮箱，old mail -> new mail
    public static final String CACHE_NAME_EMAIL_VERIFICATION_MATCHED_EMAIL = CacheKey
            .EMAIL_VERIFICATION_MATCHED_EMAIL.getName();

    //重置密码
    public static final String CACHE_NAME_EMAIL_RESETPASSWORD_CODE = CacheKey.EMAIL_RESETPASSWORD_CODE.getName();

    public static final String CACHE_NAME_LOGIN_SAVED_REQUEST = CacheKey.LOGIN_SAVED_REQUEST.getName();

    //二维码分享,发生购买回报
    public static final String CACHE_NAME_SHARE_PROJECT_UID = CacheKey.SHARE_PROJECT_UID.getName();

    //wit 门票锁定
    public static final String CACHE_NAME_WIT_TICKET_LOCKED_INDEX = CacheKey.WIT_TICKET_LOCKED_INDEX.getName();
    public static final String CACHE_NAME_WIT_TICKET_LOCKED = CacheKey.WIT_TICKET_LOCKED.getName();

    public enum CacheKey {
        AUTHENTICATION(30, TimeUnit.DAYS, "ticket"),
        CSRFTOKEN(10, TimeUnit.MINUTES, "csrf"),
        PRE_REGISTER_ACCESSTOKEN(10, TimeUnit.MINUTES, "pre_register"),
        COUNT_PRICE(1, TimeUnit.DAYS, "count_price"),
        OAUTH_PARAMS(10, TimeUnit.MINUTES, "oauth_params"),
        PHONE_VERIFICATION_CODE(10, TimeUnit.MINUTES, "phone_verification"),
        PHONE_VERIFICATION_DUPLICATE(1, TimeUnit.MINUTES, "phone_verification_duplicate"),
        WECHAT_QR_ORDER(2, TimeUnit.HOURS, "wechat_qr_order"),
        EMAIL_DUPLICATE(1, TimeUnit.MINUTES, "email_duplicate"),
        EMAIL_VERIFICATION_CODE(30, TimeUnit.DAYS, "email_verification"),
        EMAIL_VERIFICATION_MATCHED_EMAIL(30, TimeUnit.DAYS, "email_verification_match_email"),
        EMAIL_RESETPASSWORD_CODE(1, TimeUnit.DAYS, "email_resetpassword_code"),
        LOGIN_SAVED_REQUEST(10, TimeUnit.MINUTES, "saved_login_redirect_url"),
        SHARE_PROJECT_UID(1, TimeUnit.HOURS, "share_project_uid"),
        WIT_TICKET_LOCKED_INDEX(30, TimeUnit.DAYS, "wit_ticket_locked_index"),
        WIT_TICKET_LOCKED(10, TimeUnit.MINUTES, "wit_ticket_locked"),
        CAPTCHA(10, TimeUnit.MINUTES, "captcha"),
        WECHAT_JSAPI(2, TimeUnit.HOURS, "wechat_jsapi");

        long time;

        TimeUnit timeUnit;

        String name;

        CacheKey(long time, TimeUnit timeUnit, String name) {
            this.time = time;
            this.timeUnit = timeUnit;
            this.name = name;
        }

        public long getTime() {
            return time;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public String getName() {
            return name;
        }
    }

    private
    @Value("${REDIS_HOST}")
    String host;

    private
    @Value("${REDIS_PORT}")
    int port;

    private
    @Value("${REDIS_PASSWORD}")
    String password;

    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
        cacheManager.setUsePrefix(true);
        cacheManager.setCachePrefix((cacheName) -> (cacheName + "|").getBytes());
        Map<String, Long> expireMap = new HashMap<>();
        for (CacheKey cacheKey : CacheKey.values()) {
            expireMap.put(cacheKey.getName(), cacheKey.getTimeUnit().toSeconds(cacheKey.getTime()));
        }
        cacheManager.setExpires(expireMap);
        cacheManager.setDefaultExpiration(TimeUnit.DAYS.toSeconds(30));
        return cacheManager;
    }

    public JedisConnectionFactory connectionFactory() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnCreate(true);
        config.setTestWhileIdle(true);
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setPoolConfig(config);
        factory.setHostName(host);
        factory.setPort(port);
        factory.setPassword(password);
        factory.afterPropertiesSet();
        return factory;
    }

    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setEnableDefaultSerializer(true);
        redisTemplate.setConnectionFactory(connectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
