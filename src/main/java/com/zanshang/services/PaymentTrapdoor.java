package com.zanshang.services;

import com.zanshang.framework.PaymentType;
import com.zanshang.framework.Price;
import com.zanshang.models.Payment;
import org.bson.types.ObjectId;
import org.springframework.web.servlet.View;

import java.util.Map;

/**
 * Created by Lookis on 9/18/15.
 */
public interface PaymentTrapdoor {
    void save(Payment payment);

    Payment get(ObjectId key);

    View createPayment(String productName,Price price, PaymentType paymentType, Map<String, String> arguments, boolean mobile);
}
