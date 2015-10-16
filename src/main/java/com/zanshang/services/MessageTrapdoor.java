package com.zanshang.services;

import com.zanshang.models.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Lookis on 6/22/15.
 */
public interface MessageTrapdoor {

    void save(Message toSave);

    Page<Message> findBySenderAndRecipient(ObjectId sender, ObjectId recipient, Pageable pageable);
}
