package com.zanshang.utils;

import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xuming on 15/9/23.
 */
public class UserCountUtils {

    final static Logger logger = LoggerFactory.getLogger(UserCountUtils.class);

    public final static String NOLGINUSERFLAG = "nologinUserFlag";

    public static void recordAccessUser(String ticket) {
        logger.info(ticket + ",");
    }

    public static void recordLoginUser(String ticket, String uid) {
        logger.info(ticket + "," + uid);
    }
}
