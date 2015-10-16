package com.zanshang.services.order;

import com.zanshang.framework.PaymentCallbackService;
import com.zanshang.models.Order;
import com.zanshang.services.OrderTrapdoor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by Lookis on 9/18/15.
 */
@Service
public class OrderPaymentCallbackService implements PaymentCallbackService {

    public static final String ORDERID = "orderId";

    OrderTrapdoor orderTrapdoor;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @PostConstruct
    protected void init() {
        orderTrapdoor = akkaTrapdoor.createTrapdoor(OrderTrapdoor.class, OrderTrapdoorImpl.class);
    }

    @Override
    public String getCallback(Map<String, String> arguments) {
        return "/orders/" +
                arguments.get(ORDERID) + "/status";
    }

    @Override
    public void paymentNotify(Map<String, String> arguments) {
        Order order = orderTrapdoor.get(new ObjectId(arguments.get("orderId")));
        order.setPaid(true);
        orderTrapdoor.save(order);
    }
}
