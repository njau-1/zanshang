package com.zanshang.models;

import com.zanshang.framework.PaymentType;
import com.zanshang.framework.Price;
import com.zanshang.utils.Json;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

/**
 * Created by Lookis on 9/18/15.
 */
@Document(collection = "payments")
public class Payment {

    @Id
    private ObjectId id;

    @Field("product_name")
    private String productName;

    @Field("price")
    private Price price;

    @Field("paid")
    private boolean paid;

    @Field("type")
    private PaymentType type;

    @Field("arguments")
    private String arguments;

    private Payment() {
    }

    public Payment(String productName, Price price, PaymentType type, Map<String,String> arguments) {
        this.id = ObjectId.get();
        this.productName = productName;
        this.price = price;
        this.paid = false;
        this.type = type;
        this.arguments = Json.toJson(arguments);
    }

    public String getProductName() {
        return productName;
    }

    public Price getPrice() {
        return price;
    }

    public ObjectId getId() {
        return id;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isPaid() {
        return paid;
    }

    public PaymentType getType() {
        return type;
    }

    public Map<String,String> getArguments() {
        return Json.fromJson(this.arguments, Map.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Payment payment = (Payment) o;

        return !(id != null ? !id.equals(payment.id) : payment.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
