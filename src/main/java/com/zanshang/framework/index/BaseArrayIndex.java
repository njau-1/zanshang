package com.zanshang.framework.index;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lookis on 7/3/15.
 */
public abstract class BaseArrayIndex<K, V> {

    protected static final String VALUE_FIELD = "values";

    @Id
    private K key;

    @Field(VALUE_FIELD)
    private List<V> value;

    public BaseArrayIndex(K key) {
        this.key = key;
        this.value = new ArrayList<>();
    }

    public K getKey() {
        return key;
    }

    public List<V> getValue() {
        return value;
    }
}
