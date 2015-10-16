package com.zanshang;

import com.zanshang.framework.Price;
import com.zanshang.framework.PriceUnit;
import com.zanshang.framework.spring.MongodbUserDetailsManager;
import com.zanshang.models.EmailAccount;
import com.zanshang.models.wit.WitProject;
import com.zanshang.models.wit.WitReward;
import com.zanshang.notify.Notifier;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.Json;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class AppTests {

    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected AkkaTrapdoor akkaTrapdoor;

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    protected MongodbUserDetailsManager userDetailsManager;

    @Autowired
    PropertyPlaceholderConfigurer helper;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    Notifier notifier;

    @Autowired
    ApplicationContext spring;

    @Test
    public void simple() throws Exception {
//        WitProject witProject = new WitProject("成功学分享", "来自大师唐大山的成功学", null, "地点:;;;;;", "星期日上午八点半");
//        WitReward w1 = new WitReward(witProject.getId(), "这是一个非常棒的回报", 288, new Price(288, PriceUnit.YUAN));
//        WitReward w2 = new WitReward(witProject.getId(), "这是一个比非常棒更棒的回报", 12, new Price(2000, PriceUnit.YUAN));
//        List<ObjectId> witRewards = new ArrayList<>();
//        witRewards.add(w1.getId());
//        witRewards.add(w2.getId());
//        witProject.setRewards(witRewards);
//        mongoTemplate.save(witProject);
//        mongoTemplate.save(w1);
//        mongoTemplate.save(w2);

//        Cache ticketindex = cacheManager.getCache("ticketindex");
////        List<String> newTickets = new ArrayList<>();
////        newTickets.add("diyitiao");
////        ticketindex.put("123123123", Json.toJson(newTickets));
////        Cache ticketCache = cacheManager.getCache("ticket");
//        List<String> tickets = Json.fromJson(ticketindex.get("123123123", String.class), ArrayList.class);
//        for (String t: tickets) {
////            if (ticketCache.get(t) != null) {
////                newTickets.add(t);
////            }
//            System.out.println(t);
//        }

//            Map<String, String> model = new HashMap<>();
//            model.put("bookName", "bookName");
//            model.put("projectId", "projectId");
//            model.put("key", "value");
//            notifier.notify(new ObjectId("55b92752421aa9519dbd2d58"), NotifyBusinessType.PROJECT_PROGRESS_FIRST_WEEK, model);
//        EmailAccount emailAccount = mongoTemplate.findById("vip6ming@126.com", EmailAccount.class);
//
//        System.out.println(emailAccount.getEmail());
//        Configuration conf = new Configuration(Configuration.VERSION_2_3_22);
//        conf.setDefaultEncoding("utf-8");
//        conf.setLocalizedLookup(true);
//        conf.setTemplateLoader(new SpringTemplateLoader(spring, "/WEB-INF/notification/"));
//        Template template;
//        template = conf.getTemplate("email/project_create_success" + ".ftl");
//        String message = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//        System.out.println(message);


//        Configuration conf = new Configuration(Configuration.VERSION_2_3_22);
//        conf.setDefaultEncoding("UTF-8");
//        conf.setLocalizedLookup(true);
//        StringTemplateLoader templateLoader = new StringTemplateLoader();
//        String name = "firstTemplate";
//        templateLoader.putTemplate(name, "<#escape x as x?html>" +
//                "${text}" +
//                "<pre> ${text}</pre>" +
//                "</#escape>");
//        conf.setTemplateLoader(templateLoader);
//        Template test = conf.getTemplateName(name);
//        Map<String, String> model = new HashMap<>();
//        model.put("text", "<a href='#hi'>TextForA</a>");
//        String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(test, model);
//        System.out.println(mailContent);
    }
}
