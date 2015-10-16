package com.zanshang.controllers.web;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.zanshang.captcha.CaptchaException;
import com.zanshang.captcha.PhoneCaptchaService;
import com.zanshang.config.spring.CacheConfig;
import com.zanshang.constants.ActorConstant;
import com.zanshang.constants.EmailConstants;
import com.zanshang.constants.ProjectState;
import com.zanshang.framework.PaymentType;
import com.zanshang.framework.PriceUtils;
import com.zanshang.framework.Ticket;
import com.zanshang.framework.spring.MongodbUserDetailsManager;
import com.zanshang.models.*;
import com.zanshang.notify.Notifier;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.services.*;
import com.zanshang.services.address.AddressTrapdoorImpl;
import com.zanshang.services.mailbox.EmailCodeTrapdoorImpl;
import com.zanshang.services.order.OrderPaymentCallbackService;
import com.zanshang.services.order.OrderTrapdoorImpl;
import com.zanshang.services.payment.PaymentTrapdoorImpl;
import com.zanshang.services.person.PersonTrapdoorImpl;
import com.zanshang.services.phone.PhoneCaptchaTrapdoorImpl;
import com.zanshang.services.project.ProjectTrapdoorImpl;
import com.zanshang.services.qrcode.QRCodeSaveActor;
import com.zanshang.services.setting.SettingTrapdoorImpl;
import com.zanshang.services.shorturl.ShortUrlActor;
import com.zanshang.services.wechat.WechatAuthorizationTrapdoorImpl;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.RandomString;
import com.zanshang.utils.Request;
import com.zanshang.utils.UserCountUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mobile.device.Device;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import scala.concurrent.Await;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.*;

/**
 * Created by Lookis on 6/28/15.
 */
@Controller
@RequestMapping(value = "/orders")
public class OrderController implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final static int PASSWORD_LENGTH = 6;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    MongodbUserDetailsManager userDetailsManager;

    @Autowired
    MessageSource messageSource;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    Notifier notifier;

    @Autowired
    PhoneCaptchaService phoneCaptchaService;

    @Value("${SERVER_CONTEXT}")
    String SERVER_CONTEXT;

    @Value("${WECHAT_MP_ID}")
    String MP_ID;

    @Value("${WECHAT_MP_SECRET}")
    String MP_SECRET;

    ProjectTrapdoor projectTrapdoor;

    AddressTrapdoor addressTrapdoor;

    OrderTrapdoor orderTrapdoor;

    SettingTrapdoor settingTrapdoor;

    WechatAuthorizationTrapdoor wechatTrapdoor;

    ActorRef QRActor;

    ActorRef shortUrlActor;

    EmailCodeTrapdoor emailService;

    PersonTrapdoor personTrapdoor;

    PaymentTrapdoor paymentTrapdoor;

    PhoneCaptchaTrapdoor phoneService;

    private ApplicationContext applicationContext;

    @PostConstruct
    protected void init() {
        projectTrapdoor = akkaTrapdoor.createTrapdoor(ProjectTrapdoor.class, ProjectTrapdoorImpl.class);
        addressTrapdoor = akkaTrapdoor.createTrapdoor(AddressTrapdoor.class, AddressTrapdoorImpl.class);
        orderTrapdoor = akkaTrapdoor.createTrapdoor(OrderTrapdoor.class, OrderTrapdoorImpl.class);
        settingTrapdoor = akkaTrapdoor.createTrapdoor(SettingTrapdoor.class, SettingTrapdoorImpl.class);
        emailService = akkaTrapdoor.createTrapdoor(EmailCodeTrapdoor.class, EmailCodeTrapdoorImpl.class);
        wechatTrapdoor = akkaTrapdoor.createTrapdoor(WechatAuthorizationTrapdoor.class,
                WechatAuthorizationTrapdoorImpl.class);
        QRActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(QRCodeSaveActor.class, applicationContext)));
        personTrapdoor = akkaTrapdoor.createTrapdoor(PersonTrapdoor.class, PersonTrapdoorImpl.class);

        shortUrlActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(ShortUrlActor.class, applicationContext)));
        paymentTrapdoor = akkaTrapdoor.createTrapdoor(PaymentTrapdoor.class, PaymentTrapdoorImpl.class);
        phoneService = akkaTrapdoor.createTrapdoor(PhoneCaptchaTrapdoor.class, PhoneCaptchaTrapdoorImpl.class);
    }

    //创建定单表单页
    @RequestMapping(value = "/creation", method = RequestMethod.GET)
    //    @Secured("ROLE_USER")
    public Object form(HttpServletRequest request, @RequestParam("reward") String rewardId, Principal principal) {
        Reward reward = mongoTemplate.findById(rewardId, Reward.class);
        Project project = projectTrapdoor.get(reward.getProjectId());
        if (project.getState() != ProjectState.FUNDING) {
            return "redirect:" + ProjectController.PATH + "/" + project.getId();
        }
        OrderForm form = new OrderForm();
        form.setCount(1);
        List<ObjectId> authorIds = new ArrayList<ObjectId>();
        authorIds.add(project.getUid());
        Map<ObjectId, Setting> authorSettings = settingTrapdoor.findByIds(authorIds);
        ModelAndView mav = new ModelAndView("6_4");

        String tinyurl;
        try {
            Object result = Await.result(Patterns.ask(shortUrlActor, Request.buildCurrentURL(request),
                    ActorConstant.REMOTE_CALL_TIMEOUT), ActorConstant.REMOTE_CALL_DURATION);
            tinyurl = (String) result;
        } catch (Exception e) {
            tinyurl = Request.buildCurrentURL(request);
        }
        mav.addObject("tinyurl", tinyurl);

        if (principal != null) {
            ObjectId uid = new ObjectId(principal.getName());
            List<Address> addresses = addressTrapdoor.findByUid(uid);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                form.setAddress(address.getRecipient() + "," + address.getTelephone() + "," + address.getAddress() +
                        "," + address.getPostCode());
            }
            mav.addObject("addresses", addresses);
            mav.addObject("isLogin", true);
        } else {
            mav.addObject("addresses", new ArrayList<Address>());
            mav.addObject("isLogin", false);
        }
        mav.addObject("order", form);
        mav.addObject("project", project);
        mav.addObject("authorDisplayName", authorSettings.get(project.getUid()).getDisplayName());
        mav.addObject("reward", reward);
        return mav;
    }

    //创建定单
    @RequestMapping(method = RequestMethod.POST)
    //    @Secured("ROLE_USER")
    public Object create(@Valid OrderForm form, BindingResult result, Principal principal, HttpServletRequest request, Device device,
                         @RequestParam(value = "email", required = false) String email, @RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "captcha", required = false) String captcha, Locale locale, @Ticket String
                                     ticket) {
        if (result.hasErrors()) {
            return "redirect:/orders/creation?reward=" + form.getRewardId();
        }
        Reward reward = mongoTemplate.findById(form.getRewardId(), Reward.class);
        Project project = projectTrapdoor.get(reward.getProjectId());
        Assert.notNull(reward);
        Order order;
        if (principal != null) {
            order = new Order(new ObjectId(principal.getName()), reward.getId(), form.getCount(), StringUtils.isEmpty
                    (form.getAddress()) ? null : form.getAddress(), form.getComment());
        } else {
            if (device.isMobile()) {
                if (phone == null || phone.isEmpty()) {
                    return "redirect:/orders/creation?reward=" + form.getRewardId();
                }
                try {
                    if (phoneCaptchaService.verify(phone, captcha)) {
                        String password = RandomStringUtils.randomNumeric(PASSWORD_LENGTH);
                        PhoneAccount account = new PhoneAccount(phone, encoder.encode(password));
                        try {
                            userDetailsManager.createUser(account);
                            Person person = new Person(account.getUid(), phone);
                            Setting setting = new Setting(account.getUid(), phone.replace(phone.substring(3,7), "****"), null);
                            personTrapdoor.save(person);
                            settingTrapdoor.save(setting);
                            Map<String, String> model = new HashMap<>();
                            model.put("password", password);
                            notifier.notify(account.getUid(), NotifyBusinessType.PHONE_NOLOGIN_REGIST, model);
                        } catch (DuplicateKeyException e) {
                            //邮箱重复,叠加进原手机号账户
                            account = mongoTemplate.findById(phone, PhoneAccount.class);
                            request.setAttribute(UserCountUtils.NOLGINUSERFLAG, account.getUid().toHexString());
                        }
                        order = new Order(account.getUid(), reward.getId(), form.getCount(), StringUtils.isEmpty(form.getAddress
                                ()) ? null : form.getAddress(), form.getComment());
                    } else {
//                        result.rejectValue("captcha", "captcha.missing_or_error");
                        return "redirect:/orders/creation?reward=" + form.getRewardId();
                    }
                } catch (Exception e) {
//                    result.rejectValue("captcha", "captcha.missing_or_error");
                    return "redirect:/orders/creation?reward=" + form.getRewardId();
                }
            } else {
                if (email == null || email.isEmpty()) {
                    return "redirect:/orders/creation?reward=" + form.getRewardId();
                }
                String password = RandomString.getRandomString(PASSWORD_LENGTH);
                EmailAccount account = new EmailAccount(email, encoder.encode(password));
                account.setEnabled(false);//需要激活才可启用
                try {
                    userDetailsManager.createUser(account);
                    Map<String, String> mailModel = new HashMap<>();
                    mailModel.put("email", email);
                    mailModel.put("password", password);
                    emailService.create(email, EmailConstants.EMAIL_NOLOGIN_REGIST_TEMPLATENAME, messageSource.getMessage
                            ("email.title.activation", null, locale), mailModel);
                    Person person = new Person(account.getUid(), null);
                    Setting setting = new Setting(account.getUid(), email, email);
                    personTrapdoor.save(person);
                    settingTrapdoor.save(setting);
                } catch (DuplicateKeyException e) {
                    //邮箱重复,叠加进原邮箱
                    account = mongoTemplate.findById(email, EmailAccount.class);
                    request.setAttribute(UserCountUtils.NOLGINUSERFLAG, account.getUid().toHexString());
                }
                order = new Order(account.getUid(), reward.getId(), form.getCount(), StringUtils.isEmpty(form.getAddress
                        ()) ? null : form.getAddress(), form.getComment());
            }
        }
        Cache cache = cacheManager.getCache(CacheConfig.CACHE_NAME_SHARE_PROJECT_UID);
        String sharerId = cache.get(ticket + reward.getProjectId(), String.class);
        if (sharerId != null && !sharerId.isEmpty()) {
            order.setSharerId(new ObjectId(sharerId));
        }
        orderTrapdoor.save(order);
        Map<String, String> paymentArguments = new HashMap<>();
        paymentArguments.put(OrderPaymentCallbackService.ORDERID, order.getId().toHexString());
        return paymentTrapdoor.createPayment(project.getBookName(), PriceUtils.multi(reward.getPrice(), order
                .getCount()), PaymentType.Order, paymentArguments, device.isMobile());
    }

    //支付完成后的同步通知页面，用于支付宝网页支付后跳转和异步通知后页面自动跳转
    @RequestMapping(value = "/{orderId}/status")
    public Object done(@PathVariable("orderId") String orderId, HttpServletRequest request, Device device) {
        //查询alipay和wechat外网状态并返回
        //TODO: 等alipay single query接口审核通过之后再加，现在统一返回ok
        Order order = orderTrapdoor.get(new ObjectId(orderId));
        ObjectId rewardId = order.getRewardId();
        Reward reward = mongoTemplate.findById(rewardId, Reward.class);
        ObjectId projectId = reward.getProjectId();
        Project project = projectTrapdoor.get(projectId);
        if (order.isPaid()) {
            ObjectId author = project.getUid();
            Setting setting = settingTrapdoor.get(author);
            ModelAndView mav = new ModelAndView("6_6");
            mav.addObject("author", setting);
            mav.addObject("project", project);

            EmailAccount account = mongoTemplate.findOne(Query.query(Criteria.where("uid").in(order.getUid())),
                    EmailAccount.class);
            if (account != null && !account.isEnabled()) {
                mav.addObject("noactive", true);
                mav.addObject("email", account.getEmail());
            }
            String shareLink = SERVER_CONTEXT + request.getContextPath() + ProjectController.PATH + "/" +
                    project.getId();
            QRCode qrCode = new QRCode(shareLink);
            try {
                Await.ready(Patterns.ask(QRActor, qrCode, ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                        .DEFAULT_TIMEOUT_DURATION);
            } catch (Exception e) {
                logger.warn("Ops.", e);
            }
            mav.addObject("shareQRLink", String.format(QRCodeTrapdoor.pathFormat, qrCode.getId()));
            mav.addObject("shareLink", shareLink);
            return mav;
        } else {
            Map<String, String> paymentArguments = new HashMap<>();
            paymentArguments.put(OrderPaymentCallbackService.ORDERID, order.getId().toHexString());
            return paymentTrapdoor.createPayment(project.getBookName(), PriceUtils.multi(reward.getPrice(), order
                    .getCount()), PaymentType.Order, paymentArguments, device.isMobile());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    static class OrderForm {

        @Min(message = "{order.count.notempty}", value = 1)
        private int count;

        @Email(message = "{register.personal.email.format_error}")
        private String email;

        private String comment;

        //@NotEmpty(message = "{order.address.notempty}")
        private String address;

        @NotEmpty(message = "{order.reward_id.notempty}")
        private String rewardId;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public OrderForm() {
        }

        public int getCount() {

            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRewardId() {
            return rewardId;
        }

        public void setRewardId(String rewardId) {
            this.rewardId = rewardId;
        }
    }
}
