package com.zanshang.services.address;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Address;
import com.zanshang.models.Setting;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/24/15.
 */
public class AddressDeleteActor extends BaseUntypedActor {

    public AddressDeleteActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        String addressId = o.toString();
        Address address = getMongoTemplate().findById(addressId, Address.class);
        Setting setting = getMongoTemplate().findById(address.getUid(), Setting.class);
        setting.getAddresses().remove(address.getId());
        getMongoTemplate().save(setting);
        getMongoTemplate().remove(address);
    }
}
