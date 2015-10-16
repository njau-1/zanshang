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
@Document(collection = "email_accounts")
public class EmailAccount implements UserDetails, ZSAccount {

    @Id
    @Field("email")
    private String email;

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

    private EmailAccount() {

    }

    public EmailAccount(String email, String strPassword) {
        this.uid = ObjectId.get();
        this.authorities = new UserAuthorities(this.uid, Arrays.asList(new String[]{"ROLE_USER"}));
        this.password = new EPPassword(this.uid, strPassword);
        this.email = email;
        this.nonExpired = true;
        this.nonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    public EmailAccount(ObjectId uid, UserAuthorities authorities, EPPassword password, String email, boolean nonExpired, boolean nonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.uid = uid;
        this.authorities = authorities;
        this.password = password;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setNonLocked(boolean nonLocked) {
        this.nonLocked = nonLocked;
    }

    public void setUid(ObjectId uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        EmailAccount that = (EmailAccount) o;

        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
