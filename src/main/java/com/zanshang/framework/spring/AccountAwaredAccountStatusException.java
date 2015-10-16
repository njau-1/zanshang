package com.zanshang.framework.spring;

import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by Lookis on 6/1/15.
 */
public class AccountAwaredAccountStatusException extends AccountStatusException{

    private UserDetails userDetails;

    public AccountAwaredAccountStatusException(String msg, UserDetails userDetails) {
        super(msg);
        this.userDetails = userDetails;
    }

    public AccountAwaredAccountStatusException(String msg, UserDetails userDetails, AccountStatusException t) {
        super(msg, t);
        this.userDetails = userDetails;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }
}
