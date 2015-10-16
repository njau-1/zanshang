package com.zanshang.models.index;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Message;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 由MessageCreateActor创建，这个是消息索引，比方说在某人个人主页上查询对方和我的对话时使用，需要双键索引
 * Created by Lookis on 6/22/15.
 */
@Document(collection = "message_indexby_compositeid")
public class MessageIndexByCompositeId extends BaseArrayIndex<String, Message>{

    public MessageIndexByCompositeId(String key) {
        super(key);
    }
}
