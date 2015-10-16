package com.zanshang.controllers.wit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.PaymentType;
import com.zanshang.framework.PriceUnit;
import com.zanshang.models.wit.WitOrder;
import com.zanshang.models.wit.WitProject;
import com.zanshang.models.wit.WitReward;
import com.zanshang.services.PaymentTrapdoor;
import com.zanshang.services.payment.PaymentTrapdoorImpl;
import com.zanshang.services.wit.WitOrderFindByProjectIdAllActor;
import com.zanshang.services.wit.WitOrderSaveActor;
import com.zanshang.services.wit.WitPaymentCallbackService;
import com.zanshang.utils.AkkaTrapdoor;
import com.zanshang.utils.TicketNumberUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import scala.concurrent.Await;
import static akka.pattern.Patterns.ask;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuming on 15/9/17.
 */
@Controller
@RequestMapping(value = "/wit")
public class WITController implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    MongoTemplate mongoTemplate;

    ActorRef witOrderFindByProjectId;

    ActorRef witOrderSaveActor;

    PaymentTrapdoor paymentTrapdoor;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    protected void construct() {
        witOrderFindByProjectId = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(WitOrderFindByProjectIdAllActor.class, applicationContext)));
        witOrderSaveActor = actorSystem.actorOf(Props.create(AkkaTrapdoor.creator(WitOrderSaveActor.class, applicationContext)));
        paymentTrapdoor = akkaTrapdoor.createTrapdoor(PaymentTrapdoor.class, PaymentTrapdoorImpl.class);
    }

    @RequestMapping(value = "/application", method = RequestMethod.GET)
    public Object getApplyForm(@RequestParam(value = "projectId") String projectId) {
        ModelAndView mav = new ModelAndView("/wit/application");
        WitProject witProject = mongoTemplate.findById(projectId, WitProject.class);
        List<ObjectId> rewards = witProject.getRewards();
        List<WitReward> witRewards =  mongoTemplate.find(Query.query(Criteria.where("_id").in(rewards)), WitReward.class);
        try {
            List<WitOrder> witOrders = (List<WitOrder>) Await.result(ask(witOrderFindByProjectId, new ObjectId(projectId), ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
            HashMap<ObjectId, Integer> rewardsNum = new HashMap<ObjectId, Integer>();
            rewards.forEach(r -> {
                rewardsNum.put(r, 0);
            });
            witOrders.forEach(o -> {
                rewardsNum.put(o.getRewardId(), (Integer) rewardsNum.get(o.getRewardId()) + o.getCount());
            });
            List<RewardView> views = new ArrayList<>();
            witRewards.forEach(r -> {
//                rewardsNum.put(r.getId(), r.getCount() - (Integer) rewardsNum.get(r.getId()) - TicketNumberUtils.getLockedTicketNum(cacheManager, r.getId().toHexString()));
                views.add(new RewardView(r.getId(), r.getPrice().to(PriceUnit.YUAN), r.getCount(), r.getCount() - (Integer) rewardsNum.get(r.getId()) - TicketNumberUtils.getLockedTicketNum(cacheManager, r.getId().toHexString())));
            });
            mav.addObject("rewards", views);
        } catch (Exception e) {
            logger.error("Backend Service " + getClass().getSimpleName() +
                    " findByWitProjectId error.", e);
            throw new RuntimeException(e);
        }
        return mav;
    }

    @RequestMapping(value = "/application", method = RequestMethod.PUT)
    public Object createOrder(@RequestParam(value = "projectId") String projectId,
                              @RequestParam(value = "username") String username,
                              @RequestParam(value = "phone") String phone,
                              @RequestParam(value = "email") String email,
                              @RequestParam(value = "job") String job,
                              @RequestParam(value = "rewardId") String rewardId,
                              Device device) {
        ModelAndView mav = new ModelAndView("/wit/application");
        //is have ticket
        WitReward witReward = mongoTemplate.findById(new ObjectId(rewardId), WitReward.class);
        if (witReward != null) {
            try {
                List<WitOrder> witOrders = (List<WitOrder>) Await.result(ask(witOrderFindByProjectId, new ObjectId(projectId), ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION);
                if (witReward.getCount() - witOrders.size() - TicketNumberUtils.getLockedTicketNum(cacheManager, new ObjectId(rewardId).toHexString()) > 0) {
                    //save ticketId & locked this ticket
                    WitOrder witOrder = new WitOrder(new ObjectId(rewardId), username, phone, email, job);
                    witOrderSaveActor.tell(witOrder, ActorRef.noSender());
                    Map<String, String> arguments = new HashMap<>();
                    arguments.put(WitPaymentCallbackService.ORDERID, witOrder.getId().toHexString());
                    return paymentTrapdoor.createPayment(witReward.getRewardName(), witReward.getPrice(), PaymentType.Wit, arguments, true);
                } else {


                    //余票不足


                }
            } catch (Exception e) {
                logger.error("Backend Service " + getClass().getSimpleName() +
                        " createWitOrder error.", e);
                throw new RuntimeException(e);
            }
        }
        return mav;
    }
    //支付完成后的同步通知页面，用于支付宝网页支付后跳转和异步通知后页面自动跳转
    @RequestMapping(value = "/orders/{orderId}/status")
    public Object complete(@PathVariable("orderId") String witOrderId) {
        //查询alipay和wechat外网状态并返回
        //TODO: 等alipay single query接口审核通过之后再加，现在统一返回ok
        WitOrder witOrder = mongoTemplate.findById(new ObjectId(witOrderId), WitOrder.class);
        if (witOrder.isPaid()) {
            ModelAndView mav = new ModelAndView("/wit/applicationQuestion");
            mav.addObject("witOrderId", witOrderId);
            return mav;
        } else {
            WitReward witReward = mongoTemplate.findById(witOrder.getRewardId(), WitReward.class);
            Map<String, String> arguments = new HashMap<>();
            arguments.put(WitPaymentCallbackService.ORDERID, witOrder.getId().toHexString());
            return paymentTrapdoor.createPayment(witReward.getRewardName(), witReward.getPrice(), PaymentType.Wit, arguments, true);
        }
    }

    @RequestMapping(value = "/orders/{orderId}/qustion", method = RequestMethod.POST)
    public Object done(@PathVariable("orderId") String witOrderId, @RequestParam("question") String question) {
        WitOrder witOrder = mongoTemplate.findById(new ObjectId(witOrderId), WitOrder.class);
        if (witOrder.isPaid()) {
            witOrder.setQuestion(question);
            mongoTemplate.save(witOrder);
            ModelAndView mav = new ModelAndView("/wit/applicationSuccess");
            return mav;
        } else {
            WitReward witReward = mongoTemplate.findById(witOrder.getRewardId(), WitReward.class);
            Map<String, String> arguments = new HashMap<>();
            arguments.put(WitPaymentCallbackService.ORDERID, witOrder.getId().toHexString());
            return paymentTrapdoor.createPayment(witReward.getRewardName(), witReward.getPrice(), PaymentType.Wit, arguments, true);
        }
    }


    @RequestMapping(value = "/orders/{orderId}/qustion", method = RequestMethod.GET)
    public Object share(@PathVariable("orderId") String witOrderId) {
        WitOrder witOrder = mongoTemplate.findById(new ObjectId(witOrderId), WitOrder.class);
        WitReward witReward = mongoTemplate.findById(witOrder.getRewardId(), WitReward.class);
        return "redirect:http://e.eqxiu.com/s/8r2WNOUR";
    }

    public class RewardView{
        private ObjectId rewardId;

        private long price;

        private int count;

        private int currentCount;

        public RewardView(ObjectId rewardId, long price, int count, int currentCount) {
            this.rewardId = rewardId;
            this.price = price;
            this.count = count;
            this.currentCount = currentCount;
        }

        public ObjectId getRewardId() {
            return rewardId;
        }

        public void setRewardId(ObjectId rewardId) {
            this.rewardId = rewardId;
        }

        public long getPrice() {
            return price;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getCurrentCount() {
            return currentCount;
        }

        public void setCurrentCount(int currentCount) {
            this.currentCount = currentCount;
        }
    }
}
