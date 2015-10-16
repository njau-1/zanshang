package com.zanshang.framework.filter;

import akka.actor.ActorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Lookis on 7/20/15.
 */
public class ActorSystemShutdownListener implements ServletContextListener{

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sce
                .getServletContext());
        ActorSystem bean = applicationContext.getBean(ActorSystem.class);
        if(bean!=null && !bean.isTerminated()){
            logger.info("Shutting down ActorSystem...");
            bean.shutdown();
            bean.awaitTermination();
        }
    }
}
