package com.zanshang.framework.index;

import com.mongodb.DBRef;
import com.zanshang.framework.BaseUntypedActor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Lookis on 7/3/15.
 */
public abstract class AddToIndexActor<K, V> extends BaseUntypedActor {

    public AddToIndexActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        V value = extractValue(o);
        K key = extractKey(o);
        BaseArrayIndex<K, V> arrayIndex = getMongoTemplate().findById(key, indexClz());
        if (arrayIndex == null) {
            Constructor<? extends BaseArrayIndex<K, V>> constructor;
            try {
                constructor = indexClz().getConstructor(key.getClass());
                arrayIndex = constructor.newInstance(key);
            } catch (NoSuchMethodException e) {
                constructor = indexClz().getConstructor();
                arrayIndex = constructor.newInstance();
            }
            getMongoTemplate().save(arrayIndex);
        }
        List<V> array = arrayIndex.getValue();
        if (!array.contains(value)) {
            array.add(value);
            Update update = new Update();
            Document annotation = AnnotationUtils.findAnnotation(value.getClass(), Document.class);
            String collection = annotation.collection();
            Object id = null;
            Field[] declaredFields = value.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Id idAnnotation = declaredField.getAnnotation(Id.class);
                if (idAnnotation != null) {
                    declaredField.setAccessible(true);
                    id = declaredField.get(value);
                    break;
                }
            }
            logger.debug("Id for " + value + " to save, is:" + id);
            DBRef ref = new DBRef(collection, id);
            update.addToSet(BaseArrayIndex.VALUE_FIELD, ref);
            getMongoTemplate().findAndModify(Query.query(Criteria.where("_id").is(key)), update, indexClz());
        }
    }

    public abstract V extractValue(Object o);

    public abstract K extractKey(Object o);

    public abstract Class<? extends BaseArrayIndex<K, V>> indexClz();
}
