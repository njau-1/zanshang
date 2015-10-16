package com.zanshang.config.spring;

import com.zanshang.framework.spring.*;
import com.zanshang.framework.wechat.WechatOAuth2AuthenticationFilter;
import com.zanshang.framework.wechat.WechatOAuth2AuthenticationProvider;
import com.zanshang.framework.wechat.WechatUserDetailsService;
import com.zanshang.framework.weibo.WeiboOAuth2AuthenticationFilter;
import com.zanshang.framework.weibo.WeiboOAuth2AuthenticationProvider;
import com.zanshang.framework.weibo.WeiboUserDetailsService;
import com.zanshang.utils.AkkaTrapdoor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by Lookis on 4/28/15.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String USERNAME = "username";

    public static final String LOGIN_PROCESSING_URL = "/authentication";

    public static final String PASSWORD = "password";

    @Autowired
    MongodbUserDetailsManager userDetailsManager;

    @Autowired
    CachedSecurityContextRepository securityContextRepository;

    @Autowired
    CachedCsrfTokenRepository csrfTokenRepository;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @Autowired
    WechatUserDetailsService wechatUserDetailsService;

    @Autowired
    WeiboUserDetailsService weiboUserdetailsService;

    @Autowired
    CacheRequestCache requestCache;

    @Autowired
    CacheManager cacheManager;

    @Value("${SERVER_CONTEXT}")
    String serverContext;

    public SecurityConfig() {
        super(true);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
        auth.authenticationProvider(wechatAuthenticationProvider());
        auth.authenticationProvider(weiboAuthenticationProvider());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /**
         * defaults
         *
         */
        http.addFilter(new WebAsyncManagerIntegrationFilter()).exceptionHandling().and().headers().and()
                .sessionManagement().and().securityContext().and().requestCache().and().anonymous().and().servletApi
                ().and().apply(new DefaultLoginPageConfigurer<HttpSecurity>()).and().logout();
        //setting
        http.headers().frameOptions().sameOrigin();
        http.setSharedObject(RequestCache.class, requestCache);
        http.authorizeRequests()
                //after we EnableGlobalMethodSecurity, we use Secured annotation to do the auth.
                .anyRequest().permitAll() //
                .and() //
                .formLogin()//
                .loginPage("/authentication")//
                .loginProcessingUrl(LOGIN_PROCESSING_URL)//
                .usernameParameter(USERNAME)//
                .passwordParameter(PASSWORD)//
                .successHandler(authenticationSuccessHandler()).failureHandler(authenticationFailureHandler
                ("/authentication?error=1"))//
                .permitAll() //
                .and() //
                .addFilterAfter(weiboAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)//
                .addFilterAfter(wechatAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)//
                .logout()//
                .logoutRequestMatcher(new AntPathRequestMatcher("/authentication", "DELETE"))//
                .permitAll()//
                .logoutSuccessUrl("/authentication?logout")//
                        //                .and()//
                        //                .csrf()//
                        //                .csrfTokenRepository(csrfTokenRepository)//
                .and()//
                .securityContext()//
                .securityContextRepository(securityContextRepository);
    }

    public SimpleUrlAuthenticationFailureHandler authenticationFailureHandler(String authenticationFailureUrl) {
        SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler
                (authenticationFailureUrl);
        handler.setUseForward(true);
        return handler;
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setRequestCache(requestCache);
        handler.setTargetUrlParameter("return");
        handler.setDefaultTargetUrl("/authentication?success");
        return handler;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
        dap.setUserDetailsService(userDetailsManager);
        dap.setPreAuthenticationChecks(userDetailsChecker());
        dap.setPasswordEncoder(passwordEncoder());
        return dap;
    }

    @Bean
    public AuthenticationProvider wechatAuthenticationProvider() {
        return new WechatOAuth2AuthenticationProvider(wechatUserDetailsService);
    }

    @Bean
    public AuthenticationProvider weiboAuthenticationProvider() {
        return new WeiboOAuth2AuthenticationProvider(weiboUserdetailsService);
    }

    @Bean
    @Autowired
    public WechatOAuth2AuthenticationFilter wechatAuthenticationFilter() throws Exception {
        WechatOAuth2AuthenticationFilter filter = new WechatOAuth2AuthenticationFilter();
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        filter.setAkkaTrapdoor(akkaTrapdoor);
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setCallbackServerContext(serverContext);
        filter.setCacheManager(cacheManager);
        return filter;
    }

    @Bean
    public WeiboOAuth2AuthenticationFilter weiboAuthenticationFilter() throws Exception {
        WeiboOAuth2AuthenticationFilter filter = new WeiboOAuth2AuthenticationFilter();
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        filter.setAkkaTrapdoor(akkaTrapdoor);
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setCallbackServerContext(serverContext);
        filter.setCacheManager(cacheManager);
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new PasswordEncoder() {
            Md5PasswordEncoder mpe = new Md5PasswordEncoder();

            @Override
            public String encode(CharSequence rawPassword) {
                return mpe.encodePassword(String.valueOf(rawPassword), null);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return mpe.isPasswordValid(encodedPassword, String.valueOf(rawPassword), null);
            }
        };
    }

    @Bean
    public AccountAwaredUserDetailsChecker userDetailsChecker() {
        return new AccountAwaredUserDetailsChecker();
    }
}
