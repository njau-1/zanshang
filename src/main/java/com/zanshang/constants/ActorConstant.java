package com.zanshang.constants;

import akka.util.Timeout;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

/**
 * Created by Lookis on 6/2/15.
 */
public class ActorConstant {

    public static final FiniteDuration REMOTE_CALL_DURATION = Duration.create(10, "seconds");

    public static final Timeout REMOTE_CALL_TIMEOUT = new Timeout(REMOTE_CALL_DURATION);

    public static final FiniteDuration DEFAULT_TIMEOUT_DURATION = Duration.create(1, "seconds");

    public static final Timeout DEFAULT_TIMEOUT = new Timeout(DEFAULT_TIMEOUT_DURATION.mul(2));
}
