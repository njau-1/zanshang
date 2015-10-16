package com.zanshang.framework.wechat;

import com.zanshang.constants.Connect;
import com.zanshang.framework.spring.ForeignUserDetailsService;
import com.zanshang.framework.spring.ForeignUsernameNotFoundException;
import com.zanshang.models.WechatAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by Lookis on 5/18/15.
 */
@Service
public class WechatUserDetailsService implements ForeignUserDetailsService {

    @Autowired
    MongoTemplate mongo;

    @Override
    public UserDetails loadUserByAccessToken(Map<String, Object> accessTokenMap) throws ForeignUsernameNotFoundException {
        UserDetails details = null;
        try {
            details = loadUserByUsername((String) accessTokenMap.get(Connect.WECHAT_ID_IN_ACCESSTOKEN));
        } catch (UsernameNotFoundException e) {
            throw new ForeignUsernameNotFoundException(accessTokenMap, e);
        }
        return details;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WechatAccount account =  mongo.findById(username, WechatAccount.class);
        if (account == null)
            throw new UsernameNotFoundException(username);
        return account;
    }

    @Override
    public void createUser(UserDetails user) {
        Assert.isInstanceOf(WechatAccount.class, user);
        mongo.insert(user);
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException(username);
    }

    //unionId
    @Override
    public boolean userExists(String username) {
        return mongo.findById(username, WechatAccount.class) != null;
    }
}
