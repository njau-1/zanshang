package com.zanshang.services;

import com.zanshang.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Lookis on 6/14/15.
 */
public interface OrderTrapdoor {
    Order get(ObjectId orderId);

    boolean save(Order order);

    Page<Order> findPaidByUid(ObjectId uid, Pageable pageable);

    Page<Order> findByUid(ObjectId uid, Pageable pageable);

    List<Order> findByProjectId(ObjectId projectId);

}
