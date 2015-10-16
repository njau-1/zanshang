package com.zanshang.framework.index;

import com.zanshang.framework.BaseUntypedActor;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;

/**
 * Created by Lookis on 7/3/15.
 */
public abstract class FindAllByIndexActor<K, V> extends BaseUntypedActor{

    public FindAllByIndexActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        K key = (K) o;
        BaseArrayIndex<K, V> arrayIndex = getMongoTemplate().findById(key, indexClz());
        if(arrayIndex == null){
            getSender().tell(new ArrayList<>(), getSelf());
        }else{
            getSender().tell(arrayIndex.getValue(), getSelf());
        }
    }

    public abstract Class<? extends BaseArrayIndex<K, V>> indexClz();
}
