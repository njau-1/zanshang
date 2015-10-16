package com.zanshang.services.price;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.global.CountPrice;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;

/**
 * Created by xuming on 15/8/11.
 */
public class CountPriceGetActor extends BaseUntypedActor{

    private String COUNT_PRICE_CACHE_KEY = CountPrice.GLOBAL_KEY;

    public CountPriceGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        final Cache cache = getCacheManager().getCache(CacheConfig.COUNT_PRICE);
        String countPriceStr = cache.get(COUNT_PRICE_CACHE_KEY, String.class);
        CountPrice countPrice;
        if (countPriceStr != null && !countPriceStr.isEmpty()) {
            countPrice = new CountPrice(Long.parseLong(countPriceStr));
        }else {
            countPrice = getMongoTemplate().findById(COUNT_PRICE_CACHE_KEY, CountPrice.class);
            cache.put(COUNT_PRICE_CACHE_KEY, countPrice.getValueStr());
        }
        getSender().tell(countPrice, getSelf());
    }
}