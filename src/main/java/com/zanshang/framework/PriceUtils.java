package com.zanshang.framework;

/**
 * Created by Lookis on 7/8/15.
 */
public class PriceUtils {

    public static Price add(Price p1, Price p2){
        return new Price(p1.getUnit().toCent(p1.getPrice())+ p2.getUnit().toCent(p2.getPrice()), PriceUnit.CENT);
    }

    public static Price multi(Price p, int factor){
        return new Price(p.getUnit().toCent(p.getPrice()) * factor, PriceUnit.CENT.CENT);
    }

    public static boolean gt(Price p1, Price p2) {
        return p1.getUnit().toCent(p1.getPrice()) > p2.getUnit().toCent(p2.getPrice());
    }

    public static boolean gte(Price p1, Price p2) {
        return p1.getUnit().toCent(p1.getPrice()) >= p2.getUnit().toCent(p2.getPrice());
    }

    public static boolean lt(Price p1, Price p2) {
        return p1.getUnit().toCent(p1.getPrice()) < p2.getUnit().toCent(p2.getPrice());
    }

    public static boolean lte(Price p1, Price p2) {
        return p1.getUnit().toCent(p1.getPrice()) <= p2.getUnit().toCent(p2.getPrice());
    }
}
