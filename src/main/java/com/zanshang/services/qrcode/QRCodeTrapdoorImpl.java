package com.zanshang.services.qrcode;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.framework.RepositoryTrapdoor;
import com.zanshang.models.QRCode;
import com.zanshang.services.QRCodeTrapdoor;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/4/15.
 */
public class QRCodeTrapdoorImpl extends RepositoryTrapdoor<String, QRCode> implements QRCodeTrapdoor{

    public QRCodeTrapdoorImpl(ApplicationContext springContext) {
        super(springContext);
    }

    @Override
    public Class<? extends BaseUntypedActor> getClz() {
        return QRCodeGetActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> saveClz() {
        return QRCodeSaveActor.class;
    }

    @Override
    public Class<? extends BaseUntypedActor> deleteClz() {
        return QRCodeDeleteActor.class;
    }
}
