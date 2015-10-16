package com.zanshang.controllers.market;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.services.wechat.WechatJSApiTicketGetActor;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.Request;
import com.zanshang.utils.WechatUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lookis on 8/7/15.
 */
@Controller
@RequestMapping("/marketing")
public class MarketController implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ActorSystem actorSystem;

    @Value("${WECHAT_MP_ID}")
    String MP_ID;

    ActorRef wechatJSApiTicketGetActor;

    @RequestMapping("/h5/{path}/{page}")
    public Object h5(@PathVariable("path") String path, @PathVariable("page") String page, Date requestTime,
                     HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/market/h5/" + path + "/" + page);
        ArrayList<Future<Object>> futures = new ArrayList<Future<Object>>();
        Future<Object> ticketFuture = Patterns.ask(wechatJSApiTicketGetActor, "", ActorConstant.REMOTE_CALL_TIMEOUT);
        futures.add(ticketFuture.andThen(new OnComplete<Object>() {
            @Override
            public void onComplete(Throwable throwable, Object o) throws Throwable {
                if (o != null) {
                    Map<String, String> config = new HashMap<>();
                    config.put("timestamp", Long.toString(requestTime.getTime() / 1000));
                    config.put("url", Request.buildCurrentURL(request));
                    config.put("nonceStr", RandomStringUtils.randomAlphanumeric(32));
                    config.put("signature", WechatUtils.signSha1(config, o.toString()));
                    config.remove("url");
                    config.put("appId", MP_ID);
                    mav.addObject("config", config);
                } else {
                    logger.error("Future Error.", throwable);
                }
            }
        }, actorSystem.dispatcher()));
        try {
            Await.result(Futures.sequence(futures, actorSystem.dispatcher()), Duration.Inf());
        } catch (Exception e) {
            logger.warn("Timeout Of Wechat", e);
        }
        return mav;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        wechatJSApiTicketGetActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(WechatJSApiTicketGetActor
                .class, applicationContext)));
    }
}
