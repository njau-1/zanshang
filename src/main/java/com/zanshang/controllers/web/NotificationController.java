package com.zanshang.controllers.web;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.PriceUnit;
import com.zanshang.models.Notification;
import com.zanshang.models.NotificationEvent;
import com.zanshang.models.Project;
import com.zanshang.notify.Notifier;
import com.zanshang.notify.constants.FreeMarkerModelParamKey;
import com.zanshang.notify.template.FreeMarkerTemplateEngine;
import com.zanshang.services.NotificationTrapdoor;
import com.zanshang.services.notification.NotificationTrapdoorImpl;
import com.zanshang.services.notificationevent.NotificationEventFindByDateActor;
import com.zanshang.services.notificationevent.NotificationEventGetActor;
import com.zanshang.utils.Ajax;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import scala.concurrent.Await;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Lookis on 5/14/15.
 */
@Controller
@RequestMapping("/notifications")
public class NotificationController implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    MessageSource messageSource;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ApplicationContext spring;

    @Autowired
    Notifier notifier;

    @Autowired
    FreeMarkerTemplateEngine freeMarkerTemplateEngine;

    NotificationTrapdoor notificationService;

    ActorRef notificationEventGetActor;

    ActorRef notificationEventFindByDateActor;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    protected void init() {
        notificationService = akkaTrapdoor.createTrapdoor(NotificationTrapdoor.class, NotificationTrapdoorImpl.class);
        notificationEventGetActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(NotificationEventGetActor.class, applicationContext)));
        notificationEventFindByDateActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(NotificationEventFindByDateActor.class, applicationContext)));
    }

    @RequestMapping(method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public ModelAndView list(@PageableDefault Pageable pageable, Principal principal, Locale locale) {
        Page<Notification> notificationPage = notificationService.findByUid(new ObjectId(principal.getName()), pageable);
        ModelAndView mav = new ModelAndView("9_1");
        Page<NotificationView> notificationViews = notificationPage.map(notification -> {
            String content = freeMarkerTemplateEngine.render(notification.getTemplateName(), notification.getParamMap());
            return new NotificationView(notification.getId(), notification.getUid(), content, notification.getCreateTime(), notification.isRead());
        });
        mav.addObject("content", notificationViews);
        return mav;
    }


    @RequestMapping(value = "/read", method=RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object markread(Principal principal, @RequestParam("ids[]") List<String> ids){
        List<ObjectId> objectIds = ids.stream().map((id -> new ObjectId(id))).collect(Collectors.toList());
        notificationService.markRead(objectIds);
        return Ajax.ok();
    }


    @RequestMapping(value = "/mail", method=RequestMethod.GET)
    @ResponseBody
    public Object alertMail(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println(sdf.format(new Date()));
        try {
            Object result = Await.result(Patterns.ask(notificationEventFindByDateActor, sdf.format(new Date()), ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            if (result != null) {
                List<NotificationEvent> notificationEvents = (List<NotificationEvent>) result;
                for (NotificationEvent ne: notificationEvents) {
                    if (!ne.getIsSend()) {
                        Project project = mongoTemplate.findById(new ObjectId(ne.getParamMap().get(FreeMarkerModelParamKey.PROJECTID.getKey())), Project.class);
                        ne.getParamMap().put(FreeMarkerModelParamKey.CURRENTBALANCE.getKey(), String.valueOf(project.getCurrentBalance().to(PriceUnit.YUAN)));
                        notifier.notify(ne.getUid(), ne.getNotifyBusinessType(), ne.getParamMap());
                        ne.setIsSend(true);
                        mongoTemplate.save(ne);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("send alert email exception...  date:" + sdf.format(new Date()), e);
            return Ajax.failure("faile");
        }
        return Ajax.ok();
    }

    public static class NotificationView {

        private ObjectId id;

        private ObjectId uid;

        private String content;

        private Date createTime;

        private boolean read;

        public NotificationView(ObjectId id, ObjectId uid, String content, Date createTime, boolean read) {
            this.id = id;
            this.uid = uid;
            this.content = content;
            this.createTime = createTime;
            this.read = read;
        }

        public ObjectId getId() {
            return id;
        }

        public void setId(ObjectId id) {
            this.id = id;
        }

        public ObjectId getUid() {
            return uid;
        }

        public void setUid(ObjectId uid) {
            this.uid = uid;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public boolean isRead() {
            return read;
        }

        public void setRead(boolean read) {
            this.read = read;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
