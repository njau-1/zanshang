package com.zanshang.services.wit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.zanshang.framework.PaymentCallbackService;
import com.zanshang.models.wit.WitOrder;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by xuming on 15/9/18.
 */
@Service
public class WitPaymentCallbackService implements PaymentCallbackService, ApplicationContextAware {

    public static final String ORDERID = "orderId";

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @Autowired
    MongoTemplate mongoTemplate;

    ActorRef witOrderSaveActor;

    @Override
    public String getCallback(Map<String, String> arguments) {
        return "/wit/orders/" +
                arguments.get(ORDERID) + "/status";
    }

    @Override
    public void paymentNotify(Map<String, String> arguments) {
        WitOrder order = mongoTemplate.findById(new ObjectId(arguments.get("orderId")), WitOrder.class);
        order.setPaid(true);
        witOrderSaveActor.tell(order, ActorRef.noSender());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        witOrderSaveActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(WitOrderSaveActor.class, applicationContext)));
    }
}
