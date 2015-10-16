package com.zanshang.models.audit;

import com.zanshang.framework.BaseAuditIndexStub;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Lookis on 7/27/15.
 */
@Document(collection = "audit_company_account_register")
public class AuditCompany extends BaseAuditIndexStub {

    private AuditCompany() {
    }

    public AuditCompany(Object target) {
        super(target);
    }
}
