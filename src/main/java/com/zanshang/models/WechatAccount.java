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
 * Created by Lookis on 5/18/15.
 */
@Document(collection = "wechat_accounts")
public class WechatAccount implements UserDetails, ZSAccount {

    @Id
    @Field("username")
    private String username;

    @Field("uid")
    private ObjectId uid;

    @DBRef
    @Field("authorities")
    private UserAuthorities authorities;

    @Field("non_expired")
    private boolean nonExpired;

    @Field("non_locked")
    private boolean nonLocked;

    @Field("credentials_non_expired")
    private boolean credentialsNonExpired;

    @Field("enabled")
    private boolean enabled;

    private WechatAccount() {
    }

    public WechatAccount(ObjectId uid, String unionId) {
        this.uid = uid;
        this.authorities = new UserAuthorities(this.uid, Arrays.asList(new String[]{"USER"}));
        this.username = unionId;
        this.nonExpired = true;
        this.nonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    public WechatAccount(ObjectId uid, UserAuthorities authorities, String username, boolean nonExpired, boolean
            nonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.uid = uid;
        this.authorities = authorities;
        this.username = username;
        this.nonExpired = nonExpired;
        this.nonLocked = nonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.getAuthorities().parallelStream().map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return null;
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        WechatAccount that = (WechatAccount) o;

        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public EPPassword getEPPassword() {
        return null;
    }

    @Override
    public UserAuthorities getUserAuthorities() {
        return authorities;
    }

    @Override
    public ObjectId getUid() {
        return uid;
    }
}
