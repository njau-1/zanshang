package com.zanshang.models.audit;

import com.zanshang.framework.BaseAuditIndexStub;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 完成后创建 AuthorInformationIndexByIdentity, Salon
 * Created by Lookis on 5/27/15.
 */
@Document(collection = "audit_author_information")
public class AuditAuthorInformation extends BaseAuditIndexStub{

    private AuditAuthorInformation() {
    }

    public AuditAuthorInformation(Object target) {
        super(target);
    }
}
