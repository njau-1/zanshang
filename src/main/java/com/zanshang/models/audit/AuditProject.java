package com.zanshang.models.audit;

import com.zanshang.framework.BaseAuditIndexStub;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 审核Project通过之前需要审核所有的作者身份
 * @see com.zanshang.models.audit.AuditAuthorInformation
 *
 * 审核完成后需要创建ProjectFeedback
 * Created by Lookis on 5/27/15.
 */
@Document(collection = "audit_project")
public class AuditProject extends BaseAuditIndexStub {

    private AuditProject() {
    }

    public AuditProject(Object target) {
        super(target);
    }
}
