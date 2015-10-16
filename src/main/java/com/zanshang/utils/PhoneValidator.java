package com.zanshang.utils;

import java.util.regex.Pattern;

/**
 * Created by Lookis on 5/19/15.
 */
public class PhoneValidator {

    static Pattern p = Pattern.compile("^[1]([3|4|5|7|8])[0-9]{9}$");

    public static boolean isValid(String phoneNumber){
        return p.matcher(phoneNumber).find();
    }
}
