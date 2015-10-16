package com.zanshang.models.global;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Project;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 这里的value都是项目id
 * Created by Lookis on 6/25/15.
 */
@Document(collection = "globals")
public class SalonHome extends BaseArrayIndex<String, Project> {

    public static final String GLOBAL_KEY = "salon_home";

    public SalonHome() {
        super(GLOBAL_KEY);
    }
}
