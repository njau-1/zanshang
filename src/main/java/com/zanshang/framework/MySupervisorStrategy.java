package com.zanshang.framework;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategyConfigurator;
import akka.japi.pf.DeciderBuilder;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;

public class MySupervisorStrategy implements SupervisorStrategyConfigurator {

    @Override
    public akka.actor.SupervisorStrategy create() {
        PartialFunction<Throwable, SupervisorStrategy.Directive> function = DeciderBuilder.match(RuntimeException.class, e -> {
            return SupervisorStrategy.resume();
        }).build();
        return new OneForOneStrategy(10, Duration.create(10, "seconds"), true, function);
    }
}