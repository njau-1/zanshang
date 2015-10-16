package com.zanshang.services.setting;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Setting;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lookis on 6/4/15.
 */
public class SettingGetActor extends BaseUntypedActor {

    public SettingGetActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Collection<ObjectId> ids = (Collection<ObjectId>) o;
        Query query = Query.query(Criteria.where("_id").in(ids));
        List<Setting> settings = getMongoTemplate().find(query, Setting.class);
        Map<ObjectId, Setting> retMap = new HashMap<>();
        for (Setting setting : settings) {
            retMap.put(setting.getUid(), setting);
        }
        getSender().tell(retMap, getSelf());
    }
}
