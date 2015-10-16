package com.zanshang;

/**
 * Created by xuming on 15/10/8.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class SendCommonPostMail {

    public static void main(String[] args) throws IOException {
        final String url = "http://sendcloud.sohu.com/webapi/mail.send.json";
        final String apiUser = "zanshang_test_test_Et748U";
        final String apiKey = "e52mprG2B1eSCj4L";

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);

        List params = new ArrayList();
        // 不同于登录SendCloud站点的帐号，您需要登录后台创建发信子帐号，使用子帐号和密码才可以进行邮件的发送。
        params.add(new BasicNameValuePair("api_user", apiUser));
        params.add(new BasicNameValuePair("api_key", apiKey));
        params.add(new BasicNameValuePair("from", "service@zan-shang.com"));
        params.add(new BasicNameValuePair("fromname", "service"));
        params.add(new BasicNameValuePair("to", "lookisliu@gmail.com"));
        params.add(new BasicNameValuePair("subject", "『赞赏』-您的项目已发布一周"));
        params.add(new BasicNameValuePair("html", "你太棒了！你已成功的从SendCloud发送了一封测试邮件，接下来快登录前台去完善账户信息吧！"));
        params.add(new BasicNameValuePair("resp_email_id", "true"));

        httpost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        // 请求
        HttpResponse response = httpclient.execute(httpost);
        // 处理响应
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 正常返回
            // 读取xml文档
            String result = EntityUtils.toString(response.getEntity());
            System.out.println(result);
        } else {
            System.err.println("error");
        }
        httpost.releaseConnection();
    }
}
