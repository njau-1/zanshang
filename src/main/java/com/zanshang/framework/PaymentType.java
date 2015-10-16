package com.zanshang.framework;

import com.zanshang.services.order.OrderPaymentCallbackService;
import com.zanshang.services.wit.WitPaymentCallbackService;

/**
 * Created by Lookis on 9/18/15.
 */
public enum PaymentType {

    Order(OrderPaymentCallbackService.class), Wit(WitPaymentCallbackService.class);

    private Class<? extends PaymentCallbackService> clz;

    PaymentType(Class<? extends PaymentCallbackService> clz) {
        this.clz = clz;
    }

    public Class<? extends PaymentCallbackService> getClz() {
        return clz;
    }
}
