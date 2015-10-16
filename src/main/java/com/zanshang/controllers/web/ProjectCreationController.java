package com.zanshang.controllers.web;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.zanshang.constants.BookType;
import com.zanshang.constants.ProjectState;
import com.zanshang.framework.Price;
import com.zanshang.framework.PriceUnit;
import com.zanshang.framework.PriceUtils;
import com.zanshang.models.*;
import com.zanshang.notify.Notifier;
import com.zanshang.notify.constants.FreeMarkerModelParamKey;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.services.*;
import com.zanshang.services.author.AuthorTrapdoorImpl;
import com.zanshang.services.company.CompanyTrapdoorImpl;
import com.zanshang.services.mailbox.TimingEmailCreateActor;
import com.zanshang.services.person.PersonTrapdoorImpl;
import com.zanshang.services.project.ProjectTrapdoorImpl;
import com.zanshang.services.setting.SettingTrapdoorImpl;
import com.zanshang.utils.Ajax;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.BookUtils;
import com.zanshang.utils.Json;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Lookis on 6/27/15.
 */
@Controller
@RequestMapping("/projects")
public class ProjectCreationController implements ApplicationContextAware {

    public static final String NAME = "name";

    public static final String IDENTITY = "identity";

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    MessageSource messageSource;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @Autowired
    BookUtils bookUtils;

    @Autowired
    Notifier notifier;

    @Value("${IMAGE_CONTEXT}")
    String IMAGE_CONTEXT;

    ProjectTrapdoor projectTrapdoor;

    AuthorTrapdoor authorService;

    PersonTrapdoor personService;

    SettingTrapdoor settingService;

    CompanyTrapdoor companyService;

    ActorRef timingEmailCreateActor;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    protected void init() {
        authorService = akkaTrapdoor.createTrapdoor(AuthorTrapdoor.class, AuthorTrapdoorImpl.class);
        projectTrapdoor = akkaTrapdoor.createTrapdoor(ProjectTrapdoor.class, ProjectTrapdoorImpl.class);
        personService = akkaTrapdoor.createTrapdoor(PersonTrapdoor.class, PersonTrapdoorImpl.class);
        settingService = akkaTrapdoor.createTrapdoor(SettingTrapdoor.class, SettingTrapdoorImpl.class);
        companyService = akkaTrapdoor.createTrapdoor(CompanyTrapdoor.class, CompanyTrapdoorImpl.class);
        timingEmailCreateActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(TimingEmailCreateActor.class, applicationContext)));
    }

    //项目创建页面
    @RequestMapping(value = "/creation", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Object form(Principal principal) {
        ModelAndView mav = new ModelAndView("5_1_1");
        Setting setting = settingService.get(new ObjectId(principal.getName()));
        boolean filled = authorService.isFilled(new ObjectId(principal.getName()));
        if (filled) {
            Person person = personService.get(new ObjectId(principal.getName()));
            //现在我们只有个人可以创建项目，也是因为只有这个流程设计，所以这里assert一下
//            Assert.notNull(person);

            Company company = companyService.get(new ObjectId(principal.getName()));
            Assert.isTrue(person != null || company != null);
            if (person != null) {
                mav.addObject("legalname", person.getLegalName());
                mav.addObject("authorId", person.getIdentityCode());
                mav.addObject("authorFront", person.getIdentityFront());
                mav.addObject("authorBack", person.getIdentityBack());
            }else if (company != null){
                mav.addObject("legalname", company.getCompanyName());
                mav.addObject("authorId", company.getCompanyCode());
                mav.addObject("authorFront", company.getLicense());
                mav.addObject("authorBack", company.getLicense());
            }else {
                Assert.isTrue(false);
            }
        }
        mav.addObject("setting", setting);
        mav.addObject("isOpen", !filled);
        return mav;
    }

    //发起项目, 草稿阶段, 完成书籍的部分
    @RequestMapping(method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object create(@RequestParam(value = "name", required = false) String name, //
                         @RequestParam(value = "types[]",
                                 required = false) List<String> types, //
                         @RequestParam(value = "description", required = false) String description, //
                         @RequestParam(value = "cover", defaultValue = "/static/default_cover.png") String cover, //
                         @RequestParam(value = "aboutFirstAuthor", required = false) String aboutFirstAuthor, //
                         @RequestParam(value = "outline", required = false) String outline, //
                         @RequestParam(value = "draft", required = false) String draft, //
                         @RequestParam(value = "color", required = false) int color, //
                         @RequestParam(value = "wordCount", required = false) int wordCount, //
                         @RequestParam(value = "imageCount", required = false) int imageCount, //
                         @RequestParam(value = "images[]", required = false) List<String> images, //
                         @RequestParam(value = "tags[]", required = false) List<String> tags, //
                         @RequestParam(value = "authorId", required = false) String authorId, //
                         @RequestParam(value = "authorName", required = false) String authorName, //
                         @RequestParam(value = "authorIdFront", required = false) String authorIdFront, //
                         @RequestParam(value = "authorIdBack", required = false) String authorIdBack,//
                         HttpServletRequest request,
                         Principal principal, Locale locale) {
        ObjectId uid = new ObjectId(principal.getName());
        List<Project.Author> authorList = new ArrayList<>();
        boolean filled = authorService.isFilled(uid);
        Assert.isTrue(filled || (StringUtils.isNotEmpty(authorId) && (StringUtils.isNotEmpty(authorName) &&
                (StringUtils.isNotEmpty(authorIdFront) && (StringUtils.isNotEmpty(authorIdBack))))));
        if (!filled) {
            //            Map<String, String> authorInformation = getAuthorInformation(uid);
            //            authorList.add(new Project.Author(authorInformation.get(IDENTITY), authorInformation.get
            // (NAME),
            //                    aboutFirstAuthor));
            //        } else {
            //            authorList.add(new Project.Author(authorId, authorName, aboutFirstAuthor));
            Person person = personService.get(uid);
            //我们的页面里只设计了个人作者，对于机构作者来说，他们在注册的时候已经填好了所有的信息，所以不会落到这个判断里
            Company company = companyService.get(uid);
            Assert.isTrue(person != null || company != null);
            if (person != null) {
                person.setLegalName(authorName);
                person.setIdentityFront(authorIdFront);
                person.setIdentityBack(authorIdBack);
                person.setIdentityCode(authorId);
                personService.save(person);
            }else if (company != null){

            }else {
                Assert.isTrue(false);
            }
        }
        String[] params = request.getParameterValues("authors[]");
        if (params != null && params.length != 0) {
            for (String s : params) {
                Project.Author author = Json.fromJson(s, Project.Author.class);
                authorList.add(author);
            }
        }
        List<BookType> typeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(types)) {
            for (String t : types) {
                BookType type = bookUtils.parse(t, locale);
                typeList.add(type);
            }
        }
        Project project = new Project(uid, aboutFirstAuthor, authorList, name, typeList,
                description, tags, cover, images == null ? new ArrayList<>():images, outline, draft, color, wordCount, imageCount);
        projectTrapdoor.save(project);
        return Ajax.ok();
    }

    //审核通过前的编辑表单
    @RequestMapping(value = "/{projectId}/review", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Object preview(@PathVariable("projectId") String projectId, Principal principal, Locale locale) {
        Project project = projectTrapdoor.get(new ObjectId(projectId));
        if (!project.getUid().toHexString().equals(principal.getName()) || project.getState() != ProjectState
                .REVIEWING) {
            return "redirect:/projects";
        } else {

            ModelAndView mav = new ModelAndView("5_1_1");
            ObjectId uid = new ObjectId(principal.getName());
            Map<String, String> authorInformation = getAuthorInformation(uid);
            List<Project.Author> authors = project.getAuthors();
            List<Author> formAuthors = new ArrayList<>();
            for (Project.Author author : authors) {
                formAuthors.add(new Author(author.getIdentity(), author.getName(), author.getDescription()));
            }
            mav.addObject("aboutFirstAuthor", project.getFirstAuthorDescription());
            mav.addObject("authors", formAuthors);
            mav.addObject("color", project.getColorMode());
            mav.addObject("cover", project.getCover());
            mav.addObject("description", project.getDescription());
            mav.addObject("draft", project.getDraft());
            mav.addObject("imageCount", project.getImageCount());
            if (project.getImages() != null) {
                mav.addObject("images", new ArrayList<>(project.getImages()));
            }
            mav.addObject("wordCount", project.getWordCount());
            mav.addObject("name", project.getBookName());
            mav.addObject("outline", project.getOutline());
            List<BookType> typeList = project.getTypes();
            List<String> formTypes = new ArrayList<>();
            if (typeList != null) {
                for (BookType type : typeList) {
                    formTypes.add(messageSource.getMessage(type.getMessageCode(), null, locale));
                }
                mav.addObject("types", formTypes);
            }
            if (project.getTags() != null) {
                mav.addObject("tags", new ArrayList<>(project.getTags()));
            } else {
                mav.addObject("tags", new ArrayList<>());
            }
            Setting setting = settingService.get(new ObjectId(principal.getName()));
            boolean filled = authorService.isFilled(new ObjectId(principal.getName()));
            mav.addObject("setting", setting);
            mav.addObject("isOpen", !filled);
            mav.addObject("projectId", projectId);
            Person person = personService.get(new ObjectId(principal.getName()));
            //现在我们只有个人可以创建项目，也是因为只有这个流程设计，所以这里assert一下
//            Assert.notNull(person);
            if (person != null) {
                mav.addObject("legalname", person.getLegalName());
                mav.addObject("authorId", person.getIdentityCode());
                mav.addObject("authorFront", person.getIdentityFront());
                mav.addObject("authorBack", person.getIdentityBack());
            }else {
                Company company = companyService.get(new ObjectId(principal.getName()));
                Assert.notNull(company);
                mav.addObject("legalname", company.getCompanyName());
                mav.addObject("authorId", company.getCompanyCode());
                mav.addObject("authorFront", company.getLicense());
                mav.addObject("authorBack", company.getLicense());
            }
            return mav;
        }
    }

    //编辑项目，审核阶段
    @RequestMapping(value = "/{projectId}", method = RequestMethod.PUT)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object edit(@RequestParam(value = "name", required = false) String name, //
                       @RequestParam(value = "types[]",
                               required = false) List<String> types, //
                       @RequestParam(value = "description", required = false) String description, //
                       @RequestParam(value = "cover", required = false) String cover, //
                       @RequestParam(value = "aboutFirstAuthor", required = false) String aboutFirstAuthor, //
                       @RequestParam(value = "outline", required = false) String outline, //
                       @RequestParam(value = "draft", required = false) String draft, //
                       @RequestParam(value = "color", required = false) int color, //
                       @RequestParam(value = "wordCount", required = false) int wordCount, //
                       @RequestParam(value = "imageCount", required = false) int imageCount, //
                       @RequestParam(value = "images[]", required = false) List<String> images, //
                       @RequestParam(value = "tags[]", required = false) List<String> tags, //
                       HttpServletRequest request,
                       @PathVariable("projectId") String projectId, Principal principal, Locale locale, Date requestTime) {
        Project project = projectTrapdoor.get(new ObjectId(projectId));
        if (!project.getUid().toHexString().equals(principal.getName()) || project.getState() != ProjectState
                .REVIEWING) {
            return Ajax.failure(messageSource.getMessage("exception.expire", null, locale));
        } else {
            project.setColorMode(color);
            project.setCover(cover);
            project.setDescription(description);
            project.setDraft(draft);
            project.setImageCount(imageCount);
            project.setImages(images == null ? new ArrayList<>():images);
            project.setWordCount(wordCount);
            project.setBookName(name);
            project.setOutline(outline);
            List<BookType> typeList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(types)) {
                for (String t : types) {
                    BookType type = bookUtils.parse(t, locale);
                    typeList.add(type);
                }
            }
            project.setTypes(typeList);
            project.setTags(tags);
            List<Project.Author> authorList = new ArrayList<>();
            String[] params = request.getParameterValues("authors[]");
            if (params != null && params.length != 0) {
                for (String s : params) {
                    Project.Author author = Json.fromJson(s, Project.Author.class);
                    authorList.add(author);
                }
            }
            project.setFirstAuthorDescription(aboutFirstAuthor);
            project.setAuthors(authorList);
            project.setUpdateTime(requestTime);
            projectTrapdoor.save(project);
            return Ajax.ok();
        }
    }

    //定价, 表单
    @RequestMapping(value = "/{projectId}/pricing", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Object pricing(@PathVariable("projectId") String projectId, Principal principal) {
        Project project = projectTrapdoor.get(new ObjectId(projectId));
        if (!project.getUid().toHexString().equals(principal.getName()) || project.getState() != ProjectState.PRICING) {
            return "redirect:/projects";
        } else {
            ProjectFeedback feedback = mongoTemplate.findById(projectId, ProjectFeedback.class);
            if (feedback == null || !feedback.isPass()) {
                return "redirect:/projects";
            } else {
                ModelAndView mav = new ModelAndView("5_4");
                Calendar instance = Calendar.getInstance();
                instance.add(Calendar.DAY_OF_MONTH, 90);
                mav.addObject("pricingForm", new PricingForm());
                mav.addObject("project", project);
                mav.addObject("feedbackCost", feedback.getCost().to(PriceUnit.YUAN));
                mav.addObject("feedbackGoal", feedback.getGoal().to(PriceUnit.YUAN));
                mav.addObject("deadline", instance.getTime());
                return mav;
            }
        }
    }

    //定价, 表单提交
    @RequestMapping(value = "/{projectId}/pricing", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public Object createPrices(@PathVariable("projectId") String projectId, @Valid PricingForm pricingForm,
                               BindingResult result, Principal principal, Map<String, Object> model, Locale locale, Date requestTime) {
        Project project = projectTrapdoor.get(new ObjectId(projectId));
        if (!project.getUid().toHexString().equals(principal.getName()) || project.getState() != ProjectState.PRICING) {
            return "redirect:/projects";
        } else {
            ProjectFeedback feedback = mongoTemplate.findById(projectId, ProjectFeedback.class);
            if (feedback == null || !feedback.isPass()) {
                return "redirect:/projects";
            } else if (result.hasErrors()) {
                model.put("project", project);
                model.put("pricingForm", new PricingForm());
                model.put("feedbackCost", feedback.getCost().to(PriceUnit.YUAN));
                model.put("feedbackGoal", feedback.getGoal().to(PriceUnit.YUAN));
                return "5_4";
            } else {
                Assert.isTrue(PriceUtils.gte(new Price(pricingForm.getGoal(), PriceUnit.YUAN), feedback.getGoal()));
                List<PricingForm.Reward> rewards = pricingForm.getRewards();
                List<Reward> projectRewards = new ArrayList<>();
                List<ObjectId> rewardIds = new ArrayList<>();
                for (PricingForm.Reward reward : rewards) {
                    Assert.isTrue(PriceUtils.gte(new Price(reward.getPrice(), PriceUnit.YUAN), feedback.getCost()));
                    Map<Reward.Item, Reward.Detail> itemDetailMap = new LinkedHashMap<>();
                    itemDetailMap.put(Reward.Item.BOOK, new Reward.Detail(reward.getBookCount(), null));
                    itemDetailMap.put(Reward.Item.VIP, new Reward.Detail(1, null));
                    itemDetailMap.put(Reward.Item.SIGNATURE, new Reward.Detail(1, null));
                    if (StringUtils.isNotEmpty(reward.getOther())) {
                        itemDetailMap.put(Reward.Item.OTHER, new Reward.Detail(1, reward.getOther()));
                    }
                    Reward projectReward = new Reward(project.getId(), itemDetailMap, new Price(reward.getPrice(),
                            PriceUnit.YUAN));
                    projectRewards.add(projectReward);
                    rewardIds.add(projectReward.getId());
                }
                project.setRewards(rewardIds);
                project.setGoal(new Price(pricingForm.getGoal(), PriceUnit.YUAN));
                project.setDeadline(pricingForm.getDeadline());
                project.setState(ProjectState.FUNDING);
                project.setCreateTime(requestTime);
                project.setUpdateTime(requestTime);
                mongoTemplate.insert(projectRewards, Reward.class);
                projectTrapdoor.save(project);
                //notification
                Map<String, String> templateModel = new HashMap<>();
                templateModel.put(FreeMarkerModelParamKey.BOOKNAME.getKey(), project.getBookName());
                templateModel.put(FreeMarkerModelParamKey.PROJECTID.getKey(), project.getId().toHexString());
                templateModel.put(FreeMarkerModelParamKey.DEADLINE.getKey(), new SimpleDateFormat("yyyy-MM-dd").format(project.getDeadline()));
                templateModel.put(FreeMarkerModelParamKey.COVER.getKey(), IMAGE_CONTEXT + project.getCover());
                notifier.notify(project.getUid(), NotifyBusinessType.PROJECT_CREATE_SUCCESS, templateModel);
                timingEmailCreateActor.tell(project, ActorRef.noSender());
                return "redirect:/projects/" + projectId;
            }
        }
    }

    private Map<String, String> getAuthorInformation(ObjectId uid) {
        Map<String, String> result = new HashMap<>();
        Person person = personService.get(uid);
        Setting setting = settingService.get(uid);
        result.put(NAME, setting.getDisplayName());
        if (person != null) {
            result.put(IDENTITY, person.getIdentityCode());
        } else {
            Company company = companyService.get(uid);
            result.put(IDENTITY, company.getCompanyCode());
        }
        return result;
    }

    public static class Author {

        private String identity;

        private String name;

        private String description;

        private Author() {
        }

        public Author(String identity, String name, String description) {
            this.identity = identity;
            this.name = name;
            this.description = description;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    static class PricingForm {

        @NotNull(message = "{pricing.deadline.notempty}")
        @DateTimeFormat(pattern = "dd/MM/yyyy")
        private Date deadline;

        @NotEmpty(message = "{pricing.rewards.notempty}")
        private List<Reward> rewards;

        private long goal;

        public PricingForm() {
        }

        public Date getDeadline() {
            return deadline;
        }

        public void setDeadline(Date deadline) {
            this.deadline = deadline;
        }

        public List<Reward> getRewards() {
            return rewards;
        }

        public void setRewards(List<Reward> rewards) {
            this.rewards = rewards;
        }

        public long getGoal() {
            return goal;
        }

        public void setGoal(long goal) {
            this.goal = goal;
        }

        public static class Reward {

            public Integer bookCount;

            public String other;

            public long price;

            public Reward() {
                this.bookCount = 1;
            }

            public void setBookCount(int bookCount) {
                this.bookCount = bookCount;
            }

            public void setOther(String other) {
                this.other = other;
            }

            public void setPrice(long price) {
                this.price = price;
            }

            public Integer getBookCount() {

                return bookCount;
            }

            public String getOther() {
                return other;
            }

            public long getPrice() {
                return price;
            }
        }
    }
}
