package com.zanshang.framework.spring;

import com.zanshang.models.EPPassword;
import com.zanshang.models.EmailAccount;
import com.zanshang.models.PhoneAccount;
import com.zanshang.models.ZSAccount;
import com.zanshang.utils.PhoneValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.EmailValidator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Created by Lookis on 4/28/15.
 */
@Repository
public class MongodbUserDetailsManager implements UserDetailsManager {

    @Autowired
    MongoTemplate mongo;

    @Autowired
    PasswordEncoder encoder;

    protected final Log logger = LogFactory.getLog(this.getClass());

    private AuthenticationManager authenticationManager;



    @Override
    public void createUser(UserDetails userDetails) {
        validateUserDetails(userDetails);
        mongo.insert(userDetails);
        mongo.insert(((ZSAccount) userDetails).getEPPassword());
        mongo.insert(((ZSAccount) userDetails).getUserAuthorities());
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        validateUserDetails(userDetails);
        mongo.save(userDetails);
        mongo.save(((ZSAccount) userDetails).getEPPassword());
        mongo.save(((ZSAccount) userDetails).getUserAuthorities());
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        newPassword = encoder.encode(newPassword);
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can\'t change password as no Authentication object found in context for current user.");
        } else {
            String uid = currentUser.getName();    //here is uid for user
            this.logger.debug("Changing password for user \'" + uid + "\'");
            if (this.authenticationManager != null) {
                this.logger.debug("Reauthenticating user \'" + uid + "\' for password change request.");
                this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(uid, oldPassword));
            } else {
                this.logger.debug("No authentication manager set. Password won\'t be re-checked.");
            }
            this.logger.debug("Changing password for user \'" + uid + "\'");
            EPPassword newPasswordModel = new EPPassword(new ObjectId(uid), newPassword);
            mongo.save(newPasswordModel);
            SecurityContextHolder.getContext().setAuthentication(this.createNewAuthentication(currentUser, newPassword));
        }
    }

    //by email or phone
    @Override
    public boolean userExists(String s) {
        if (EmailValidator.getInstance().isValid(s)) {
            return mongo.findById(s, EmailAccount.class) != null;
        } else if (PhoneValidator.isValid(s)) {
            return mongo.findById(s, PhoneAccount.class) != null;
        } else {
            return false;
        }
    }

    //by email or phone
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetails details = null;
        if (EmailValidator.getInstance().isValid(s)) {
            details = (EmailAccount) mongo.findById(s, EmailAccount.class);
        } else if (PhoneValidator.isValid(s)) {
            details = (PhoneAccount) mongo.findById(s, PhoneAccount.class);
        }
        if (details == null)
            throw new UsernameNotFoundException(s);
        return details;
    }

    protected Authentication createNewAuthentication(Authentication currentAuth, String newPassword) {
        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(currentAuth.getPrincipal(), null, currentAuth.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());
        return newAuthentication;
    }

    private void validateUserDetails(UserDetails user) {
        Assert.hasText(user.getUsername(), "Username may not be empty or null");
        Assert.isTrue(user instanceof ZSAccount, "Not supported user");
        this.validateAuthorities(user.getAuthorities());
    }

    private void validateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Authorities list must not be null");
        Iterator i$ = authorities.iterator();

        while (i$.hasNext()) {
            GrantedAuthority authority = (GrantedAuthority) i$.next();
            Assert.notNull(authority, "Authorities list contains a null entry");
            Assert.hasText(authority.getAuthority(), "getAuthority() method must return a non-empty string");
        }
    }
}
