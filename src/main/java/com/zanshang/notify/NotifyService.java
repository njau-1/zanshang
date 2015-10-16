package com.zanshang.notify;

import com.zanshang.notify.constants.NotifyBusinessType;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * Created by xuming on 15/9/7.
 */
public interface NotifyService {

    void send(ObjectId uid, NotifyBusinessType notifyBusinessType, Map<String, String> model);

    MessageTemplateEngine getTemplate();
}
