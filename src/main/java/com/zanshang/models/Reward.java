package com.zanshang.models;

import com.zanshang.framework.Price;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

/**
 * Created by Lookis on 5/26/15.
 */
@Document(collection = "rewards")
public class Reward {

    public static final Integer UNCOUNTABLE = Integer.MAX_VALUE;

    public static final String PRICE_FIELD = "price";

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Reward reward = (Reward) o;

        return id.equals(reward.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public enum Item {
        BOOK, VIP, SIGNATURE, OTHER;
    }

    public static class Detail {

        private int count;

        private String detail;

        private Detail() {
        }

        public Detail(int count, String detail) {
            this.count = count;
            this.detail = detail;
        }

        public int getCount() {
            return count;
        }

        public String getDetail() {
            return detail;
        }
    }

    @Id
    @Field("id")
    private ObjectId id;

    @Field("project_id")
    private ObjectId projectId;

    @Field("items")
    private SortedMap<Item, Detail> items;

    //总量，默认不限
    @Field("count")
    private int count;

    @Field(PRICE_FIELD)
    private Price price;

    private Reward() {
    }

    public Reward(ObjectId projectId, Map<Item, Detail> items, Price price) {
        this.id = ObjectId.get();
        this.projectId = projectId;
        this.items = new TreeMap<>(items);
        this.price = price;
        this.count = UNCOUNTABLE;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public Map<String, Detail> getItems() {
        Map<String, Detail> retMap = new LinkedHashMap<>();
        items.entrySet().forEach(itemDetailEntry -> retMap.put(itemDetailEntry.getKey().toString(), itemDetailEntry
                .getValue()));
        return retMap;
    }

    public void setItems(Map<Item, Detail> items) {
        this.items = new TreeMap<>(items);
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
