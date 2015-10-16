package com.zanshang.controllers.web;

import com.zanshang.models.OldBookMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jump from http://www.zan-shang.com/index.php/index/bookView/bid/320
 * Created by Lookis on 7/17/15.
 */
@Controller
@RequestMapping(value = "/index.php/index")
public class OldLinkJumpController {

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping(value = "/bookView/bid/{bid}")
    public String bookView(@PathVariable("bid") String bid, HttpServletRequest request) {
        OldBookMapping mapping = mongoTemplate.findById(bid, OldBookMapping.class);
        if (mapping == null) {
            return "redirect:/";
        } else {
            return "redirect:" + ProjectController.PATH + "/" + mapping.getProjectId();
        }
    }
}
