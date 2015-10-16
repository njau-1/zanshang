package com.zanshang.services.wechat;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.WechatInformation;
import com.zanshang.framework.RepositoryTrapdoor;
import com.zanshang.services.WechatInformationTrapdoor;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/7/15.
 */
public class WechatInformationTrapdoorImpl extends RepositoryTrapdoor<String, WechatInformation> implements
        WechatInformationTrapdoor {

    public WechatInformationTrapdoorImpl(ApplicationContext springContext) {
        super(springContext);
    }

    @Override
    public Class<? extends BaseUntypedActor> getClz() {
        return WechatInformationGetActor.class;
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
