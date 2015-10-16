package com.zanshang.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Lookis on 6/16/15.
 */
public class AlipayUtils {

    public static String currentTimestamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    public static String sign(Map<String, String> message, String secret) {
        SortedMap<String, String> sortedMap = new TreeMap<>(message);
        sortedMap.remove("sign");
        sortedMap.remove("sign_type");
        StringBuilder toSign = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getValue())) {
                toSign.append(entry.getKey());
                toSign.append("=");
                toSign.append(entry.getValue());
                toSign.append("&");
            }
        }
        toSign.deleteCharAt(toSign.length() - 1);
        toSign.append(secret);
        String signature = DigestUtils.md5Hex(toSign.toString());
        return signature;
    }

    public static URI buildIFrameURL(Map<String, String> params, String gateway) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(gateway);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            builder.addParameter(name, value);
        }
        return builder.build();
    }
}
