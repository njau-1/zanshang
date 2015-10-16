package com.zanshang.services.wit;

import akka.actor.ActorRef;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.wit.WitOrder;
import com.zanshang.models.wit.WitReward;
import com.zanshang.notify.Notifier;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.TicketNumberUtils;
import org.bson.types.ObjectId;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuming on 15/9/17.
 */
public class WitOrderSaveActor extends BaseUntypedActor {

    public final static String YEZIUID = "55fc0413e4b09bc29853012b";

    Notifier notifier;

    ActorRef orderIndexByProjectId;

    public WitOrderSaveActor(ApplicationContext spring) {
        super(spring);
        notifier = spring.getBean(Notifier.class);

        orderIndexByProjectId = AkkaTrapdoor.create(getContext(), WitOrderIndexByProjectIdSaveActor.class, spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        WitOrder witOrder = (WitOrder) o;
        //判断订单状态
        WitOrder originWitOrder = getMongoTemplate().findById(witOrder.getId(), WitOrder.class);
        WitReward witReward = getMongoTemplate().findById(witOrder.getRewardId(), WitReward.class);
        //2.支付成功
        if (originWitOrder != null && !originWitOrder.isPaid() && witOrder.isPaid()) {
            //2.2更新orderIndexByProjectId
            orderIndexByProjectId.tell(witOrder, ActorRef.noSender());
            //2.3解除锁定,减少一张门票
            TicketNumberUtils.unLocketTicket(getCacheManager(), witReward.getId().toHexString(), witOrder.getId().toHexString());
            //2.4send Notification to yezi
            //notifier.notify(yezi.uid);
            Map<String, String> model = new HashMap<>();
            model.put("username", witOrder.getUsername());
            model.put("phone", witOrder.getPhone());
            model.put("email", witOrder.getEmail());
            model.put("job", witOrder.getJob());
            model.put("rewardName", witReward.getRewardName());
            model.put("date", new SimpleDateFormat("yyyy-MM-dd").format(witOrder.getCreateTime()));
            notifier.notify(new ObjectId(YEZIUID), NotifyBusinessType.WIT, model);
        } else {
            //1.2锁定门票
            TicketNumberUtils.locketTicket(getCacheManager(), witReward.getId().toHexString(), witOrder.getId().toHexString());
        }
        //1.新建订单
        //1.1入库
        //2.1更新
        getMongoTemplate().save(witOrder);
    }
}
