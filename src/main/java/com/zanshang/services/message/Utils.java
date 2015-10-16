package com.zanshang.services.message;

import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Lookis on 6/22/15.
 */
class Utils {

    public static String extractCompositeId(ObjectId sender, ObjectId recipient) {
        List<ObjectId> sortIds = Arrays.asList(new ObjectId[]{sender, recipient});
        Collections.sort(sortIds);
        StringBuilder sb = new StringBuilder();
        for (ObjectId sortId : sortIds) {
            sb.append(sortId.toHexString());
            sb.append(":");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
