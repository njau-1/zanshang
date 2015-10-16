package com.zanshang.framework;

/**
 * Created by Lookis on 7/8/15.
 */
public class Price implements Comparable<Price> {

    public static final Price ZERO = new Price(0, PriceUnit.CENT);

    private long price;

    private PriceUnit unit;

    public Price(long price, PriceUnit unit) {
        this.price = price;
        this.unit = unit;
    }

    public long getPrice() {
        return price;
    }

    public PriceUnit getUnit() {
        return unit;
    }

    public long to(PriceUnit toUnit){
        switch (toUnit){
            case YUAN:
                return unit.toYuan(price);
            case CENT:
                return unit.toCent(price);
        }
        throw new IllegalArgumentException();
    }

    public Long to(String toUnit){
        for (PriceUnit priceUnit : PriceUnit.values()) {
            if(priceUnit.name().equalsIgnoreCase(toUnit)){
                return to(priceUnit);
            }
        }
        return null;
    }

    @Override
    public int compareTo(Price o) {
        return Long.compare(unit.toCent(price), o.getUnit().toCent(o.getPrice()));
    }

}
