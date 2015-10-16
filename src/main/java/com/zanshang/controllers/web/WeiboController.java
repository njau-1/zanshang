package com.zanshang.controllers.web;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zanshang.constants.Connect;
import com.zanshang.constants.WechatPlatform;
import com.zanshang.framework.weibo.WeiboOAuth2AuthenticationFilter;
import com.zanshang.models.Person;
import com.zanshang.services.OrderTrapdoor;
import com.zanshang.services.PersonTrapdoor;
import com.zanshang.services.WeiboAuthorizationTrapdoor;
import com.zanshang.services.order.OrderTrapdoorImpl;
import com.zanshang.services.person.PersonTrapdoorImpl;
import com.zanshang.services.weibo.WeiboAuthorizationTrapdoorImpl;
import com.zanshang.utils.AkkaTrapdoor;

/**
 * Created by xuming on 7/14/15.
 */
@Controller
@RequestMapping(value = WeiboController.WEIBO_PATH)
public class WeiboController {

    private Properties properties = new Properties();

    private String SERVER_CONTEXT;
    
    Logger logger = LoggerFactory.getLogger(getClass());

    public static final String WEIBO_PATH = "/weibo";

    public static final String NOTIFY_PATH = "/notify";

    public static final String CALLBACK_REQUEST_PATH = WEIBO_PATH + "/bind";
    
    public static final String UNBIND_PATH = WEIBO_PATH + "/unbind";

    @Autowired
    AkkaTrapdoor akkaTrapdoor;
    
    PersonTrapdoor personService;

    private WeiboAuthorizationTrapdoor weiboAuthorizationTrapdoor;

    @PostConstruct
    protected void init(){
        try {
            properties.load(new ClassPathResource("environment.properties").getInputStream());
            SERVER_CONTEXT = properties.getProperty("SERVER_CONTEXT");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        personService = akkaTrapdoor.createTrapdoor(PersonTrapdoor.class, PersonTrapdoorImpl.class);
        weiboAuthorizationTrapdoor = akkaTrapdoor.createTrapdoor(WeiboAuthorizationTrapdoor.class,
        		WeiboAuthorizationTrapdoorImpl.class);
    }
    
    //weibo 绑定回调处理
    //xuming weibo bind 07/14/15
    @RequestMapping(value = "/bind", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public String bind(HttpServletRequest request, Principal principal) {
        String callback = SERVER_CONTEXT + request.getContextPath() + WeiboOAuth2AuthenticationFilter.CALLBACK_REQUEST_PATH;
        String code = request.getParameter("code");
        Map<String, String> accessTokenMap = weiboAuthorizationTrapdoor.fetchAndSave(code, callback);
        ObjectId uid = new ObjectId(principal.getName());
        Person person = personService.get(uid);
        person.setWeiboId(accessTokenMap.get(Connect.WEIBO_ID_IN_ACCESSTOKEN));
        personService.save(person);
        return "redirect:/settings";
    }
    
    //weibo 解除绑定处理
    //xuming weibo bind 07/14/15
    @RequestMapping(value = "/unbind", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public String unbind(HttpServletRequest request, Principal principal) {
        ObjectId uid = new ObjectId(principal.getName());
        Person person = personService.get(uid);
        person.setWeiboId(null);
        personService.save(person);
        return "redirect:/settings";
    }
}
