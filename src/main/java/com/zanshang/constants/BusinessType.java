package com.zanshang.constants;

/**
 * Created by xuming on 15/8/25.
 */
public enum BusinessType {
    LOGIN("login");

    BusinessType(String businessType) {
        this.businessType = businessType;
    }

    private String businessType;

    public String getBusinessType() {
        return businessType;
    }
}
