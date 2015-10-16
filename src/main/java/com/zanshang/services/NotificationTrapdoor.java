package com.zanshang.services;

import com.zanshang.models.Notification;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Lookis on 6/24/15.
 */
public interface NotificationTrapdoor {

    Page<Notification> findByUid(ObjectId uid, Pageable pageable);

    void save(Notification notification);

    void markRead(List<ObjectId> notificationIds);

    boolean hasNews(ObjectId uid);
}
