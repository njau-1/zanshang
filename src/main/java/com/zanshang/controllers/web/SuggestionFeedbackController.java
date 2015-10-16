package com.zanshang.controllers.web;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.zanshang.framework.Ticket;
import com.zanshang.models.SuggestionFeedback;
import com.zanshang.services.feedback.SuggestionFeedbackSaveActor;
import com.zanshang.utils.Ajax;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

/**
 * Created by xuming on 15/9/24.
 */
@Controller
@RequestMapping(value = "/suggestionfeedback")
public class SuggestionFeedbackController implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ActorSystem actorSystem;

    ActorRef suggestionfeedbackSaveActor;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        suggestionfeedbackSaveActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(SuggestionFeedbackSaveActor.class, applicationContext)));
    }

    @RequestMapping(value = "/mobile", method = RequestMethod.GET)
    public Object createfeedbackMobile() {
        ModelAndView mav = new ModelAndView("feedback");
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object createfeedback(@Ticket String ticket, Principal principal, @RequestParam(value = "content") String content, @RequestParam(value = "contact") String contact) {
        SuggestionFeedback feedback = new SuggestionFeedback(content, contact, ticket, principal != null? new ObjectId(principal.getName()):null);
        suggestionfeedbackSaveActor.tell(feedback, ActorRef.noSender());
        return Ajax.ok();
    }

    @RequestMapping(value = "/mobile", method = RequestMethod.POST)
    public Object createfeedbackMobile(@Ticket String ticket, Principal principal, @RequestParam(value = "content") String content, @RequestParam(value = "contact") String contact) {
        ModelAndView mav = new ModelAndView("feedbacksuccess");
        SuggestionFeedback feedback = new SuggestionFeedback(content, contact, ticket, principal != null? new ObjectId(principal.getName()):null);
        suggestionfeedbackSaveActor.tell(feedback, ActorRef.noSender());
        return mav;
    }
}
