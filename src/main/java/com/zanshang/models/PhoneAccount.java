package com.zanshang.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by Lookis on 4/28/15.
 */
@Document(collection = "phone_accounts")
public class PhoneAccount implements UserDetails, ZSAccount {

    @Id
    @Field("phone")
    private String phone;

    @Field("uid")
    private ObjectId uid;

    @DBRef
    @Field("authorities")
    private UserAuthorities authorities;

    @DBRef
    @Field("password")
    private EPPassword password;

    @Field("non_expired")
    private boolean nonExpired;

    @Field("non_locked")
    private boolean nonLocked;

    @Field("credentials_non_expired")
    private boolean credentialsNonExpired;

    @Field("enabled")
    private boolean enabled;

    private PhoneAccount() {
    }

    public PhoneAccount(String phone, String strPassword) {
        this.uid = ObjectId.get();
        this.authorities = new UserAuthorities(this.uid, Arrays.asList(new String[]{"ROLE_USER"}));
        this.password = new EPPassword(this.uid, strPassword);
        this.phone = phone;
        this.nonExpired = true;
        this.nonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    public PhoneAccount(ObjectId uid, UserAuthorities authorities, EPPassword password, String phone, boolean nonExpired, boolean nonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.uid = uid;
        this.authorities = authorities;
        this.password = password;
        this.phone = phone;
        this.nonExpired = nonExpired;
        this.nonLocked = nonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    @Override
    public ObjectId getUid() {
        return uid;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.getAuthorities().parallelStream().map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password.getPassword();
    }

    @Override
    public String getUsername() {
        return uid.toHexString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return nonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public EPPassword getEPPassword() {
        return password;
    }

    @Override
    public UserAuthorities getUserAuthorities() {
        return authorities;
    }

    public String getPhone(){
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PhoneAccount that = (PhoneAccount) o;

        return phone.equals(that.phone);
    }

    @Override
    public int hashCode() {
        return phone.hashCode();
    }
}
