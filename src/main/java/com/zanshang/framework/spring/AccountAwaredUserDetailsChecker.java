package com.zanshang.framework.spring;

import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by Lookis on 6/1/15.
 */
public class AccountAwaredUserDetailsChecker extends AccountStatusUserDetailsChecker {

    @Override
    public void check(UserDetails user) {
        try {
            super.check(user);
        } catch (AccountStatusException e) {
            throw new AccountAwaredAccountStatusException(e.getMessage(), user, e);
        }
    }
}
