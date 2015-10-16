package com.zanshang.services.salon;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindByIndexPageableActor;
import com.zanshang.models.Project;
import com.zanshang.models.global.SalonHome;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 7/3/15.
 */
public class SalonHomePageableActor extends FindByIndexPageableActor<String, Project> {

    public SalonHomePageableActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<String, Project>> indexClz() {
        return SalonHome.class;
    }

    @Override
    protected boolean reverse() {
        return true;
    }
}
