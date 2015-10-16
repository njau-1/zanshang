package com.zanshang.controllers.web;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.constants.NotificationConstants;
import com.zanshang.framework.Price;
import com.zanshang.framework.PriceUnit;
import com.zanshang.framework.PriceUtils;
import com.zanshang.framework.index.IndexPageableParams;
import com.zanshang.models.*;
import com.zanshang.notify.Notifier;
import com.zanshang.notify.constants.FreeMarkerModelParamKey;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.services.*;
import com.zanshang.services.address.AddressTrapdoorImpl;
import com.zanshang.services.message.MessageTrapdoorImpl;
import com.zanshang.services.order.OrderFindByUidActor;
import com.zanshang.services.order.OrderTrapdoorImpl;
import com.zanshang.services.project.ProjectFindByPublisherIdPageableActor;
import com.zanshang.services.project.ProjectFindByUidPageableActor;
import com.zanshang.services.project.ProjectTrapdoorImpl;
import com.zanshang.services.setting.SettingTrapdoorImpl;
import com.zanshang.utils.Ajax;
import com.zanshang.utils.AkkaTrapdoor;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Lookis on 7/3/15.
 */
@Controller
@RequestMapping("/profile")
public class ProfileController implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(getClass());

    ProjectTrapdoor projectTrapdoor;

    AddressTrapdoor addressService;

    MessageTrapdoor messageTrapdoor;

    SettingTrapdoor settingTrapdoor;

    OrderTrapdoor orderTrapdoor;

    ActorRef projectByUidActor;

    ActorRef orderByUidActor;

    ActorRef projectByPublisherActor;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MessageSource messageSource;

    @Autowired
    Notifier notifier;

    private ApplicationContext applicationContext;

    @PostConstruct
    protected void init() {
        projectTrapdoor = akkaTrapdoor.createTrapdoor(ProjectTrapdoor.class, ProjectTrapdoorImpl.class);
        addressService = akkaTrapdoor.createTrapdoor(AddressTrapdoor.class, AddressTrapdoorImpl.class);
        messageTrapdoor = akkaTrapdoor.createTrapdoor(MessageTrapdoor.class, MessageTrapdoorImpl.class);
        settingTrapdoor = akkaTrapdoor.createTrapdoor(SettingTrapdoor.class, SettingTrapdoorImpl.class);
        orderTrapdoor = akkaTrapdoor.createTrapdoor(OrderTrapdoor.class, OrderTrapdoorImpl.class);
        projectByUidActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(ProjectFindByUidPageableActor
                .class, applicationContext)));
        orderByUidActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(OrderFindByUidActor.class,
                applicationContext)));
        projectByPublisherActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator
                (ProjectFindByPublisherIdPageableActor.class, applicationContext)));
    }

    @RequestMapping("/{profileId}")
    public ModelAndView profile(Principal principal, @PathVariable("profileId") String profileId, @PageableDefault
    Pageable pageable) {
        Assert.isTrue(ObjectId.isValid(profileId));
        Map<String, Object> retMap = new ConcurrentHashMap<>();
        //paid projects
        Future<Object> orderFuture = Patterns.ask(orderByUidActor, new IndexPageableParams(pageable, new ObjectId
                (profileId)), ActorConstant.DEFAULT_TIMEOUT);
        List<Future<Object>> futures = new ArrayList<>();
        futures.add(orderFuture.andThen(new OnComplete<Object>() {
            @Override
            public void onComplete(Throwable throwable, Object o) throws Throwable {
                if (o != null) {
                    Page<Order> orders = (Page<Order>) o;
                    List<Order> content = orders.getContent();
                    List<ObjectId> rewardIds = content.stream().map((order -> order.getRewardId())).collect
                            (Collectors.toList());
                    List<Reward> rewards = mongoTemplate.find(Query.query(Criteria.where("_id").in(rewardIds)),
                            Reward.class);
                    List<ObjectId> projectIds = rewards.stream().map((reward -> reward.getProjectId())).collect
                            (Collectors.toList());
                    Map<ObjectId, Project> projects = projectTrapdoor.findByIds(projectIds);
                    retMap.put("paidProjects", projects.values());
                    retMap.put("paidSize",orders.getTotalElements());
                } else {
                    logger.error("Future Error.", throwable);
                }
            }
        }, actorSystem.dispatcher()));
        //Own projects
        Future<Object> projectByUidFuture = Patterns.ask(projectByUidActor, new IndexPageableParams(pageable, new
                ObjectId(profileId)), ActorConstant.DEFAULT_TIMEOUT);
        futures.add(projectByUidFuture.andThen(new OnComplete<Object>() {
            @Override
            public void onComplete(Throwable throwable, Object o) throws Throwable {
                if (o != null) {
                    Page<Project> projects = (Page<Project>) o;
                    retMap.put("ownProjects", projects.getContent());
                    retMap.put("ownSize", projects.getTotalElements());
                } else {
                    logger.error("Future Error.", throwable);
                }
            }
        }, actorSystem.dispatcher()));
        //Published projects
        Future<Object> projectByPublisherId = Patterns.ask(projectByPublisherActor, new IndexPageableParams(pageable,
                new ObjectId(profileId)), ActorConstant.DEFAULT_TIMEOUT);
        futures.add(projectByPublisherId.andThen(new OnComplete<Object>() {
            @Override
            public void onComplete(Throwable throwable, Object o) throws Throwable {
                if (o != null) {
                    Page<Project> projects = (Page<Project>) o;
                    retMap.put("publishProjects", projects.getContent());
                    retMap.put("publishSize", projects.getTotalElements());
                } else {
                    logger.error("Future Error.", throwable);
                }
            }
        }, actorSystem.dispatcher()));
        //if logined, return message list
        ModelAndView mav = new ModelAndView("10_1");
        Setting profileSetting = settingTrapdoor.get(new ObjectId(profileId));
        if (principal != null) {
            ObjectId visitor = new ObjectId(principal.getName());
            Page<Message> messages = messageTrapdoor.findBySenderAndRecipient(visitor, new ObjectId(profileId),
                    pageable);
            Setting me = settingTrapdoor.get(visitor);
            Map<ObjectId, Setting> settingMap = new HashMap<>();
            settingMap.put(me.getUid(), me);
            settingMap.put(profileSetting.getUid(), profileSetting);
            Page<MessageView> messageViewPage = messages.map(message -> new MessageView(settingMap.get(message
                    .getSender()).getDisplayName(), settingMap.get(message.getSender()).getAvatar(), message.getTime
                    (), message.getMessage(), message.getSender().equals(visitor)));
            mav.addObject("messages", messageViewPage);
            mav.addObject("visitor", visitor);
        }
        try {
            Await.ready(Futures.sequence(futures, actorSystem.dispatcher()), Duration.Inf());
        } catch (Exception e) {
            logger.error("Future Error.", e);
        }
        mav.addAllObjects(retMap);
        mav.addObject("setting", profileSetting);
        return mav;
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public ModelAndView project(Principal principal, Pageable page, Locale locale) throws Exception {
        ModelAndView mav = new ModelAndView("8_1");
        Page<Project> byUid = projectTrapdoor.findByUid(new ObjectId(principal.getName()), page);
        mav.addObject("content", buildViewFromProject(byUid, locale));
        mav.addObject("tab", "projects");
        return mav;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public ModelAndView order(Principal principal, Pageable page, Locale locale) {
        ModelAndView mav = new ModelAndView("8_1");
        Page<Order> orderPage = orderTrapdoor.findByUid(new ObjectId(principal.getName()), page);
        List<Address> addressList = addressService.findByUid(new ObjectId(principal.getName()));
        mav.addObject("addresses", addressList);
        mav.addObject("content", buildViewFromOrder(orderPage, locale));
        mav.addObject("tab", "orders");
        return mav;
    }

    @RequestMapping(value = "/published", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public ModelAndView published(Principal principal, Pageable page, Locale locale) {
        ModelAndView mav = new ModelAndView("8_1");
        Page<Project> projectPage = projectTrapdoor.findByPublisherId(new ObjectId(principal.getName()), page);
        mav.addObject("content", buildViewFromPublished(projectPage, locale));
        mav.addObject("tab", "published");
        return mav;
    }

    //创建新收获地址,并变更本订单收货地址
    @RequestMapping(value = "/orders/{orderId}/address", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object createAddressForOrders(@PathVariable("orderId") String orderId, @RequestParam("address") String
            address, @RequestParam("postCode") String postCode, @RequestParam("recipient") String recipient,
                                       @RequestParam("phone") String phone, Principal principal, Locale locale) {
        Order order = orderTrapdoor.get(new ObjectId(orderId));
        if (StringUtils.equals(order.getUid().toHexString(), principal.getName())) {
            order.setAddress(recipient + "," + phone + "," + address + "," + postCode);
            Address addr = new Address(new ObjectId(principal.getName()), recipient, phone, postCode, address);
            addressService.save(addr, true);
            orderTrapdoor.save(order);
            return Ajax.ok();
        } else {
            return Ajax.failure(messageSource.getMessage("order.address.noaccess", null, locale));
        }
    }

    //更新本订单收货地址
    @RequestMapping(value = "/orders/{orderId}/address", method = RequestMethod.PUT)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object profileOrdersAddress(@PathVariable("orderId") String orderId, @RequestParam("address") String
            address, Principal principal, Locale locale) {
        Order order = orderTrapdoor.get(new ObjectId(orderId));
        if (StringUtils.equals(order.getUid().toHexString(), principal.getName())) {
            order.setAddress(address);
            orderTrapdoor.save(order);
            return Ajax.ok();
        } else {
            return Ajax.failure(messageSource.getMessage("order.address.noaccess", null, locale));
        }
    }

    @RequestMapping(value = "/{profileId}/message", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_USER")
    public Object profileMessage(@RequestParam("message") String message, @PathVariable("profileId") String
            profileId, Principal principal) {
        messageTrapdoor.save(new Message(new ObjectId(principal.getName()), new ObjectId(profileId), message));
        //notification
        Setting senderSetting = settingTrapdoor.get(new ObjectId(principal.getName()));
        Map<String, String> model = new HashMap<String, String>();
        model.put(FreeMarkerModelParamKey.SENDERNAME.getKey(), senderSetting.getDisplayName());
        model.put(FreeMarkerModelParamKey.SENDERUID.getKey(), principal.getName());
        notifier.notify(new ObjectId(profileId), NotifyBusinessType.PERSONAL_NEW_CHAT, model);
        return Ajax.ok();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Page<ParticipateProjectView> buildViewFromPublished(Page<Project> projectPage, Locale locale) {
        List<ObjectId> projectIds = projectPage.getContent().parallelStream().map((project -> project.getId()))
                .collect(Collectors.toList());
        List<ProjectFeedback> feedbacks = mongoTemplate.find(Query.query(Criteria.where("_id").in(projectIds)),
                ProjectFeedback.class);
        Map<ObjectId, ProjectFeedback> feedbackMap = new HashMap<>();
        for (ProjectFeedback feedback : feedbacks) {
            feedbackMap.put(feedback.getProjectId(), feedback);
        }
        Map<ObjectId, Price> paidsMap = projectTrapdoor.getPaids(projectIds);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        return projectPage.map(p -> {
            Integer daysLeft = null;
            if (p.getDeadline() != null) {
                if (now.before(p.getDeadline())) {
                    daysLeft = (int) TimeUnit.DAYS.convert(p.getDeadline().getTime() - now.getTime(), TimeUnit
                            .MILLISECONDS);
                } else {
                    daysLeft = 0;
                }
            }
            return new ParticipateProjectView(p.getId(), sdf.format(p.getCreateTime()), p.getCover(), daysLeft,
                    p.getCurrentBalance() != null ? p.getCurrentBalance().to(PriceUnit.YUAN) : Price.ZERO.to
                            (PriceUnit.YUAN), p.getGoal().to(PriceUnit.YUAN), feedbackMap.containsKey(p.getId()) ?
                    feedbackMap.get(p.getId()).getGoal().to(PriceUnit.YUAN) : null, feedbackMap.containsKey(p.getId()
            ) ? feedbackMap.get(p.getId()).getDetails() : null, messageSource.getMessage(p.getState().getMessageCode
                    (), null, locale), p.getState().name().toLowerCase());
        });
    }

    private Page<ParticipateProjectView> buildViewFromProject(Page<Project> projectPage, Locale locale) {
        List<ObjectId> projectIds = projectPage.getContent().parallelStream().map((project -> project.getId()))
                .collect(Collectors.toList());
        List<ProjectFeedback> feedbacks = mongoTemplate.find(Query.query(Criteria.where("_id").in(projectIds)),
                ProjectFeedback.class);
        Map<ObjectId, ProjectFeedback> feedbackMap = new HashMap<>();
        for (ProjectFeedback feedback : feedbacks) {
            feedbackMap.put(feedback.getProjectId(), feedback);
        }
        Map<ObjectId, Price> paidsMap = projectTrapdoor.getPaids(projectIds);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        return projectPage.map(p -> {
            Integer daysLeft = null;
            if (p.getDeadline() != null) {
                if (now.before(p.getDeadline())) {
                    daysLeft = (int) TimeUnit.DAYS.convert(p.getDeadline().getTime() - now.getTime(), TimeUnit
                            .MILLISECONDS);
                } else {
                    daysLeft = 0;
                }
            }
            return new ParticipateProjectView(p.getId(), sdf.format(p.getCreateTime()), p.getCover(), daysLeft,
                    p.getCurrentBalance() != null ? p.getCurrentBalance().to(PriceUnit.YUAN) : Price.ZERO.to
                            (PriceUnit.YUAN), p.getGoal() == null ? null : p.getGoal().to(PriceUnit.YUAN),
                    feedbackMap.containsKey(p.getId()) ? feedbackMap.get(p.getId()).getGoal().to(PriceUnit.YUAN) :
                            null, feedbackMap.containsKey(p.getId()) ? feedbackMap.get(p.getId()).getDetails() :
                    null, messageSource.getMessage(p.getState().getMessageCode(), null, locale), p.getState().name()
                    .toLowerCase());
        });
    }

    private Page<OrderProjectView> buildViewFromOrder(Page<Order> orderPage, Locale locale) {
        List<ObjectId> rewardIds = new ArrayList<>();
        orderPage.getContent().forEach(order -> rewardIds.add(order.getRewardId()));
        //reward
        List<Reward> rewards = mongoTemplate.find(Query.query(Criteria.where("_id").in(rewardIds)), Reward.class);
        Map<ObjectId, ObjectId> rewardIdToProjectMap = new HashMap<>();
        Map<ObjectId, Reward> rewardMap = new HashMap<>();
        List<ObjectId> projectIds = new ArrayList<>();
        rewards.forEach(reward -> {
            projectIds.add(reward.getProjectId());
            rewardIdToProjectMap.put(reward.getId(), reward.getProjectId());
            rewardMap.put(reward.getId(), reward);
        });
        //project
        Map<ObjectId, Project> projectMap = projectTrapdoor.findByIds(projectIds);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        return orderPage.map(source -> {
            Project project = projectMap.get(rewardIdToProjectMap.get(source.getRewardId()));
            Reward reward = rewardMap.get(source.getRewardId());
            Integer daysLeft = null;
            if (project.getDeadline() != null) {
                if (now.before(project.getDeadline())) {
                    daysLeft = (int) TimeUnit.DAYS.convert(project.getDeadline().getTime() - now.getTime(), TimeUnit
                            .MILLISECONDS);
                } else {
                    daysLeft = 0;
                }
            }
            return new OrderProjectView(source.getId(), project.getId(), project.getBookName(), sdf.format(source
                    .getCreateTime()), project.getCover(), reward, source.getAddress(), source.getCount(), PriceUtils
                    .multi(reward.getPrice(), source.getCount()).to(PriceUnit.YUAN), messageSource.getMessage(project
                    .getState().getMessageCode(), null, locale), project.getState().name().toLowerCase(), daysLeft);
        });
    }

    public static class OrderProjectView {

        private ObjectId projectId;

        private ObjectId orderId;

        private String bookName;

        private String createTime;

        private String cover;

        private Reward reward;

        private String address;

        private int count;

        private long price;

        private String strState;

        private String state;

        private Integer daysLeft;

        public OrderProjectView(ObjectId orderId, ObjectId projectId, String bookName, String createTime, String
                cover, Reward reward, String address, int count, long price, String strState, String state, Integer
                daysLeft) {
            this.orderId = orderId;
            this.bookName = bookName;
            this.projectId = projectId;
            this.createTime = createTime;
            this.cover = cover;
            this.reward = reward;
            this.address = address;
            this.count = count;
            this.price = price;
            this.strState = strState;
            this.state = state;
            this.daysLeft = daysLeft;
        }

        public ObjectId getOrderId() {

            return orderId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public ObjectId getProjectId() {
            return projectId;
        }

        public String getBookName() {
            return bookName;
        }

        public String getCover() {
            return cover;
        }

        public Reward getReward() {
            return reward;
        }

        public String getAddress() {
            return address;
        }

        public int getCount() {
            return count;
        }

        public long getPrice() {
            return price;
        }

        public String getStrState() {
            return strState;
        }

        public String getState() {
            return state;
        }

        public Integer getDaysLeft() {
            return daysLeft;
        }
    }

    public static class ParticipateProjectView {

        private ObjectId projectId;

        private String createTime;

        private String cover;

        private Integer daysLeft;

        private long currentBalance;

        private Long goal;

        private Long feedbackGoal;

        private String feedback;

        private String strState;

        private String state;

        public ParticipateProjectView(ObjectId projectId, String createTime, String cover, Integer daysLeft, long
                currentBalance, Long goal, Long feedbackGoal, String feedback, String strState, String state) {
            this.projectId = projectId;
            this.createTime = createTime;
            this.cover = cover;
            this.daysLeft = daysLeft;
            this.currentBalance = currentBalance;
            this.goal = goal;
            this.feedbackGoal = feedbackGoal;
            this.feedback = feedback;
            this.strState = strState;
            this.state = state;
        }

        public String getCover() {
            return cover;
        }

        public ObjectId getProjectId() {
            return projectId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public Integer getDaysLeft() {
            return daysLeft;
        }

        public long getCurrentBalance() {
            return currentBalance;
        }

        public Long getGoal() {
            return goal;
        }

        public Long getFeedbackGoal() {
            return feedbackGoal;
        }

        public String getFeedback() {
            return feedback;
        }

        public String getStrState() {
            return strState;
        }

        public String getState() {
            return state;
        }
    }

    public static class MessageView {

        private String displayName;

        private String avatar;

        private Date time;

        private String message;

        private boolean isMe;

        public String getDisplayName() {
            return displayName;
        }

        public String getAvatar() {
            return avatar;
        }

        public Date getTime() {
            return time;
        }

        public String getMessage() {
            return message;
        }

        public boolean isMe() {
            return isMe;
        }

        public MessageView(String displayName, String avatar, Date time, String message, boolean isMe) {

            this.displayName = displayName;
            this.avatar = avatar;
            this.time = time;
            this.message = message;
            this.isMe = isMe;
        }
    }
}
