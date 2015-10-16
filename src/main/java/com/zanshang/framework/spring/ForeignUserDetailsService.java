package com.zanshang.framework.spring;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

/**
 * Created by Lookis on 5/5/15.
 */
public interface ForeignUserDetailsService extends UserDetailsService{

    UserDetails loadUserByAccessToken(Map<String, Object> accessTokenMap) throws ForeignUsernameNotFoundException;
    /**
     * Create a new user with the supplied details.
     */
    void createUser(UserDetails user);

    /**
     * Remove the user with the given login name from the system.
     */
    void deleteUser(String username);

    /**
     * Check if a user with the supplied login name exists in the system.
     */
    boolean userExists(String username);
}
