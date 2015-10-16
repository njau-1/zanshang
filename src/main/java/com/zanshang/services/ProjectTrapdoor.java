package com.zanshang.services;

import com.zanshang.framework.Price;
import com.zanshang.models.Project;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Lookis on 6/5/15.
 */
public interface ProjectTrapdoor {

    Page<Project> globalProject(Pageable pageable);

    Page<Project> findByUid(ObjectId uid, Pageable pageable);

    Page<Project> findByPublisherId(ObjectId uid, Pageable pageable);

    Page<Project> findByIds(List<ObjectId> ids, Pageable pageable);

    Map<ObjectId, Project> findByIds(List<ObjectId> ids);

    Price getPaid(ObjectId projectId);

    Map<ObjectId, Price> getPaids(Collection<ObjectId> projectIds);

    Project get(ObjectId id);

    void save(Project project);

}
