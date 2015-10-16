package com.zanshang.services.payment;

import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.PaymentType;
import com.zanshang.framework.Price;
import com.zanshang.framework.RepositoryTrapdoor;
import com.zanshang.models.Payment;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import scala.concurrent.Await;

import java.util.Map;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 9/18/15.
 */
public class PaymentTrapdoorImpl extends RepositoryTrapdoor<ObjectId, Payment> implements com.zanshang.services
        .PaymentTrapdoor {

    public PaymentTrapdoorImpl(ApplicationContext springContext) {
        super(springContext);
    }

    @Override
    public Class<? extends BaseUntypedActor> getClz() {
        return PaymentGetActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> saveClz() {
        return PaymentSaveActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> deleteClz() {
        return null;
    }

    @Override
    public void save(Payment toSave) {
        try {
            Object result = Await.result(ask(save, toSave, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                    .DEFAULT_TIMEOUT_DURATION);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public View createPayment(String productName, Price price, PaymentType type, Map<String,String> arguments, boolean mobile) {
        Assert.hasLength(productName);
        Payment payment = new Payment(productName, price, type, arguments);
        save(payment);
        if (!mobile) {
            return new RedirectView("/payments/" + payment.getId());
        }else{
            return new RedirectView("/payments/mobile/?paymentId="+payment.getId());
        }
    }
}
