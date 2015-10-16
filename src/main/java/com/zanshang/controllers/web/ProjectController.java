package com.zanshang.controllers.web;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.constants.BookType;
import com.zanshang.constants.ProjectState;
import com.zanshang.framework.Price;
import com.zanshang.framework.PriceUnit;
import com.zanshang.framework.Ticket;
import com.zanshang.models.*;
import com.zanshang.models.index.AuthorInformationIndexByIdentity;
import com.zanshang.services.*;
import com.zanshang.services.bid.BidTrapdoorImpl;
import com.zanshang.services.company.CompanyTrapdoorImpl;
import com.zanshang.services.order.OrderTrapdoorImpl;
import com.zanshang.services.person.PersonTrapdoorImpl;
import com.zanshang.services.project.ProjectTrapdoorImpl;
import com.zanshang.services.publisher.PublisherTrapdoorImpl;
import com.zanshang.services.qrcode.QRCodeTrapdoorImpl;
import com.zanshang.services.setting.SettingTrapdoorImpl;
import com.zanshang.utils.Ajax;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.BookUtils;
import com.zanshang.utils.Json;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import scala.collection.parallel.ParIterableLike;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Lookis on 6/26/15.
 */
@Controller
@RequestMapping(value = ProjectController.PATH)
public class ProjectController {

    public static final String PATH = "/projects";

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @Value("${SERVER_CONTEXT}")
    String serverContext;

    @Value("${IMAGE_CONTEXT}")
    String IMAGE_CONTEXT;

    @Autowired
    MessageSource messageSource;

    @Autowired
    BookUtils bookUtils;

    @Autowired
    CacheManager cacheManager;

    ProjectTrapdoor projectTrapdoor;

    PublisherTrapdoor publisherTrapdoor;

    OrderTrapdoor orderTrapdoor;

    SettingTrapdoor settingTrapdoor;

    BidTrapdoor bidTrapdoor;

    CompanyTrapdoor companyTrapdoor;

    QRCodeTrapdoor qrTrapdoor;

    PersonTrapdoor personTrapdoor;

    @PostConstruct
    protected void construct() {
        orderTrapdoor = akkaTrapdoor.createTrapdoor(OrderTrapdoor.class, OrderTrapdoorImpl.class);
        settingTrapdoor = akkaTrapdoor.createTrapdoor(SettingTrapdoor.class, SettingTrapdoorImpl.class);
        projectTrapdoor = akkaTrapdoor.createTrapdoor(ProjectTrapdoor.class, ProjectTrapdoorImpl.class);
        bidTrapdoor = akkaTrapdoor.createTrapdoor(BidTrapdoor.class, BidTrapdoorImpl.class);
        publisherTrapdoor = akkaTrapdoor.createTrapdoor(PublisherTrapdoor.class, PublisherTrapdoorImpl.class);
        companyTrapdoor = akkaTrapdoor.createTrapdoor(CompanyTrapdoor.class, CompanyTrapdoorImpl.class);
        qrTrapdoor = akkaTrapdoor.createTrapdoor(QRCodeTrapdoor.class, QRCodeTrapdoorImpl.class);
        personTrapdoor = akkaTrapdoor.createTrapdoor(PersonTrapdoor.class, PersonTrapdoorImpl.class);
    }

    @RequestMapping(params = "!page", method = RequestMethod.GET)
    public ModelAndView list(@PageableDefault(size = 10) Pageable pageable, Locale locale) {
        Page<View> viewPage = getProjectHomeViews(pageable, locale);
        ModelAndView mav = new ModelAndView("3_1");
        mav.addObject("projects", viewPage);
        return mav;
    }

    @RequestMapping(params = "page", method = RequestMethod.GET)
    @ResponseBody
    public Object listMore(@PageableDefault(size = 10) Pageable pageable, Locale locale) {
        Page<View> projectHomeViews = getProjectHomeViews(pageable, locale);
        return Ajax.ok(Ajax.getPagedModel(projectHomeViews));
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.GET)
    public Object detail(@PathVariable("projectId") String projectId, HttpServletRequest request, Principal principal, Locale locale) {
        Project project = projectTrapdoor.get(new ObjectId(projectId));
        if (project.getState() == ProjectState.REVIEWING) {
            return "redirect:/projects/" + projectId + "/review";
        }
        ModelAndView mav = new ModelAndView("6_1_1");
        //rewards
        Collection<ObjectId> rewards = project.getRewards();
        List<Reward> rewardList = mongoTemplate.find(Query.query(Criteria.where("_id").in(rewards)), Reward.class);
        Collections.sort(rewardList, ((o1, o2) -> o1.getPrice().compareTo(o2.getPrice())));
        mav.addObject("rewards", rewardList);
        //orders
        List<Order> orderList = orderTrapdoor.findByProjectId(project.getId());
        //sort by rewards
        List<ObjectId> userIds = orderList.stream().map((o -> o.getUid())).collect(Collectors.toList());
        Map<ObjectId, Setting> settingMap = settingTrapdoor.findByIds(userIds);
        Map<String, Set<Setting>> sortMap = new LinkedHashMap<>();
        Set<Setting> allBuyers = new HashSet<>();
        for (Reward reward : rewardList) {
            sortMap.put(reward.getId().toHexString(), new LinkedHashSet<>());
        }
        for (Order order : orderList) {
            if (order.isPaid()) {
                Set<Setting> settings = sortMap.get(order.getRewardId().toHexString());
                settings.add(settingMap.get(order.getUid()));
                allBuyers.add(settingMap.get(order.getUid()));
            }
        }
        mav.addObject("buyerMap", sortMap);
        mav.addObject("allBuyers", allBuyers);
        //paid money
        Price paid = projectTrapdoor.getPaid(project.getId());
        //authors
        List<Project.Author> authors = project.getAuthors();
        List<String> identities = authors.stream().map((author -> author.getIdentity())).collect(Collectors.toList());
        Person firstAuthor = personTrapdoor.get(project.getUid());
        Company companyAuthor = null;
        if (firstAuthor != null) {
            identities.add(firstAuthor.getIdentityCode());
        } else {
            companyAuthor = companyTrapdoor.get(project.getUid());
            identities.add(companyAuthor.getCompanyCode());
        }
        List<AuthorInformationIndexByIdentity> indexByIdentityList = mongoTemplate.find(Query.query(Criteria.where
                ("_id").in(identities)), AuthorInformationIndexByIdentity.class);
        List<ObjectId> authorIds = indexByIdentityList.stream().map(authorInformationIndexByIdentity ->
                authorInformationIndexByIdentity.getUid()).collect(Collectors.toList());
        Map<ObjectId, Setting> authorSettings = settingTrapdoor.findByIds(authorIds);
        Map<String, ObjectId> identityToUid = new HashMap<>();
        indexByIdentityList.forEach(authorInformationIndexByIdentity -> identityToUid.put
                (authorInformationIndexByIdentity.getIdentity(), authorInformationIndexByIdentity.getUid()));
        List<AuthorView> authorViews = new ArrayList<>();
        if (firstAuthor != null) {
            authorViews.add(new AuthorView(firstAuthor.getUid(), authorSettings.get(firstAuthor.getUid()).getDisplayName
                    (), authorSettings.get(firstAuthor.getUid()).getAvatar(), project.getFirstAuthorDescription()));
        } else {
            authorViews.add(new AuthorView(companyAuthor.getUid(), authorSettings.get(companyAuthor.getUid()).getDisplayName
                    (), authorSettings.get(companyAuthor.getUid()).getAvatar(), project.getFirstAuthorDescription()));
        }
        authors.forEach(author -> {
            authorViews.add(new AuthorView(identityToUid.get(author.getIdentity()), authorSettings.get(identityToUid.get
                    (author.getIdentity())).getDisplayName(), authorSettings.get(identityToUid.get(author.getIdentity()))
                    .getAvatar(), author.getDescription()));
        });
        mav.addObject("paid", paid.getUnit().toYuan(paid.getPrice()));
        mav.addObject("users", sortMap.values());
        mav.addObject("project", new View(project.getId().toHexString(), project.getUid().toHexString(),
                authorSettings.get(project.getUid()).getAvatar(), authorSettings.get(project.getUid()).getDisplayName
                (), paid.to(PriceUnit.YUAN), project.getGoal().to(PriceUnit.YUAN), project.getCreateTime(), project
                .getUpdateTime(), project.getDeadline(), project.getPublisher(), project.getBookName(), project.getTypes() != null ? project.getTypes().stream().map(type -> bookUtils.parse(type, locale)).collect(Collectors.toList()) : null, project
                .getTags(), project.getDescription(), project.getCover(), project.getState(), project.getImages(),
                project.getOutline(), project.getDraft()));
        mav.addObject("authors", authorViews);
        //price authorize
        if (principal != null) {
            ObjectId uid = new ObjectId(principal.getName());
            if (StringUtils.equals(project.getUid().toHexString(), principal.getName()) || publisherTrapdoor
                    .isVerified(uid)) {
                mav.addObject("bidAuth", true);
            }
            mav.addObject("isAuthor", project.getUid().equals(uid));
        }
        //publisher got
        if (project.getPublisher() != null) {
            Map<String, Object> publisher = new HashMap<>();
            ObjectId pubId = project.getPublisher();
            Setting setting = settingTrapdoor.get(pubId);
            publisher.put("displayName", setting.getDisplayName());
            publisher.put("avatar", setting.getAvatar());
            publisher.put("id", setting.getUid());
            Company company = companyTrapdoor.get(pubId);
            if (company != null) {
                publisher.put("organizeName", company.getCompanyName());
            }
            mav.addObject("publisher", publisher);
        }
        String shareAddress = serverContext + request.getContextPath() + ProjectController.PATH + "/" +
                projectId;
        QRCode qrCode = new QRCode(shareAddress);
        qrTrapdoor.save(qrCode);
        boolean isWechat = WechatController.isWechat(request);
        mav.addObject("isWechat", isWechat);
        mav.addObject("shareQRLink", String.format(QRCodeTrapdoor.pathFormat, qrCode.getId()));
        mav.addObject("shareLink", shareAddress);
        boolean isPreview = false;
        mav.addObject("isPreview", isPreview);
        return mav;
    }

    @RequestMapping(value = "/{projectId}/uid/{sharerId}", method = RequestMethod.GET)
    public Object detail(@PathVariable("projectId") String projectId, @PathVariable("sharerId") String sharerId, @Ticket String ticket) {
        Setting setting = settingTrapdoor.get(new ObjectId(sharerId));
        if (setting != null){
            Cache cache = cacheManager.getCache(CacheConfig.CACHE_NAME_SHARE_PROJECT_UID);
            cache.put(ticket + projectId, sharerId);
        }
        return "redirect:/projects/" + projectId;
    }

    @RequestMapping(value = "/{projectId}/mobile", method = RequestMethod.GET)
    public Object mobileDetail(@PathVariable("projectId") String projectId, HttpServletRequest request, Principal principal, Locale locale) {
        Project project = projectTrapdoor.get(new ObjectId(projectId));
        if (project.getState() == ProjectState.REVIEWING) {
            return "redirect:/projects/" + projectId + "/review";
        }
        ModelAndView mav = new ModelAndView("6_1_2");
        //authors
        List<Project.Author> authors = project.getAuthors();
        List<String> identities = authors.stream().map((author -> author.getIdentity())).collect(Collectors.toList());
        Person firstAuthor = personTrapdoor.get(project.getUid());
        Company companyAuthor = null;
        if (firstAuthor != null) {
            identities.add(firstAuthor.getIdentityCode());
        } else {
            companyAuthor = companyTrapdoor.get(project.getUid());
            identities.add(companyAuthor.getCompanyCode());
        }
        List<AuthorInformationIndexByIdentity> indexByIdentityList = mongoTemplate.find(Query.query(Criteria.where
                ("_id").in(identities)), AuthorInformationIndexByIdentity.class);
        List<ObjectId> authorIds = indexByIdentityList.stream().map(authorInformationIndexByIdentity ->
                authorInformationIndexByIdentity.getUid()).collect(Collectors.toList());
        Map<ObjectId, Setting> authorSettings = settingTrapdoor.findByIds(authorIds);
        Map<String, ObjectId> identityToUid = new HashMap<>();
        indexByIdentityList.forEach(authorInformationIndexByIdentity -> identityToUid.put
                (authorInformationIndexByIdentity.getIdentity(), authorInformationIndexByIdentity.getUid()));
        List<AuthorView> authorViews = new ArrayList<>();
        if (firstAuthor != null) {
            authorViews.add(new AuthorView(firstAuthor.getUid(), authorSettings.get(firstAuthor.getUid()).getDisplayName
                    (), authorSettings.get(firstAuthor.getUid()).getAvatar(), project.getFirstAuthorDescription()));
        } else {
            authorViews.add(new AuthorView(companyAuthor.getUid(), authorSettings.get(companyAuthor.getUid()).getDisplayName
                    (), authorSettings.get(companyAuthor.getUid()).getAvatar(), project.getFirstAuthorDescription()));
        }
        authors.forEach(author -> {
            authorViews.add(new AuthorView(identityToUid.get(author.getIdentity()), authorSettings.get(identityToUid.get
                    (author.getIdentity())).getDisplayName(), authorSettings.get(identityToUid.get(author.getIdentity()))
                    .getAvatar(), author.getDescription()));
        });
        mav.addObject("project", new View(project.getId().toHexString(), project.getUid().toHexString(),
                authorSettings.get(project.getUid()).getAvatar(), authorSettings.get(project.getUid()).getDisplayName
                (), 0, project.getGoal().to(PriceUnit.YUAN), project.getCreateTime(), project
                .getUpdateTime(), project.getDeadline(), project.getPublisher(), project.getBookName(), project.getTypes() != null ? project.getTypes().stream().map(type -> bookUtils.parse(type, locale)).collect(Collectors.toList()) : null, project
                .getTags(), project.getDescription(), project.getCover(), project.getState(), project.getImages(),
                project.getOutline(), project.getDraft()));
        mav.addObject("authors", authorViews);
        return mav;
    }

    @RequestMapping(value = "/{projectId}/draft", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_USER")
    public Object projectDraft(@PathVariable("projectId") String projectId, @RequestParam(value = "images[]", required = false) List<String>
            images, Principal principal, Date requestTime) {
        Project project = projectTrapdoor.get(new ObjectId(projectId));
        if (StringUtils.equals(project.getUid().toHexString(), principal.getName())) {
            if (images == null) {
                images = Collections.EMPTY_LIST;
            }
            project.setImages(images);
//            project.setDraft(draft);
            project.setUpdateTime(requestTime);
            projectTrapdoor.save(project);
            return Ajax.ok();
        } else {
            return Ajax.failure(null);
        }
    }

    @RequestMapping(value = "/preview", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_USER")
    public Object projectPreview(@RequestParam(value = "name", required = false) String name, //
                                 @RequestParam(value = "types",
                                         required = false) List<String> types, //
                                 @RequestParam(value = "description", required = false) String description, //
                                 @RequestParam(value = "cover", defaultValue = "/static/default_cover.png") String cover, //
                                 @RequestParam(value = "aboutFirstAuthor", required = false) String aboutFirstAuthor, //
                                 @RequestParam(value = "outline", required = false) String outline, //
                                 @RequestParam(value = "draft", required = false) String draft, //
                                 @RequestParam(value = "images", required = false) List<String> images, //
                                 @RequestParam(value = "tags", required = false) List<String> tags, //
                                 @RequestParam(value = "authorId", required = false) String authorId, //
                                 @RequestParam(value = "authorName", required = false) String authorName, //
                                 @RequestParam(value = "authorIdFront", required = false) String authorIdFront, //
                                 @RequestParam(value = "authorIdBack", required = false) String authorIdBack,//
                                 HttpServletRequest request,
                                 Principal principal, Locale locale) {
        ModelAndView mav = new ModelAndView("6_1_1");
        ObjectId uid = new ObjectId(principal.getName());
        List<Project.Author> authorList = new ArrayList<>();
        authorList.add(new Project.Author(authorId, authorName, aboutFirstAuthor));
        String[] params = request.getParameterValues("authors");
        if (params != null && params.length != 0) {
            for (String s : params) {
                if (!s.isEmpty()) {
                    Project.Author author = Json.fromJson(s, Project.Author.class);
                    authorList.add(author);
                }
            }
        }
        List<String> typeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(types)) {
            for (String t : types) {
                typeList.add(t);
            }
        }
        ProjectPreview project = new ProjectPreview(uid, aboutFirstAuthor, authorList, name, typeList,
                description, tags, cover, images == null ? new ArrayList<>() : images, outline, draft, 0, 0, 0);
        mav.addObject("project", new View(project.getId().toHexString(), project.getUid().toHexString(),
                null, authorName, 0, 0, project.getCreateTime(), project.getUpdateTime(), project.getDeadline(), project.getPublisher(), project.getBookName(), typeList, project
                .getTags(), project.getDescription(), project.getCover(), project.getState(), project.getImages(),
                project.getOutline(), project.getDraft()));
        mav.addObject("authors", authorList);
        boolean isPreview = true;
        mav.addObject("isPreview", isPreview);
        return mav;
    }

    @RequestMapping("/{projectId}/rewards")
    public Object rewards(@PathVariable("projectId") String projectId) {
        Project project = projectTrapdoor.get(new ObjectId(projectId));
        Collection<ObjectId> rewards = project.getRewards();
        List<Reward> rewardList = mongoTemplate.find(Query.query(Criteria.where("_id").in(rewards)), Reward.class);
        rewardList.sort((o1, o2) -> o1.getPrice().compareTo(o2.getPrice()));
        ModelAndView mav = new ModelAndView("6_3");
        mav.addObject("project", project);
        mav.addObject("state", project.getState().name());
        mav.addObject("rewards", rewardList);
        return mav;
    }

    @RequestMapping("/{projectId}/mobile/buyer")
    public Object buyer(@PathVariable("projectId") String projectId, @RequestParam(value = "index", required = false, defaultValue = "0") int index) {
        Project project = projectTrapdoor.get(new ObjectId(projectId));
        if (project.getState() == ProjectState.REVIEWING) {
            return "redirect:/projects/" + projectId + "/review";
        }
        ModelAndView mav = new ModelAndView("6_1_3");
        //rewards
        Collection<ObjectId> rewards = project.getRewards();
        List<Reward> rewardList = mongoTemplate.find(Query.query(Criteria.where("_id").in(rewards)), Reward.class);
        Collections.sort(rewardList, ((o1, o2) -> o1.getPrice().compareTo(o2.getPrice())));
        mav.addObject("rewards", rewardList);
        //orders
        List<Order> orderList = orderTrapdoor.findByProjectId(project.getId());
        //sort by rewards
        List<ObjectId> userIds = orderList.stream().map((o -> o.getUid())).collect(Collectors.toList());
        Map<ObjectId, Setting> settingMap = settingTrapdoor.findByIds(userIds);
        Map<String, Set<Setting>> sortMap = new LinkedHashMap<>();
        Set<Setting> allBuyers = new HashSet<>();
        for (Reward reward : rewardList) {
            sortMap.put(reward.getId().toHexString(), new LinkedHashSet<>());
        }
        for (Order order : orderList) {
            if (order.isPaid()) {
                Set<Setting> settings = sortMap.get(order.getRewardId().toHexString());
                settings.add(settingMap.get(order.getUid()));
                allBuyers.add(settingMap.get(order.getUid()));
            }
        }
        mav.addObject("buyerMap", sortMap);
        mav.addObject("allBuyers", allBuyers);
        mav.addObject("index", index);
        mav.addObject("projectId", projectId);
        mav.addObject("bookName", project.getBookName());
        return mav;
    }

    @RequestMapping(value = "/{projectId}/bid", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Object getBid(@PathVariable("projectId") String projectId, Principal principal) {

        Project project = projectTrapdoor.get(new ObjectId(projectId));
        ModelAndView mav = new ModelAndView("6_8");
        ObjectId uid = new ObjectId(principal.getName());
        if (publisherTrapdoor.isVerified(uid)) {
            mav.addObject("bidAuth", true);
        } else if (!project.getUid().equals(uid)) {
            return "redirect:/projects/" + projectId;
        }
        //bids
        List<Bid> bids = bidTrapdoor.findByProjectId(project.getId());
        List<ObjectId> bidUids = new ArrayList<>();
        for (Bid bid : bids) {
            bidUids.add(bid.getUid());
        }
        Map<ObjectId, Setting> bidUser = settingTrapdoor.findByIds(bidUids);
        Map<ObjectId, Company> companyById = companyTrapdoor.findById(bidUids);
        List<Map<String, String>> bidModels = new ArrayList<>();
        for (Bid bid : bids) {
            Map<String, String> model = new HashMap<>();
            Setting setting = bidUser.get(bid.getUid());
            model.put("uid", bid.getUid().toHexString());
            model.put("avatar", setting.getAvatar());
            model.put("displayName", setting.getDisplayName());
            if (companyById.containsKey(bid.getUid())) {
                model.put("organizeName", companyById.get(bid.getUid()).getCompanyName());
            }
            model.put("price", Long.toString(bid.getPrice().to(PriceUnit.YUAN)));
            bidModels.add(model);
        }
        Collections.sort(bidModels, ((o1, o2) -> Integer.compare(Integer.parseInt(o1.get("price")), Integer.parseInt
                (o2.get("price")))));
        mav.addObject("bids", bidModels);
        mav.addObject("projectId", project.getId());
        mav.addObject("publisherId", project.getPublisher());
        return mav;
    }

    @RequestMapping(value = "/{projectId}/bid", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object bid(@RequestParam("price") long price, @PathVariable("projectId") String projectId, Principal
            principal, Locale locale) {
        ObjectId uid = new ObjectId(principal.getName());
        if (publisherTrapdoor.isVerified(uid)) {
            Map<String, Object> retMap = new HashMap<>();
            StringBuilder sb = new StringBuilder();
            Setting setting = settingTrapdoor.get(uid);
            sb.append(setting.getDisplayName());
            Company company = companyTrapdoor.get(uid);
            if (company != null) {
                sb.append("(");
                sb.append(company.getCompanyName());
                sb.append(")");
            }
            retMap.put("name", sb.toString());
            retMap.put("id", principal.getName());
            retMap.put("price", price);
            bidTrapdoor.save(new Bid(new ObjectId(principal.getName()), new ObjectId(projectId), new Price(price,
                    PriceUnit.YUAN)));
            return Ajax.ok(retMap);
        } else {
            return Ajax.failure(messageSource.getMessage("project.bid.noaccess", null, locale));
        }
    }

    private Page<View> getProjectHomeViews(Pageable pageable, Locale locale) {
        Page<Project> projectPage = projectTrapdoor.globalProject(pageable);
        List<Project> content = projectPage.getContent();
        List<ObjectId> authorIds = content.stream().map((project -> project.getUid())).collect(Collectors.toList());
        Map<ObjectId, Setting> settingMap = settingTrapdoor.findByIds(authorIds);
        return projectPage.map((project) -> new View(project.getId().toHexString(), project.getUid().toHexString(),
                IMAGE_CONTEXT + settingMap.get(project.getUid()).getAvatar(), settingMap.get(project.getUid())
                .getDisplayName(), project.getCurrentBalance().to(PriceUnit.YUAN), project.getGoal().to(PriceUnit
                .YUAN), project.getCreateTime(), project.getUpdateTime(), project.getDeadline(), project.getPublisher
                (), project.getBookName(), project.getTypes() != null ? project.getTypes().stream().map(type -> bookUtils.parse(type, locale)).collect(Collectors.toList()) : null, project.getTags(), project.getDescription(), IMAGE_CONTEXT + project
                .getCover(), project.getState(), project.getImages(), project.getOutline(), project.getDraft()));
    }

    public static class AuthorView {

        private ObjectId uid;

        private String name;

        private String avatar;

        private String description;

        public AuthorView(ObjectId uid, String name, String avatar, String description) {
            this.uid = uid;
            this.name = name;
            this.avatar = avatar;
            this.description = description;
        }

        public ObjectId getUid() {

            return uid;
        }

        public String getName() {
            return name;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class View {

        private String projectId;

        private String uid;

        private String avatar;

        private String authorName;

        private long currentMoney;

        private long goal;

        private Date createTime;

        private Date updateTime;

        private Date deadline;

        private boolean isPublisherLocked;

        private String bookName;

        private Collection<String> types;

        private Collection<String> tags;

        private Collection<String> images;

        private String description;

        private String cover;

        private String outline;

        private String draft;

        private ProjectState state;

        public View(String projectId, String uid, String avatar, String authorName, long currentMoney, long goal,
                    Date createTime, Date updateTime, Date deadline, ObjectId publisherId, String bookName,
                    Collection<String> types, Collection<String> tags, String description, String cover, ProjectState state, Collection<String>
                            images, String outline, String draft) {
            this.projectId = projectId;
            this.uid = uid;
            this.avatar = avatar;
            this.authorName = authorName;
            this.updateTime = updateTime;
            this.currentMoney = currentMoney;
            this.goal = goal;
            this.createTime = createTime;
            this.deadline = deadline;
            this.isPublisherLocked = publisherId != null;
            this.bookName = bookName;
            this.types = types;
            this.tags = tags;
            this.description = description;
            this.cover = cover;
            this.state = state;
            this.images = images;
            this.outline = outline;
            this.draft = draft;
        }

        public Collection<String> getTypes() {
            return types;
        }

        public void setTypes(Collection<String> types) {
            this.types = types;
        }

        public String getUid() {
            return uid;
        }

        public String getOutline() {
            return outline;
        }

        public String getDraft() {
            return draft;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public Collection<String> getImages() {
            return images;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public Date getDeadline() {
            return deadline;
        }

        public ProjectState getState() {
            return state;
        }

        public String getProjectId() {

            return projectId;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getAuthorName() {
            return authorName;
        }

        public long getCurrentMoney() {
            return currentMoney;
        }

        public long getGoal() {
            return goal;
        }

        public int getTotalDays() {
            return (int) TimeUnit.DAYS.convert(deadline.getTime() - createTime.getTime(), TimeUnit.MILLISECONDS);
        }

        public int getPassedDays() {
            int passedDays = (int) TimeUnit.DAYS.convert(new Date().getTime() - createTime.getTime(), TimeUnit
                    .MILLISECONDS);
            int totalDays = getTotalDays();
            return passedDays > totalDays ? totalDays : passedDays;
        }

        public boolean isDeadline() {
            return deadline.before(new Date());
        }

        public boolean isPublisherLocked() {
            return isPublisherLocked;
        }

        public String getBookName() {
            return bookName;
        }

        public Collection<String> getTags() {
            return tags;
        }

        public String getDescription() {
            return description;
        }

        public String getCover() {
            return cover;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    public static class ProjectPreview {

        private ObjectId id;

        private ObjectId uid; //owner，版权所属人

        private String firstAuthorDescription; //owner 简介

        private List<Project.Author> authors; //作者

        private String bookName;

        private BookType type;

        private List<String> types;

        private String description;

        private Collection<String> tags;

        private String cover;

        private Collection<String> images;

        private String outline;

        private String draft;

        private int colorMode;

        private int publishMode;

        private int wordCount;

        private int imageCount;

        private Date deadline;

        private Date createTime;

        private Date updateTime;

        private Price currentBalance;

        private Price goal;

        //锁定的出版人，由后台审核后填入
        private ObjectId publisher;

        private Collection<ObjectId> rewards;

        private ProjectState state;

        private int progressCode;

        private ProjectPreview() {
        }

        public ProjectPreview(ObjectId uid, String firstAuthorDescription, List<Project.Author> authors, String bookName, List<String> types, String description,
                              Collection<String> tags, String cover, Collection<String> images, String outline, String draft,
                              int colorMode, int wordCount, int imageCount) {
            this.uid = uid;
            this.authors = authors;
            this.firstAuthorDescription = firstAuthorDescription;
            this.bookName = bookName;
            this.types = types;
            this.description = description;
            this.tags = tags;
            this.cover = cover;
            this.images = images;
            this.outline = outline;
            this.draft = draft;
            this.colorMode = colorMode;
            this.wordCount = wordCount;
            this.imageCount = imageCount;
            this.id = ObjectId.get();
            this.state = ProjectState.REVIEWING;
            this.createTime = new Date();
            this.rewards = new ArrayList<>();
            this.currentBalance = new Price(0, PriceUnit.CENT);
            this.updateTime = new Date();
            this.progressCode = 0;
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

        public String getFirstAuthorDescription() {
            return firstAuthorDescription;
        }

        public void setFirstAuthorDescription(String firstAuthorDescription) {
            this.firstAuthorDescription = firstAuthorDescription;
        }

        public List<Project.Author> getAuthors() {
            return authors;
        }

        public void setAuthors(List<Project.Author> authors) {
            this.authors = authors;
        }

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        public BookType getType() {
            return type;
        }

        public void setType(BookType type) {
            this.type = type;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Collection<String> getTags() {
            return tags;
        }

        public void setTags(Collection<String> tags) {
            this.tags = tags;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public Collection<String> getImages() {
            return images;
        }

        public void setImages(Collection<String> images) {
            this.images = images;
        }

        public String getOutline() {
            return outline;
        }

        public void setOutline(String outline) {
            this.outline = outline;
        }

        public String getDraft() {
            return draft;
        }

        public void setDraft(String draft) {
            this.draft = draft;
        }

        public int getColorMode() {
            return colorMode;
        }

        public void setColorMode(int colorMode) {
            this.colorMode = colorMode;
        }

        public int getPublishMode() {
            return publishMode;
        }

        public void setPublishMode(int publishMode) {
            this.publishMode = publishMode;
        }

        public int getWordCount() {
            return wordCount;
        }

        public void setWordCount(int wordCount) {
            this.wordCount = wordCount;
        }

        public int getImageCount() {
            return imageCount;
        }

        public void setImageCount(int imageCount) {
            this.imageCount = imageCount;
        }

        public Date getDeadline() {
            return deadline;
        }

        public void setDeadline(Date deadline) {
            this.deadline = deadline;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }

        public Price getCurrentBalance() {
            return currentBalance;
        }

        public void setCurrentBalance(Price currentBalance) {
            this.currentBalance = currentBalance;
        }

        public Price getGoal() {
            return goal;
        }

        public void setGoal(Price goal) {
            this.goal = goal;
        }

        public ObjectId getPublisher() {
            return publisher;
        }

        public void setPublisher(ObjectId publisher) {
            this.publisher = publisher;
        }

        public Collection<ObjectId> getRewards() {
            return rewards;
        }

        public void setRewards(Collection<ObjectId> rewards) {
            this.rewards = rewards;
        }

        public ProjectState getState() {
            return state;
        }

        public void setState(ProjectState state) {
            this.state = state;
        }

        public int getProgressCode() {
            return progressCode;
        }

        public void setProgressCode(int progressCode) {
            this.progressCode = progressCode;
        }
    }
}
