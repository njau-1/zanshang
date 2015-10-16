package com.zanshang.services;

import com.zanshang.models.Setting;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Lookis on 6/4/15.
 */
public interface SettingTrapdoor {

    Setting get(ObjectId uid);

    Map<ObjectId, Setting> findByIds(Collection<ObjectId> ids);

    void save(Setting setting);

}
