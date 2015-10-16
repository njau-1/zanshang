package com.zanshang.config.spring;

import akka.actor.ActorSystem;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Lookis on 6/25/15.
 */
@Configuration
public class AkkaConfig {

    @Bean
    public ActorSystem actorSystem() {
        return ActorSystem.create("springSystem", ConfigFactory.parseString("akka {" +
                "actor {" +
                "   guardian-supervisor-strategy = \"com.zanshang.framework.MySupervisorStrategy\"" +
                "}" +
                "}"));

    }

}
