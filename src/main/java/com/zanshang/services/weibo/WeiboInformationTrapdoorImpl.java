package com.zanshang.services.weibo;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.WeiboInformation;
import com.zanshang.framework.RepositoryTrapdoor;
import com.zanshang.services.WeiboInformationTrapdoor;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/7/15.
 */
public class WeiboInformationTrapdoorImpl extends RepositoryTrapdoor<String, WeiboInformation> implements
        WeiboInformationTrapdoor {

    public WeiboInformationTrapdoorImpl(ApplicationContext springContext) {
        super(springContext);
    }

    @Override
    public Class<? extends BaseUntypedActor> getClz() {
        return WeiboInformationGetActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> saveClz() {
        return null;
    }

    @Override
    public Class<? extends BaseUntypedActor> deleteClz() {
        return null;
    }
}
