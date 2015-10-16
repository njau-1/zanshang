package com.zanshang.services.address;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Address;
import com.zanshang.models.Setting;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by Lookis on 6/24/15.
 */
public class AddressSaveActor extends BaseUntypedActor {

    public AddressSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Object[] params = (Object[]) o;
        Address address = (Address) params[0];
        boolean isDefault = Boolean.TRUE.equals(params[1]);
        Setting setting = getMongoTemplate().findById(address.getUid(), Setting.class);
        Assert.notNull(setting);
        List<ObjectId> addresses = setting.getAddresses();
        if (isDefault) {
            addresses.remove(address.getId());
            addresses.add(0, address.getId());
        } else if(!addresses.contains(address.getId())) {
            addresses.add(addresses.size(), address.getId());
        }
        getMongoTemplate().save(setting);
        getMongoTemplate().save(address);
    }
}
