package com.zanshang.framework;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.TypedActor;
import akka.japi.pf.DeciderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lookis on 6/25/15.
 */
public abstract class TrapdoorSupervisor implements TypedActor.Supervisor {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(true, DeciderBuilder.match(Throwable.class, (e -> {
            logger.error("Ops.", e);
            return SupervisorStrategy.escalate();
        })).build());
    }
}
