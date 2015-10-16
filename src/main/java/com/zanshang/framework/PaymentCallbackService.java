package com.zanshang.framework;

import java.util.Map;

/**
 * Created by Lookis on 9/18/15.
 */
public interface PaymentCallbackService {

    String getCallback(Map<String, String> arguments);

    void paymentNotify(Map<String, String> arguments) throws Exception;
}
