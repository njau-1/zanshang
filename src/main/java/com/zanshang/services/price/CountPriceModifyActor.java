package com.zanshang.services.price;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.zanshang.config.spring.CacheConfig;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.Price;
import com.zanshang.framework.PriceUnit;
import com.zanshang.models.global.CountPrice;
import com.zanshang.utils.AkkaTrapdoor;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


/**
 * Created by xuming on 15/8/11.
 */
public class CountPriceModifyActor extends BaseUntypedActor {

    private String COUNT_PRICE_CACHE_KEY = CountPrice.GLOBAL_KEY;

    ActorRef countPriceGetActor;

    public CountPriceModifyActor(ApplicationContext spring) {
        super(spring);
        countPriceGetActor = AkkaTrapdoor.create(getContext(), CountPriceGetActor.class, spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        try {
            Price price = (Price) o;
            Future<Object> future = Patterns.ask(countPriceGetActor, 1, ActorConstant.DEFAULT_TIMEOUT);
            CountPrice countPrice = (CountPrice) Await.result(future, Duration.Inf());
            countPrice.setValue(countPrice.getValue() + price.to(PriceUnit.CENT));
            getMongoTemplate().save(countPrice);
            final Cache cache = getCacheManager().getCache(CacheConfig.COUNT_PRICE);
            cache.put(COUNT_PRICE_CACHE_KEY, countPrice.getValueStr());
        }catch (Exception e) {
            logger.error("Ops", e);
        }
    }
}
