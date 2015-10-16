package com.zanshang.services.order;

import akka.actor.ActorRef;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.Price;
import com.zanshang.framework.PriceUtils;
import com.zanshang.models.*;
import com.zanshang.notify.Notifier;
import com.zanshang.notify.constants.FreeMarkerModelParamKey;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.services.price.CountPriceModifyActor;
import com.zanshang.services.project.ProjectGetActor;
import com.zanshang.services.project.ProjectSaveActor;
import com.zanshang.services.salon.SalonGetActor;
import com.zanshang.services.salon.member.SalonAddMemberActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Future;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lookis on 6/14/15.
 */
public class OrderSaveActor extends BaseUntypedActor {

    ActorRef salonMemberActor;

    ActorRef salonGetActor;

    ActorRef indexByUidSave;

    ActorRef indexByProjectIdSave;

    ActorRef projectGetActor;

    ActorRef projectSaveActor;

    ActorRef countPriceModifyActor;

    ActorRef orderFindByProjectIdAllActor;

    Notifier notifier;

    String IMAGE_CONTEXT;

    public OrderSaveActor(ApplicationContext spring) {
        super(spring);
        notifier = spring.getBean(Notifier.class);
        IMAGE_CONTEXT = getProperty("IMAGE_CONTEXT");

        salonMemberActor = AkkaTrapdoor.create(getContext(), SalonAddMemberActor.class, spring);
        projectGetActor = AkkaTrapdoor.create(getContext(), ProjectGetActor.class, spring);
        projectSaveActor = AkkaTrapdoor.create(getContext(), ProjectSaveActor.class, spring);
        salonGetActor = AkkaTrapdoor.create(getContext(), SalonGetActor.class, spring);
        indexByUidSave = AkkaTrapdoor.create(getContext(), OrderIndexByUidSaveActor.class, spring);
        indexByProjectIdSave = AkkaTrapdoor.create(getContext(), OrderIndexByProjectIdSaveActor.class, spring);
        countPriceModifyActor = AkkaTrapdoor.create(getContext(), CountPriceModifyActor.class, spring);
        orderFindByProjectIdAllActor = AkkaTrapdoor.create(getContext(), OrderFindByProjectIdAllActor.class, spring);
    }

    @Override
    //FixMe: 这里没有判断库存的情况，如果以后业务有需要需要新增加库存判断，这里只是简单减少库存
    public void onReceive(Object o) throws Exception {
        Order order = (Order) o;
        //如果order是改变paid的状态，减少相应库存、做相关通知、如果Reward里包含VIP，需要加入沙龙列表
        Reward reward = getMongoTemplate().findById(order.getRewardId(), Reward.class);
        //TODO: 相关通知
        Order originOrder = getMongoTemplate().findById(order.getId(), Order.class);
        if (originOrder != null && !originOrder.isPaid() && order.isPaid()) {
            if (reward.getCount() != Reward.UNCOUNTABLE) {
                reward.setCount(reward.getCount() - 1);
                getMongoTemplate().save(reward);
            }
            if (order.getSharerId() != null) {
                getMongoTemplate().save(new ShareReward(order.getUid(), order.getSharerId(), order.getId()));
            }
            Price cost = new Price(reward.getPrice().getPrice() * order.getCount(), reward.getPrice().getUnit());
            Future<Object> projectFuture = Patterns.ask(projectGetActor, reward.getProjectId(), ActorConstant
                    .DEFAULT_TIMEOUT);
            projectFuture.onSuccess(new OnSuccess<Object>() {
                @Override
                public void onSuccess(Object o) throws Throwable {
                    Project project = (Project) o;
                    if (reward.getItems().keySet().contains(Reward.Item.VIP.toString())) {
                        Future<Object> salonFuture = Patterns.ask(salonGetActor, project.getUid(), ActorConstant
                                .DEFAULT_TIMEOUT);
                        salonFuture.onSuccess(new OnSuccess<Object>() {
                            @Override
                            public void onSuccess(Object o) throws Throwable {
                                salonMemberActor.tell(new SalonAddMemberActor.Params(order.getUid(), (Salon) o),
                                        getSelf());
                            }
                        }, getContext().dispatcher());
                    }
                    //send notify
                    if (PriceUtils.lt(project.getCurrentBalance(), project.getGoal())) {
                        if (PriceUtils.gte(PriceUtils.add(project.getCurrentBalance(), cost), project.getGoal())) {
                            //sort by rewards
                            Map<String, String> model = new HashMap<String, String>();
                            model.put(FreeMarkerModelParamKey.BOOKNAME.getKey(), project.getBookName());
                            model.put(FreeMarkerModelParamKey.PROJECTID.getKey(), project.getId().toHexString());
                            model.put(FreeMarkerModelParamKey.DEADLINE.getKey(), new SimpleDateFormat("yyyy-MM-dd").format(project.getDeadline()));
                            model.put(FreeMarkerModelParamKey.COVER.getKey(), IMAGE_CONTEXT + project.getCover());
                            notifier.notify(project.getUid(), NotifyBusinessType.PROJECT_FUNDING_SUCCESS, model);
                        }
                    }
                    project.setCurrentBalance(PriceUtils.add(project.getCurrentBalance(), cost));
                    projectSaveActor.tell(project, getSelf());
                }
            }, getContext().dispatcher());
            //modify count price
            countPriceModifyActor.tell(cost, getSelf());
            //save order index by uid
            indexByUidSave.tell(order, getSelf());
            //save order index by project;
            indexByProjectIdSave.tell(order, getSelf());
        }
        //save order
        getMongoTemplate().save(o);
        //notify create success
        getSender().tell(true, getSelf());
    }
}
