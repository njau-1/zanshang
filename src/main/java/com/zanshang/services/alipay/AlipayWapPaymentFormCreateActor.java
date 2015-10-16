package com.zanshang.services.alipay;

import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 7/23/15.
 */
public class AlipayWapPaymentFormCreateActor extends AlipayPaymentFormCreateActor {

    public AlipayWapPaymentFormCreateActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    protected String getService() {
        return "alipay.wap.create.direct.pay.by.user";
    }
}
