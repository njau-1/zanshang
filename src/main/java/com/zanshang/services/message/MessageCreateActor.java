package com.zanshang.services.message;

import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Message;
import com.zanshang.models.index.MessageIndexByCompositeId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/22/15.
 */
public class MessageCreateActor extends AddToIndexActor<String, Message> {

    public MessageCreateActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        super.onReceive(o);
        getMongoTemplate().save(o);
    }

    @Override
    public Message extractValue(Object o) {
        Message message = (Message) o;
        return message;
    }

    @Override
    public String extractKey(Object o) {
        Message message = (Message) o;
        return Utils.extractCompositeId(message.getSender(), message.getRecipient());
    }

    @Override
    public Class<? extends BaseArrayIndex<String, Message>> indexClz() {
        return MessageIndexByCompositeId.class;
    }
}
