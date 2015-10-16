package com.zanshang.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Lookis on 6/16/15.
 */
public class WechatUtils {

    public static String sign(Map<String, String> message, String secret) {
        SortedMap<String, String> sortedMap = new TreeMap<>(message);
        sortedMap.remove("sign");
        StringBuilder toSign = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getValue())) {
                toSign.append(entry.getKey());
                toSign.append("=");
                toSign.append(entry.getValue());
                toSign.append("&");
            }
        }
        toSign.append("key=");
        toSign.append(secret);
        String signature = DigestUtils.md5Hex(toSign.toString());
        return signature.toUpperCase();
    }

    public static String signSha1(Map<String, String> message, String jsApiTicket) {
        SortedMap<String, String> sortedMap = new TreeMap<>(message);
        sortedMap.remove("sign");
        sortedMap.put("jsapi_ticket", jsApiTicket);
        StringBuilder toSign = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getValue())) {
                toSign.append(entry.getKey().toLowerCase());
                toSign.append("=");
                toSign.append(entry.getValue());
                toSign.append("&");
            }
        }
        toSign.deleteCharAt(toSign.length()-1);
        String signature = DigestUtils.sha1Hex(toSign.toString());
        return signature.toUpperCase();
    }
}
