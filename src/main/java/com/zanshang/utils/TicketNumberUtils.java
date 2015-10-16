package com.zanshang.utils;

import com.zanshang.config.spring.CacheConfig;
import org.bson.types.ObjectId;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuming on 15/9/17.
 */
public class TicketNumberUtils {

    /**
     * 获得当前锁定的票数
     * @param cacheManager
     * @param witRewardid
     * @return
     */
    public static int getLockedTicketNum(CacheManager cacheManager, String witRewardid) {
        Cache ticketindex = cacheManager.getCache(CacheConfig.CACHE_NAME_WIT_TICKET_LOCKED_INDEX);
        Cache ticketCache = cacheManager.getCache(CacheConfig.CACHE_NAME_WIT_TICKET_LOCKED);
        List<String> newTickets = new ArrayList<>();
        String json = ticketindex.get(witRewardid, String.class);
        if (json != null && !json.isEmpty()) {
            List<String> tickets = Json.fromJson(json, ArrayList.class);
            for (String t: tickets) {
                if (ticketCache.get(t) != null) {
                    newTickets.add(t);
                }
            }
            ticketindex.put(witRewardid, Json.toJson(newTickets));
        }
        return newTickets.size();
    }

    public static boolean locketTicket(CacheManager cacheManager, String witRewardid, String witOrderId) {
        Cache ticketindex = cacheManager.getCache(CacheConfig.CACHE_NAME_WIT_TICKET_LOCKED_INDEX);
        Cache ticketCache = cacheManager.getCache(CacheConfig.CACHE_NAME_WIT_TICKET_LOCKED);
        ticketCache.put(witOrderId, witOrderId);
        String json = ticketindex.get(witRewardid, String.class);
        List<String> tickets;
        if (json != null && !json.isEmpty()) {
            tickets = Json.fromJson(json, ArrayList.class);
        } else {
            tickets = new ArrayList<>();
        }
        tickets.add(witOrderId);
        ticketindex.put(witRewardid, Json.toJson(tickets));
        return true;
    }

    public static boolean unLocketTicket(CacheManager cacheManager, String witRewardid, String witOrderId) {
        Cache ticketCache = cacheManager.getCache(CacheConfig.CACHE_NAME_WIT_TICKET_LOCKED);
        ticketCache.evict(witOrderId);
        getLockedTicketNum(cacheManager, witRewardid);
        return true;
    }
}
