package com.zanshang.models.global;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Project;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * //这个数据是项目浏览页的，浏览页里的数据就是这里的value列表所代表的project,包括顺序
 * Created by Lookis on 6/24/15.
 */
@Document(collection = "globals")
public class ProjectHome extends BaseArrayIndex<String, Project> {

    public static final String GLOBAL_KEY = "project_home";

    public ProjectHome() {
        super(GLOBAL_KEY);
    }
}
