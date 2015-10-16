package com.zanshang.framework.index;

import com.mongodb.DBObject;
import com.zanshang.framework.BaseUntypedActor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Lookis on 7/3/15.
 */
public abstract class FindByIndexPageableActor<K, V> extends BaseUntypedActor {

    public FindByIndexPageableActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        IndexPageableParams params = (IndexPageableParams) o;
        Pageable pageable = params.getPageable();
        Document annotation = AnnotationUtils.findAnnotation(indexClz(), Document.class);
        DBObject document = getMongoTemplate().getCollection(annotation.collection()).findOne(params.getKey());
        if (document == null) {
            getSender().tell(new PageImpl<V>(new ArrayList<V>(), pageable, 0), getSelf());
            return;
        }
        Collection<?> o1 = (Collection<?>) document.get(BaseArrayIndex.VALUE_FIELD);
        int size = o1.size();
        int pageSize = pageable.getPageSize();
        if (pageable.getOffset() != 0 && size - pageable.getOffset() > 0 && size - pageable.getOffset() < pageSize) {
            pageSize = size - pageable.getOffset();
        }
        if (pageable.getOffset() != 0 && size < pageable.getOffset()) {
            getSender().tell(new PageImpl<V>(new ArrayList<V>(), pageable, size), getSelf());
            return;
        }
        Query query = Query.query(Criteria.where("_id").is(params.getKey()));
        int reverse = reverse() ? -1 : 1;
        if (pageable.getOffset() != 0) {
            query.fields().slice(BaseArrayIndex.VALUE_FIELD, reverse * pageable.getOffset() - pageSize,
                    pageSize);
        } else {
            query.fields().slice(BaseArrayIndex.VALUE_FIELD, reverse * pageSize);
        }
        BaseArrayIndex<K, V> arrayIndex = getMongoTemplate().findOne(query, indexClz());
        if (arrayIndex == null) {
            getSender().tell(new PageImpl<V>(new ArrayList<V>(), pageable, size), getSelf());
            return;
        }
        List<V> value = arrayIndex.getValue();
        if (logger.isDebugEnabled()) {
            for (V v : value) {
                if (v == null) {
                    throw new NullPointerException("Index and the entity does not match...");
                }
            }
        }
        Collections.reverse(value);
        Page<V> page = new PageImpl<V>(value, pageable, size);
        getSender().tell(page, getSelf());
    }

    public abstract Class<? extends BaseArrayIndex<K, V>> indexClz();

    protected boolean reverse() {
        return false;
    }
}
