package com.zanshang.services.salon.chat;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.constants.ProjectState;
import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.IndexPageableParams;
import com.zanshang.models.*;
import com.zanshang.models.index.SalonChatIndexBySalonId;
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
public class SalonChatCreateActor extends AddToIndexActor<ObjectId, SalonChat> {

    ActorRef settingActor;

    ActorRef findByUid;

    ActorRef salonGetActor;

    Notifier notifier;

    public SalonChatCreateActor(ApplicationContext spring) {
        super(spring);
        notifier = spring.getBean(Notifier.class);

        settingActor = AkkaTrapdoor.create(getContext(), SettingGetActor.class, spring);
        findByUid = AkkaTrapdoor.create(getContext(), ProjectFindByUidPageableActor.class, spring);
        salonGetActor = AkkaTrapdoor.create(getContext(), SalonGetActor.class, spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        super.onReceive(o);
        getMongoTemplate().save(extractValue(o));
        /////////////////////////////////
        // SALON_NEW_CHAT Notification //
        /////////////////////////////////
        SalonChat salonChat = (SalonChat) o;
        //notification type
        Setting setting = ((Map<ObjectId, Setting>) Await.result(ask(settingActor, Collections.singleton(salonChat.getUid()), ActorConstant.DEFAULT_TIMEOUT), ActorConstant
                .DEFAULT_TIMEOUT_DURATION)).get(salonChat.getUid());
        //project Bookname
        ArrayList<Project> projects = new ArrayList<>(((Page<Project>) Await.result(Patterns.ask(findByUid, new IndexPageableParams(new PageRequest(0, 50), salonChat.getSalonId()),
                ActorConstant.DEFAULT_TIMEOUT), ActorConstant.DEFAULT_TIMEOUT_DURATION)).getContent());
        String bookName = "";
        for (Project project: projects) {
            if (project.getState() != ProjectState.REVIEWING && project.getState() != ProjectState.PRICING) {
                bookName = project.getBookName();
                break;
            }
        }
        //author || member
        if (salonChat.getSalonId().equals(salonChat.getUid())) {
            Map<String, String> model = new HashMap<String, String>();
            model.put(FreeMarkerModelParamKey.BOOKNAME.getKey(), bookName.isEmpty()?setting.getDisplayName():bookName);
            model.put(FreeMarkerModelParamKey.UID.getKey(), salonChat.getSalonId().toHexString());
            model.put(FreeMarkerModelParamKey.AUTHOR.getKey(), setting.getDisplayName());

            //orders
            List<ObjectId> memberList = ((Salon) Await.result(ask(salonGetActor, salonChat.getSalonId(), ActorConstant.DEFAULT_TIMEOUT),
                    ActorConstant.DEFAULT_TIMEOUT_DURATION)).getMembers();
            for (ObjectId uid : memberList) {
                if (!uid.equals(salonChat.getUid())) {
                    notifier.notify(uid, NotifyBusinessType.SALON_NEW_CHAT_AUTHOR, model);
                }
            }
        } else {
            Map<String, String> model = new HashMap<String, String>();
            model.put(FreeMarkerModelParamKey.BOOKNAME.getKey(), bookName.isEmpty()?setting.getDisplayName():bookName);
            model.put(FreeMarkerModelParamKey.UID.getKey(), salonChat.getSalonId().toHexString());
            model.put(FreeMarkerModelParamKey.SENDERNAME.getKey(), setting.getDisplayName());
            notifier.notify(salonChat.getSalonId(), NotifyBusinessType.SALON_NEW_CHAT_MEMBER, model);
        }
    }

    @Override
    public SalonChat extractValue(Object o) {
        return (SalonChat) o;
    }

    @Override
    public ObjectId extractKey(Object o) {
        SalonChat chat = (SalonChat) o;
        return chat.getSalonId();
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, SalonChat>> indexClz() {
        return SalonChatIndexBySalonId.class;
    }
}
