package com.zanshang.services.order;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.index.IndexPageableParams;
import com.zanshang.models.Order;
import com.zanshang.models.index.OrderIndexByUid;
import com.zanshang.utils.PageUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lookis on 7/14/15.
 */
public class OrderFindPaidByUidActor extends BaseUntypedActor {

    public OrderFindPaidByUidActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        IndexPageableParams params = (IndexPageableParams) o;
        OrderIndexByUid arrayIndex = getMongoTemplate().findById(params.getKey(), OrderIndexByUid.class);
        if (arrayIndex == null) {
            getSender().tell(new PageImpl<>(new ArrayList<>(), params.getPageable(), 0), getSelf());
        } else {
            List<Order> paidOrder = new ArrayList<>();
            for (Order order : arrayIndex.getValue()) {
                if (order.isPaid()) {
                    paidOrder.add(order);
                }
            }
            Page<Order> paging = PageUtils.paging(params.getPageable(), paidOrder);
            getSender().tell(paging, getSelf());
        }
    }
}
