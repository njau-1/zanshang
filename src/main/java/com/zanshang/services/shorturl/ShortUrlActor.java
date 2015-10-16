package com.zanshang.services.shorturl;

import akka.actor.ActorRef;
import akka.actor.Status;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.utils.Json;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by xuming on 15/9/18.
 */
public class ShortUrlActor extends BaseUntypedActor {

    private final static String URL = "http://dwz.cn/create.php";

    public static final String PARAMNAME = "tinyurl";

    private CloseableHttpAsyncClient httpClient = HttpAsyncClients.createMinimal();

    public ShortUrlActor(ApplicationContext spring) {
        super(spring);
        httpClient.start();
    }

    @Override
    public void onReceive(Object o) throws Exception {
        String longurl = (String) o;
        ActorRef sender = getSender();
        ActorRef self = getSelf();
        HttpPost post = new HttpPost(URL);
        ArrayList<NameValuePair> postParameters;
        postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("url", longurl));
        post.setEntity(new UrlEncodedFormEntity(postParameters));
        httpClient.execute(post, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                try {
                    InputStream content = result.getEntity().getContent();
                    Map<String, Object> body = Json.fromJson(content, Map.class);
                    String tinyuri = (String) body.get(PARAMNAME);
                    if (tinyuri != null) {
                        sender.tell(tinyuri, self);
                    } else {
                        logger.debug("ShortUrl fetch error:" + content);
                        sender.tell(new Status.Failure(new RuntimeException("Error happend! see " +
                                "Actor log " + "for details.")), self);
                    }
                } catch (IOException e) {
                    logger.error("Ops. ShortUrl fetch error.", e);
                    sender.tell(new Status.Failure(new RuntimeException("Error " +
                            "happend! see " +
                            "Actor log " + "for details.")), self);
                }
            }

            @Override
            public void failed(Exception ex) {
                logger.error("ShortUrl failed.", ex);
                sender.tell(new Status.Failure(new RuntimeException("Error " +
                        "happend! see " +
                        "Actor log " + "for details.")), self);
            }

            @Override
            public void cancelled() {

            }
        });
    }
}
