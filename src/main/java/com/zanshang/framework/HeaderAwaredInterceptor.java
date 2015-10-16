package com.zanshang.framework;

import com.zanshang.models.Person;
import com.zanshang.models.Setting;
import com.zanshang.services.NotificationTrapdoor;
import com.zanshang.services.PersonTrapdoor;
import com.zanshang.services.SettingTrapdoor;
import com.zanshang.services.notification.NotificationTrapdoorImpl;
import com.zanshang.services.person.PersonTrapdoorImpl;
import com.zanshang.services.setting.SettingTrapdoorImpl;
import com.zanshang.utils.AkkaTrapdoor;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Lookis on 5/26/15.
 */
public class HeaderAwaredInterceptor implements HandlerInterceptor {

    SettingTrapdoor settingTrapdoor;

    PersonTrapdoor personTrapdoor;

    NotificationTrapdoor notificationTrapdoor;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @PostConstruct
    protected void construct() {
        settingTrapdoor = akkaTrapdoor.createTrapdoor(SettingTrapdoor.class, SettingTrapdoorImpl.class);
        personTrapdoor = akkaTrapdoor.createTrapdoor(PersonTrapdoor.class, PersonTrapdoorImpl.class);
        notificationTrapdoor = akkaTrapdoor.createTrapdoor(NotificationTrapdoor.class, NotificationTrapdoorImpl.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {
        String userId = request.getRemoteUser();
        if (StringUtils.isNotEmpty(userId) && modelAndView != null) {
            ObjectId uid = new ObjectId(userId);
            Setting setting = settingTrapdoor.get(uid);
            Person person = personTrapdoor.get(uid);
            boolean hasNews = notificationTrapdoor.hasNews(uid);
            modelAndView.addObject("visitor", setting);
            request.setAttribute("isPerson", person != null);
            request.setAttribute("hasNews", hasNews);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
            ex) throws Exception {

    }
}
