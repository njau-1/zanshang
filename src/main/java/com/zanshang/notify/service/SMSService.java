package com.zanshang.notify.service;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.constants.NotificationType;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Notification;
import com.zanshang.models.Person;
import com.zanshang.models.PhoneAccount;
import com.zanshang.notify.MessageTemplateEngine;
import com.zanshang.notify.NotifyService;
import com.zanshang.notify.config.MessageTemplateConfig;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.notify.constants.NotifyParameter;
import com.zanshang.notify.constants.TemplateName;
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
import org.bson.types.ObjectId;
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
 * Created by xuming on 15/9/8.
 */
public class SMSService extends BaseUntypedActor implements NotifyService {

    final static NotificationType NOTIFICATION_TYPE = NotificationType.SMS;

    MessageTemplateConfig config;

    private CloseableHttpAsyncClient httpClient;

    public SMSService(ApplicationContext spring) {
        super(spring);
        config = spring.getBean(MessageTemplateConfig.class);
        httpClient = HttpAsyncClients.createDefault();
        httpClient.start();
    }

    @Override
    public void onReceive(Object o) throws Exception {
        NotifyParameter parameter = (NotifyParameter) o;
        send(parameter.getUid(), parameter.getNotifyBusinessType(), parameter.getModel());
    }

    @Override
    public void send(ObjectId uid, NotifyBusinessType notifyBusinessType, Map<String, String> model) {
        notifyBusinessType.setNotificationType(NOTIFICATION_TYPE);
        String message = getTemplate().render(notifyBusinessType.buildTemplateName(), model);
        Person person = getMongoTemplate().findById(uid, Person.class);
        String phone = person.getPhone();
        if (phone != null && !phone.isEmpty()) {
            sendSMS(phone, message, notifyBusinessType);
        } else {
            logger.info("Send SMS Notification Failed...");
            logger.info("User [" + uid + "] don`t have Phone ...");
        }
    }

    private void sendSMS(String phone, String message, NotifyBusinessType notifyBusinessType) {
        final SmsProvider provider = SmsProvider.values()[RandomUtils.nextInt(SmsProvider.values().length)];
        HttpPost httpPost = provider.build(phone, message);
        HttpClientContext context = provider.context();
        httpClient.execute(httpPost, context, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                logger.debug("Send SMS Done." + result);
                logger.info("Send SMS[" + message + "] Type:[" + notifyBusinessType.getTemplateName() + "] to Phone[" + phone + "] via [" + provider
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
                logger.error("Send SMS Exception.", ex);
                logger.info("Send SMS[" + message + "] Type:[" + notifyBusinessType.getTemplateName() + "] to Phone[" + phone + "] via [" + provider
                        .name() + "] Failure");
            }

            @Override
            public void cancelled() {

            }
        });
    }

    @Override
    public MessageTemplateEngine getTemplate() {
        return config.getTemplate(getClass());
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
