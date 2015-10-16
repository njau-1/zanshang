package com.zanshang.models.audit;

import com.zanshang.framework.BaseAuditIndexStub;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 完成后需要创建Publisher
 * Created by Lookis on 6/5/15.
 */
@Document(collection = "audit_publisher")
public class AuditPublisher extends BaseAuditIndexStub {

    public AuditPublisher() {
    }

    public AuditPublisher(Object target) {
        super(target);
    }
}
