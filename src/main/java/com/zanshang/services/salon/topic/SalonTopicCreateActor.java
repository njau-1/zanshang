package com.zanshang.services.salon.topic;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.constants.ProjectState;
import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.IndexPageableParams;
import com.zanshang.models.*;
import com.zanshang.models.index.SalonTopicIndexBySalonId;
import com.zanshang.notify.Notifier;
import com.zanshang.notify.constants.FreeMarkerModelParamKey;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.services.project.ProjectFindByUidPageableActor;
import com.zanshang.services.salon.SalonGetActor;
import com.zanshang.services.setting.SettingGetActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import scala.concurrent.Await;

import java.util.*;

import static akka.pattern.Patterns.ask;

/**
 * Created by Lookis on 7/3/15.
 */
public class SalonTopicCreateActor extends AddToIndexActor<ObjectId, SalonTopic> {

    ActorRef settingActor;

    ActorRef findByUid;

    ActorRef salonGetActor;

    Notifier notifier;
    String IMAGE_CONTEXT;
    public SalonTopicCreateActor(ApplicationContext spring) {
        super(spring);
        notifier = spring.getBean(Notifier.class);
        IMAGE_CONTEXT = getProperty("IMAGE_CONTEXT");

        settingActor = AkkaTrapdoor.create(getContext(), SettingGetActor.class, spring);
        findByUid = AkkaTrapdoor.create(getContext(), ProjectFindByUidPageableActor.class, spring);
        salonGetActor = AkkaTrapdoor.create(getContext(), SalonGetActor.class, spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        super.onReceive(o);
        getMongoTemplate().save(extractValue(o));
        getSender().tell(true, getSelf());
        /////////////////////////////////
        // SALON_NEW_TOPIC Notification //
        /////////////////////////////////
        SalonTopic salonTopic = (SalonTopic) o;
        //notification type
        Setting setting = ((Map<ObjectId, Setting>) Await.result(ask(settingActor, Collections.singleton(salonTopic.getSalonId()), ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                .DEFAULT_TIMEOUT_DURATION)).get(salonTopic.getSalonId());
        //project Bookname
        ArrayList<Project> projects = new ArrayList<>(((Page<Project>) Await.result(Patterns.ask(findByUid, new IndexPageableParams(new PageRequest(0, 50), salonTopic.getSalonId()),
                ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION)).getContent());
        String bookName = "";
        String cover = "";
        for (Project project: projects) {
            if (project.getState() != ProjectState.REVIEWING && project.getState() != ProjectState.PRICING) {
                bookName = project.getBookName();
                cover = project.getCover();
                break;
            }
        }
        //author || member
        Map<String, String> model = new HashMap<String, String>();
        model.put(FreeMarkerModelParamKey.BOOKNAME.getKey(), bookName.isEmpty()?setting.getDisplayName():bookName);
        model.put(FreeMarkerModelParamKey.UID.getKey(), salonTopic.getSalonId().toHexString());
        model.put(FreeMarkerModelParamKey.AUTHOR.getKey(), setting.getDisplayName());
        model.put(FreeMarkerModelParamKey.COVER.getKey(), IMAGE_CONTEXT + cover);

        //orders
        List<ObjectId> memberList = ((Salon) Await.result(ask(salonGetActor, salonTopic.getSalonId(), ActorConstant.DEFAULT_TIMEOUT),
                ActorConstant.DEFAULT_TIMEOUT_DURATION)).getMembers();
        for (ObjectId uid : memberList) {
            if (!uid.equals(salonTopic.getSalonId())) {
                notifier.notify(uid, NotifyBusinessType.SALON_NEW_TOPIC, model);
            }
        }
    }

    @Override
    public SalonTopic extractValue(Object o) {
        return (SalonTopic) o;
    }

    @Override
    public ObjectId extractKey(Object o) {
        SalonTopic topic = (SalonTopic) o;
        return topic.getSalonId();
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, SalonTopic>> indexClz() {
        return SalonTopicIndexBySalonId.class;
    }
}
