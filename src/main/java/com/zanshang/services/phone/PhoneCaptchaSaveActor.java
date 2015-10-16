package com.zanshang.services.phone;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.framework.BaseUntypedActor;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Lookis on 6/4/15.
 */
public class PhoneCaptchaSaveActor extends BaseUntypedActor {

    private Cache duplicateCache;

    private Cache verificationCache;

    private CloseableHttpAsyncClient httpClient;

    public PhoneCaptchaSaveActor(ApplicationContext spring) {
        super(spring);
        duplicateCache = getCacheManager().getCache(CacheConfig.CACHE_NAME_PHONE_VERIFICATION_DUPLICATE);
        verificationCache = getCacheManager().getCache(CacheConfig.CACHE_NAME_PHONE_VERIFICATION_CODE);
        httpClient = HttpAsyncClients.createDefault();
        httpClient.start();
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Map.Entry<String, String> entry = (Map.Entry<String, String>) o;
        Cache.ValueWrapper valueWrapper = duplicateCache.get(entry.getKey());
        if (valueWrapper == null || valueWrapper.get() == null) {
            String code = verificationCache.get(entry.getKey(), String.class);
            if (code == null) {
                code = RandomStringUtils.randomAlphanumeric(6).toLowerCase();
                verificationCache.put(entry.getKey(), code);
            }
            String template = entry.getValue();
            final CacheConfig.CacheKey cacheKey = CacheConfig.CacheKey.PHONE_VERIFICATION_CODE;

            final SmsProvider provider = SmsProvider.values()[RandomUtils.nextInt(SmsProvider.values().length)];
            //            SmsProvider provider = SmsProvider.DIEXIN;
            String message = String.format(template, code, cacheKey.getTimeUnit().toMinutes(cacheKey.getTime()));
            HttpPost httpPost = provider.build(entry.getKey(), message);
            HttpClientContext context = provider.context();
            httpClient.execute(httpPost, context, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse result) {
                    duplicateCache.put(entry.getKey(), Boolean.TRUE.toString());
                    logger.debug("Phone Captcha Done." + result);
                    logger.info("Send Captcha[" + message + "] to Phone[" + entry.getKey() + "] via [" + provider
                            .name() + "] Done");
                    if(logger.isDebugEnabled()){
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(result.getEntity
                                    ().getContent()));
                            logger.debug("Result:" + reader.readLine());
                        } catch (IOException e) {
                        }
                    }
                }

                @Override
                public void failed(Exception ex) {
                    logger.error("Phone Captcha Exception.", ex);
                    logger.info("Send Captcha[" + message + "] to Phone[" + entry.getKey() + "] via [" + provider
                            .name() + "] Failure");
                }

                @Override
                public void cancelled() {

                }
            });
        }
    }

    public enum SmsProvider {
        DIEXIN("http", "114.215.130.61:8082", "/SendMT/SendMessage",
                "UserName=benbangkeji&UserPass=diexin123&Mobile=%s&Content=%s", null),
        YUNPIAN("http", "yunpian.com", "/v1/sms/send.json",
                "apikey=17f40cb057d89301dc0aa60a25080003&mobile=%s&text=【赞赏网】%s", null),
        LUOSIMAO("http", "sms-api.luosimao.com", "/v1/send.json", "mobile=%s&message=%s【赞赏网】", new
                UsernamePasswordCredentials("api", "key-36c6ce2a843aa7b5ca17a98f8a948f24"));

        private URI originUri;

        private String paramString;

        private BasicAuthCache authCache;

        private CredentialsProvider credentialsProvider;

        SmsProvider(String scheme, String httpHost, String path, String paramString, Credentials credentials) {
            try {
                this.originUri = new URI(scheme + "://" + httpHost + path);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            this.paramString = paramString;
            if (credentials != null) {
                authCache = new BasicAuthCache();
                HttpHost host = new HttpHost(httpHost);
                authCache.put(host, new BasicScheme());
                BasicCredentialsProvider provider = new BasicCredentialsProvider();
                provider.setCredentials(new AuthScope(host), credentials);
                credentialsProvider = provider;
            }
        }

        public HttpClientContext context() {
            HttpClientContext httpClientContext = HttpClientContext.create();
            httpClientContext.setAuthCache(authCache);
            httpClientContext.setCredentialsProvider(credentialsProvider);
            return httpClientContext;
        }

        public HttpPost build(String phone, String content) {
            URIBuilder uri = new URIBuilder(originUri);
            String params = String.format(paramString, phone, content);
            String[] split = params.split("&");
            List<NameValuePair> paramList = new ArrayList<>();
            for (String namevalue : split) {
                String[] nv = namevalue.split("=");
                paramList.add(new BasicNameValuePair(nv[0], nv[1]));
            }
            try {
                final HttpPost httpPost = new HttpPost(uri.build());
                httpPost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                return httpPost;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
