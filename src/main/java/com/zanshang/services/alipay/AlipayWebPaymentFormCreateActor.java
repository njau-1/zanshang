package com.zanshang.services.alipay;

import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 7/23/15.
 */
public class AlipayWebPaymentFormCreateActor extends AlipayPaymentFormCreateActor {

    public AlipayWebPaymentFormCreateActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    protected String getService() {
        return "create_direct_pay_by_user";
    }
}
