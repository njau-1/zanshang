package com.zanshang.services.project;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Project;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lookis on 6/21/15.
 */
public class ProjectFindByIdsActor extends BaseUntypedActor {

    public ProjectFindByIdsActor(ApplicationContext spring) {
        super(spring);
    }

    /**
     * 如果传了Pageable，就返回PageImpl，否则直接返回List
     *
     * @param o
     * @throws Exception
     */

    @Override
    public void onReceive(Object o) throws Exception {
        List<ObjectId> ids;
        Pageable pageable = null;
        if (o instanceof Object[]) {
            Object[] params = (Object[]) o;
            ids = (List<ObjectId>) params[0];
            pageable = (Pageable) params[1];
        } else {
            ids = (List<ObjectId>) o;
        }
        Query query = Query.query(Criteria.where("_id").in(ids));
        long count = getMongoTemplate().count(query, Project.class);
        if (pageable != null) {
            query.with(pageable);
        }
        List<Project> projects = getMongoTemplate().find(query, Project.class);
        if (pageable != null) {
            PageImpl<Project> ret = new PageImpl<Project>(projects, pageable, count);
            getSender().tell(ret, getSelf());
        } else {
            Map<ObjectId, Project> retMap = new HashMap<>();
            for (Project project : projects) {
                retMap.put(project.getId(), project);
            }
            getSender().tell(retMap, getSelf());
        }
    }
}
