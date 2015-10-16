package com.zanshang.services.message;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.framework.index.FindByIndexPageableActor;
import com.zanshang.models.Message;
import com.zanshang.models.index.MessageIndexByCompositeId;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/22/15.
 */
public class MessageFindByCompositeIdActor extends FindByIndexPageableActor<String, Message> {

    public MessageFindByCompositeIdActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public Class<? extends BaseArrayIndex<String, Message>> indexClz() {
        return MessageIndexByCompositeId.class;
    }

    @Override
    protected boolean reverse() {
        return true;
    }
}
