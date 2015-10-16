package com.zanshang.services.address;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Address;
import com.zanshang.models.Setting;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Lookis on 6/24/15.
 */
public class AddressListActor extends BaseUntypedActor {

    public AddressListActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        String uid = o.toString();
        Setting setting = getMongoTemplate().findById(uid, Setting.class);
        if (setting == null) {
            getSender().tell(new ArrayList<>(), getSelf());
        } else {
            List<ObjectId> addressIds = setting.getAddresses();
            List<Address> addressList = getMongoTemplate().find(Query.query(Criteria.where("_id").in(addressIds)),
                    Address.class);
            if (CollectionUtils.isEmpty(addressList)) {
                getSender().tell(Collections.EMPTY_LIST, getSelf());
            } else {
                ObjectId defaultId = addressIds.get(0);
                Address defaultAddress = null;
                for (Address address : addressList) {
                    if (address.getId().equals(defaultId)) {
                        defaultAddress = address;
                        break;
                    }
                }
                addressList.remove(defaultAddress);
                addressList.add(0, defaultAddress);
                getSender().tell(addressList, getSelf());
            }
        }
    }
}
