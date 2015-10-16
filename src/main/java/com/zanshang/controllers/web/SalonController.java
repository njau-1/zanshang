package com.zanshang.controllers.web;

import akka.actor.ActorSystem;
import com.zanshang.constants.ProjectState;
import com.zanshang.models.*;
import com.zanshang.models.index.SalonChatIndexBySalonId;
import com.zanshang.models.index.SalonTopicIndexBySalonId;
import com.zanshang.services.*;
import com.zanshang.services.author.AuthorTrapdoorImpl;
import com.zanshang.services.order.OrderTrapdoorImpl;
import com.zanshang.services.project.ProjectTrapdoorImpl;
import com.zanshang.services.qrcode.QRCodeTrapdoorImpl;
import com.zanshang.services.salon.SalonTrapdoorImpl;
import com.zanshang.services.setting.SettingTrapdoorImpl;
import com.zanshang.utils.Ajax;
import com.zanshang.utils.AkkaTrapdoor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.web.PageableDefault;
import org.springframework.mobile.device.Device;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

/**
 * Created by Lookis on 7/2/15.
 */
@Controller
@RequestMapping("/salons")
public class SalonController {

    Logger logger = LoggerFactory.getLogger(getClass());

    SalonTrapdoor salonService;

    ProjectTrapdoor projectService;

    AuthorTrapdoor authorService;

    SettingTrapdoor settingTrapdoor;

    OrderTrapdoor orderTrapdoor;

    QRCodeTrapdoor qrTrapdoor;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @Autowired
    MessageSource messageSource;

    @Autowired
    ActorSystem actorSystem;

    @Value("${SERVER_CONTEXT}")
    String serverContext;

    @PostConstruct
    protected void init() {
        salonService = akkaTrapdoor.createTrapdoor(SalonTrapdoor.class, SalonTrapdoorImpl.class);
        projectService = akkaTrapdoor.createTrapdoor(ProjectTrapdoor.class, ProjectTrapdoorImpl.class);
        authorService = akkaTrapdoor.createTrapdoor(AuthorTrapdoor.class, AuthorTrapdoorImpl.class);
        settingTrapdoor = akkaTrapdoor.createTrapdoor(SettingTrapdoor.class, SettingTrapdoorImpl.class);
        orderTrapdoor = akkaTrapdoor.createTrapdoor(OrderTrapdoor.class, OrderTrapdoorImpl.class);
        qrTrapdoor = akkaTrapdoor.createTrapdoor(QRCodeTrapdoor.class, QRCodeTrapdoorImpl.class);
    }

    @RequestMapping(method = RequestMethod.GET, params = "!page")
    public Object salons(@PageableDefault(size = 8) Pageable pageable, Principal principal, Device device) {
        if(device.isMobile()) {
            return "redirect:/salons/all";
        }
        ModelAndView mav = new ModelAndView("4_1");
        if (principal != null) {
            Page<Order> orderPage = orderTrapdoor.findPaidByUid(new ObjectId(principal.getName()), pageable);
            List<Order> orderList = orderPage.getContent();
            List<ObjectId> rewardId = new ArrayList<>();
            orderList.forEach(order -> rewardId.add(order.getRewardId()));
            List<Reward> rewardList = mongoTemplate.find(Query.query(Criteria.where("_id").in(rewardId)), Reward.class);
            List<ObjectId> projectId = new ArrayList<>();
            rewardList.forEach(reward -> projectId.add(reward.getProjectId()));
            Page<Project> projectPage = projectService.findByIds(projectId, pageable);
            Page<SalonView> salonPage = getPagedSalonModel(projectPage);
            mav.addObject("joinedSalons", salonPage);
        }
        Page<Project> projects = salonService.globalSalon(pageable);
        Page<SalonView> salonPage = getPagedSalonModel(projects);
        mav.addObject("allSalons", salonPage);
        return mav;
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public Object allSalon(@PageableDefault(size = 16) Pageable pageable) {
        ModelAndView mav = new ModelAndView("4_1_1");
        Page<Project> projects = salonService.globalSalon(pageable);
        Page<SalonView> salonPage = getPagedSalonModel(projects);
        mav.addObject("salons", salonPage);
        mav.addObject("title", "all");
        mav.addObject("tab", "all");
        return mav;
    }

    @RequestMapping(value = "/my", params = "page", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_USER")
    public Object mySalonMore(Pageable pageable, Principal principal) {
        Page<Project> projectPage = projectService.findByUid(new ObjectId(principal.getName()), pageable);
        Page<SalonView> salonPage = getPagedSalonModel(projectPage);
        Map<String, Object> pagedModel = Ajax.getPagedModel(salonPage);
        return Ajax.ok(pagedModel);
    }

    @RequestMapping(value = "/my", params = "!page", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_USER")
    public Object mySalon(@PageableDefault(size = 16) Pageable pageable, Principal principal) {
        ModelAndView mav = new ModelAndView("4_1_1");
        if (principal != null) {
            Page<Order> orderPage = orderTrapdoor.findPaidByUid(new ObjectId(principal.getName()), pageable);
            List<Order> orderList = orderPage.getContent();
            List<ObjectId> rewardId = new ArrayList<>();
            orderList.forEach(order -> rewardId.add(order.getRewardId()));
            List<Reward> rewardList = mongoTemplate.find(Query.query(Criteria.where("_id").in(rewardId)), Reward.class);
            List<ObjectId> projectId = new ArrayList<>();
            rewardList.forEach(reward -> projectId.add(reward.getProjectId()));
            Page<Project> projectPage = projectService.findByIds(projectId, pageable);
            Page<SalonView> salonPage = getPagedSalonModel(projectPage);
            mav.addObject("salons", salonPage);
        }
        mav.addObject("title", "my");
        mav.addObject("tab", "my");
        return mav;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object detail(@PathVariable("id") String id, Principal principal, Locale locale, HttpServletRequest
            request) {
        if (authorService.isVerified(new ObjectId(id))) {
            Salon salon = salonService.get(new ObjectId(id));
            Page<SalonTopic> topic = salonService.getTopic(new ObjectId(id), new PageRequest(0, 4));
            //有问题,需要再修改
            Page<Project> projectPage = projectService.findByUid(salon.getUid(), new PageRequest(0, 50));
            //
            Page<SalonChat> chat = salonService.getChat(new ObjectId(id), new PageRequest(0, 1000));
            List<ObjectId> memberId = new ArrayList<>();
            chat.getContent().forEach(salonChat -> memberId.add(salonChat.getUid()));
            Map<ObjectId, Setting> memberSettings = settingTrapdoor.findByIds(memberId);
            Setting setting = settingTrapdoor.get(new ObjectId(id));
            Page<SalonChatView> salonChatViews = chat.map(salonChat -> new SalonChatView(salonChat.getUid()
                    .toHexString(), memberSettings.get(salonChat.getUid()).getAvatar(), memberSettings.get(salonChat
                    .getUid()).getDisplayName(), salonChat.getChat(), salonChat.getCreateTime()));
            //
            ModelAndView mav = new ModelAndView("4_2");
            List<SalonTopic> salonTopics = new ArrayList<>(topic.getContent());
            if (CollectionUtils.isNotEmpty(salonTopics)) {
                SalonTopic salonTopic = salonTopics.get(0);
//                Date createTime = salonTopic.getCreateTime();
//                Date now = new Date();
//                if (now.getTime() - createTime.getTime() < (24 * 60 * 60 * 1000)) {
                //解除24小时限制
                if (true) {
//                    String time = DurationFormatUtils.formatDuration((24 * 60 * 60 * 1000)-(new Date().getTime() - createTime.getTime()),
//                            messageSource.getMessage("topic.duration", null, locale));
//                    mav.addObject("aliveTime", time);
                    SalonTopic currentTopic = salonTopics.remove(0);
                    mav.addObject("currentTopic", currentTopic);
                }
            }
            //qrshare
            String shareLink = serverContext + request.getContextPath() + "/salons/" + id;
            QRCode qrCode = new QRCode(shareLink);
            qrTrapdoor.save(qrCode);
            boolean isWechat = WechatController.isWechat(request);
            mav.addObject("isWechat", isWechat);
            mav.addObject("shareQRLink", String.format(QRCodeTrapdoor.pathFormat, qrCode.getId()));
            mav.addObject("shareLink", shareLink);
            mav.addObject("chats", salonChatViews);
            mav.addObject("topic", salonTopics);
            mav.addObject("salon", salon);
            mav.addObject("avatar", setting.getAvatar());
            mav.addObject("authorname", setting.getDisplayName());
            ArrayList<Project> projects = new ArrayList<>(projectPage.getContent());
//            Collections.reverse(projects);
            ArrayList<Project> projectList = new ArrayList<>();
            for (Project project: projects) {
                if (project.getState() != ProjectState.REVIEWING && project.getState() != ProjectState.PRICING) {
                    projectList.add(project);
                }
            }
            mav.addObject("projects", projectList);
            if (projectList.size() != 0) {
                mav.addObject("progressCode", projectList.get(0).getProgressCode());
            }else {
                mav.addObject("progressCode", 0);
            }
            if (principal != null && salonService.isMember(new ObjectId(id), new ObjectId(principal.getName()))) {
                mav.addObject("isMember", true);
            } else {
                mav.addObject("isMember", false);
            }
            if (principal != null && StringUtils.equals(id, principal.getName())) {
                mav.addObject("isAuthor", true);
            } else {
                mav.addObject("isAuthor", false);
            }
            return mav;
        } else {
            return "redirect:/salons";
        }
    }

    @RequestMapping(value = "/{id}/topic", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Object getTopicCreation(@PathVariable("id") String salonId, Principal principal) {
        if (StringUtils.equals(salonId, principal.getName()) && authorService.isVerified(new ObjectId(principal
                .getName()))) {
            ModelAndView mav = new ModelAndView("4_4");
            mav.addObject("id", salonId);
            return mav;
        } else {
            return "redirect:/salons/" + salonId;
        }
    }

    @RequestMapping(value = "/{id}/topic", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public String makeTopic(@PathVariable("id") String salonId, @RequestParam("title") String title, @RequestParam
            ("content") String content, @RequestParam(value = "images[]", required = false) String[] images,
                            Principal principal) {
        if (StringUtils.equals(salonId, principal.getName()) && authorService.isVerified(new ObjectId(principal
                .getName()))) {
            SalonTopic topic;
            if (images != null) {
                topic = new SalonTopic(new ObjectId(salonId), title, content, Arrays.asList(images));
            } else {
                topic = new SalonTopic(new ObjectId(salonId), title, content, null);
            }
            salonService.createTopic(topic);
        }
        return "redirect:/salons/" + salonId;
    }

    @RequestMapping(value = "/{id}/chat", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object chat(@PathVariable("id") String id, Principal principal, @RequestParam("chat") String chat, Locale
            locale) {
        if (salonService.isMember(new ObjectId(id), new ObjectId(principal.getName())) || StringUtils.equals
                (principal.getName(), id)) {
            salonService.chat(new SalonChat(new ObjectId(principal.getName()), new ObjectId(id), chat));
            return Ajax.ok();
        } else {
            return Ajax.failure(messageSource.getMessage("salon.notmember", null, locale));
        }
    }

    private Page<SalonView> getPagedSalonModel(Page<Project> pagedSalons) {
        List<Project> content = pagedSalons.getContent();
        List<ObjectId> salonIds = new ArrayList<>();
        content.forEach(project -> salonIds.add(project.getUid()));
        //
        List<SalonTopicIndexBySalonId> topicIndexBySalonIds = mongoTemplate.find(Query.query(Criteria.where("_id").in
                (salonIds)), SalonTopicIndexBySalonId.class);
        Map<ObjectId, Integer> topicSizeMap = new HashMap<>();
        salonIds.forEach(id -> topicSizeMap.put(id, 0));
        topicIndexBySalonIds.forEach(salonTopicIndexBySalonId -> topicSizeMap.put(salonTopicIndexBySalonId.getKey(),
                salonTopicIndexBySalonId.getValue().size()));
        //
        List<SalonChatIndexBySalonId> salonChatIndexBySalonIds = mongoTemplate.find(Query.query(Criteria.where("_id")
                .in(salonIds)), SalonChatIndexBySalonId.class);
        Map<ObjectId, Integer> chatSizeMap = new HashMap<>();
        salonIds.forEach(id -> chatSizeMap.put(id, 0));
        salonChatIndexBySalonIds.forEach(salonChatIndexBySalonId -> chatSizeMap.put(salonChatIndexBySalonId.getKey(),
                salonChatIndexBySalonId.getValue().size()));
        //
        Map<ObjectId, Salon> salonMap = salonService.findByIds(salonIds);
        return pagedSalons.map(source -> new SalonView(source.getUid().toHexString(), source.getCover(), source
                .getBookName(), salonMap.get(source.getUid()).getMembers().size(), topicSizeMap.get(source.getUid()),
                chatSizeMap.get(source.getUid())));
    }

    public static class SalonChatView {

        private String memberId;

        private String avatar;

        private String name;

        private String chat;

        private Date createTime;

        public SalonChatView(String memberId, String avatar, String name, String chat, Date createTime) {
            this.memberId = memberId;
            this.avatar = avatar;
            this.name = name;
            this.chat = chat;
            this.createTime = createTime;
        }

        public String getMemberId() {

            return memberId;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getName() {
            return name;
        }

        public String getChat() {
            return chat;
        }

        public Date getCreateTime() {
            return createTime;
        }
    }

    public static class SalonView {

        private String salonId;

        private String image;

        private String name;

        private int members;

        private int history;

        private int chat;

        public SalonView(String salonId, String image, String name, int members, int history, int chat) {
            this.salonId = salonId;
            this.image = image;
            this.name = name;
            this.members = members;
            this.history = history;
            this.chat = chat;
        }

        public String getSalonId() {

            return salonId;
        }

        public String getImage() {
            return image;
        }

        public String getName() {
            return name;
        }

        public int getMembers() {
            return members;
        }

        public int getHistory() {
            return history;
        }

        public int getChat() {
            return chat;
        }
    }
}
