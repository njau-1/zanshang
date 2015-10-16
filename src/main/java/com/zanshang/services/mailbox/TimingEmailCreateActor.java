package com.zanshang.services.mailbox;

import akka.actor.ActorRef;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.PriceUnit;
import com.zanshang.models.*;
import com.zanshang.notify.constants.FreeMarkerModelParamKey;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.services.notificationevent.NotificationEventSaveActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuming on 15/8/26.
 */
public class TimingEmailCreateActor extends BaseUntypedActor{

    ActorRef notificationEventSaveActor;
    String IMAGE_CONTEXT;

    public TimingEmailCreateActor(ApplicationContext spring) {
        super(spring);
        IMAGE_CONTEXT = getProperty("IMAGE_CONTEXT");

        notificationEventSaveActor = AkkaTrapdoor.create(getContext(), NotificationEventSaveActor.class, spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Project project = (Project) o;
        int totalDays = getTotalDays(project);
        if (totalDays <= 7) {
            halfProgress(project);
        }else if(totalDays <=14) {
            firstWeek(project);
            halfProgress(project);
        }else {
            firstWeek(project);
            halfProgress(project);
            lastWeek(project);
        }

    }
    public void firstWeek(Project project) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(FreeMarkerModelParamKey.BOOKNAME.getKey(), project.getBookName());
        paramMap.put(FreeMarkerModelParamKey.PROJECTID.getKey(), project.getId().toHexString());
        paramMap.put(FreeMarkerModelParamKey.DEADLINE.getKey(), new SimpleDateFormat("yyyy-MM-dd").format(project.getDeadline()));
        paramMap.put(FreeMarkerModelParamKey.CURRENTBALANCE.getKey(), String.valueOf(project.getCurrentBalance().to(PriceUnit.YUAN)));
        paramMap.put(FreeMarkerModelParamKey.GOAL.getKey(), String.valueOf(project.getGoal().to(PriceUnit.YUAN)));
        paramMap.put(FreeMarkerModelParamKey.COVER.getKey(), IMAGE_CONTEXT + project.getCover());
        Date date = DateUtils.addDays(project.getCreateTime(), +7);
        NotificationEvent notificationEvent = new NotificationEvent(project.getUid(), NotifyBusinessType.PROJECT_PROGRESS_FIRST_WEEK, paramMap, date);
        notificationEventSaveActor.tell(notificationEvent, getSelf());
    }

    public void halfProgress(Project project) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(FreeMarkerModelParamKey.BOOKNAME.getKey(), project.getBookName());
        paramMap.put(FreeMarkerModelParamKey.PROJECTID.getKey(), project.getId().toHexString());
        paramMap.put(FreeMarkerModelParamKey.DEADLINE.getKey(), new SimpleDateFormat("yyyy-MM-dd").format(project.getDeadline()));
        paramMap.put(FreeMarkerModelParamKey.CURRENTBALANCE.getKey(), String.valueOf(project.getCurrentBalance().to(PriceUnit.YUAN)));
        paramMap.put(FreeMarkerModelParamKey.GOAL.getKey(), String.valueOf(project.getGoal().to(PriceUnit.YUAN)));
        paramMap.put(FreeMarkerModelParamKey.COVER.getKey(), IMAGE_CONTEXT + project.getCover());
        Date date = DateUtils.addDays(project.getCreateTime(), ((Double) Math.floor(getTotalDays(project) / 2)).intValue());
        NotificationEvent notificationEvent = new NotificationEvent(project.getUid(), NotifyBusinessType.PROJECT_PROGRESS_HALF, paramMap, date);
        notificationEventSaveActor.tell(notificationEvent, getSelf());
    }

    public void lastWeek(Project project) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(FreeMarkerModelParamKey.BOOKNAME.getKey(), project.getBookName());
        paramMap.put(FreeMarkerModelParamKey.PROJECTID.getKey(), project.getId().toHexString());
        paramMap.put(FreeMarkerModelParamKey.DEADLINE.getKey(), new SimpleDateFormat("yyyy-MM-dd").format(project.getDeadline()));
        paramMap.put(FreeMarkerModelParamKey.CURRENTBALANCE.getKey(), String.valueOf(project.getCurrentBalance().to(PriceUnit.YUAN)));
        paramMap.put(FreeMarkerModelParamKey.GOAL.getKey(), String.valueOf(project.getGoal().to(PriceUnit.YUAN)));
        paramMap.put(FreeMarkerModelParamKey.COVER.getKey(), IMAGE_CONTEXT + project.getCover());
        //
        Date date = DateUtils.addDays(project.getDeadline(), -7);
        NotificationEvent notificationEvent = new NotificationEvent(project.getUid(), NotifyBusinessType.PROJECT_PROGRESS_LAST_WEEK, paramMap, date);
        notificationEventSaveActor.tell(notificationEvent, getSelf());
    }
    public int getTotalDays(Project project) {
        return (int) TimeUnit.DAYS.convert(project.getDeadline().getTime() - project.getCreateTime().getTime(), TimeUnit.MILLISECONDS);
    }
}
