package com.zanshang.controllers.web;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.Price;
import com.zanshang.framework.PriceUnit;
import com.zanshang.models.global.CountPrice;
import com.zanshang.services.price.CountPriceGetActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.annotation.PostConstruct;

import java.security.Principal;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 5/14/15.
 */
@Controller
public class IndexController implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    ActorRef countPriceGetActor;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    protected void init() {
        countPriceGetActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(CountPriceGetActor.class, applicationContext)));
    }

    @RequestMapping(value = {"/", "/index"})
    public Object index(Device device, Principal principal) {
        if (device.isMobile()) {
            return "redirect:/projects";
        } else {
            ModelAndView mav = new ModelAndView("1_1");
            Future<Object> future = ask(countPriceGetActor, 1, ActorConstant.DEFAULT_TIMEOUT);
            try {
                CountPrice countPrice = (CountPrice) Await.result(future, Duration.Inf());
                String strCountPrice = String.valueOf(new Price(countPrice.getValue(), PriceUnit.CENT).to(PriceUnit.YUAN));
                mav.addObject("countPrice", strCountPrice.toCharArray());
            }catch (Exception e) {
                logger.error("Ops", e);
            }
            return mav;
        }
    }

    @RequestMapping(value = "/support/{url}")
    public String staticPages(@PathVariable("url") String url) {
        return url;
    }
}
