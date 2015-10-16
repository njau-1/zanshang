package com.zanshang.models.global;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by xuming on 15/8/11.
 */
@Document(collection = "globals")
public class CountPrice{

    @Id
    private String key;

    @Field("values")
    private long value;

    public final static String GLOBAL_KEY = "count_price";

    public CountPrice(long value) {
        this.key = GLOBAL_KEY;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getValueStr() {
        return String.valueOf(value);
    }
}
