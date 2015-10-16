package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Lookis on 5/9/15.
 */
@Document(collection = "companies")
public class Company {

    @Id
    @Field("uid")
    private ObjectId uid;

    @Field("company_name")
    private String companyName;

    @Field("company_code")
    private String companyCode;

    @Field("contact_phone")
    private String contactPhone;

    @Field("license")
    private String license;

    private Company() {
    }

    public Company(ObjectId uid, String companyName, String companyCode, String contactName, String contactPhone,
                   String license) {
        this.uid = uid;
        this.companyName = companyName;
        this.companyCode = companyCode;
        this.contactPhone = contactPhone;
        this.license = license;
    }

    public ObjectId getUid() {
        return uid;
    }

    public void setUid(ObjectId uid) {
        this.uid = uid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Company company = (Company) o;

        return uid.equals(company.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
