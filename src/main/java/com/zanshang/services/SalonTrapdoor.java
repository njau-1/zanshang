package com.zanshang.services;

import com.zanshang.models.Project;
import com.zanshang.models.Salon;
import com.zanshang.models.SalonChat;
import com.zanshang.models.SalonTopic;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Lookis on 6/25/15.
 */
public interface SalonTrapdoor {

    Page<Project> globalSalon(Pageable pageable);

    Page<Salon> findByMemberId(ObjectId uid, Pageable pageable);

    boolean isMember(ObjectId salonId, ObjectId uid);

    boolean createTopic(SalonTopic topic);

    void chat(SalonChat salonChat);

    Salon get(ObjectId salonId);

    Page<SalonTopic> getTopic(ObjectId salonId, Pageable pageable);

    Page<SalonChat> getChat(ObjectId salonId, Pageable pageable);

    Map<ObjectId, Salon> findByIds(Collection<ObjectId> salonIds);

}
