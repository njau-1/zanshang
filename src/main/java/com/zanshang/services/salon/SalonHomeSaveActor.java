package com.zanshang.services.salon;

import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Project;
import com.zanshang.models.global.SalonHome;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 7/10/15.
 */
public class SalonHomeSaveActor extends AddToIndexActor<String, Project> {

    public SalonHomeSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Project extractValue(Object o) {
        return (Project) o;
    }

    @Override
    public String extractKey(Object o) {
        return SalonHome.GLOBAL_KEY;
    }

    @Override
    public Class<? extends BaseArrayIndex<String, Project>> indexClz() {
        return SalonHome.class;
    }
}
