package com.zanshang.services.company;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Company;
import com.zanshang.models.Publisher;
import com.zanshang.models.audit.AuditAuthorInformation;
import com.zanshang.models.index.AuthorInformationIndexByIdentity;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

/**
 * Created by Lookis on 6/4/15.
 */
public class CompanySaveActor extends BaseUntypedActor {

    public CompanySaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Company company = (Company) o;
        //如果已经被认证过作者，或者已经成为了出版社，而且数据有修改，就放弃修改
        Company origin = getMongoTemplate().findById(company.getUid(), Company.class);

        AuthorInformationIndexByIdentity byIdentity = getMongoTemplate().findById(company.getCompanyCode(),
                AuthorInformationIndexByIdentity.class);
        Publisher publisher = getMongoTemplate().findById(company.getUid(), Publisher.class);

        boolean legalInformationModified;
        if (origin == null) {
            legalInformationModified = false;
        } else {
            legalInformationModified = (!origin.getCompanyCode().equals(company.getCompanyCode())) || (!origin
                    .getLicense().equals(company.getLicense()));
        }
        //可能是另一个出版人员注册的相同的企业，所以这里不能仅用之前存在就跳过, 通过 origin = null 来check
        if ((byIdentity != null || publisher != null) && legalInformationModified) {
            logger.error("Try to modify company Legal information after verified. " + company.getUid());
            return;
        } else {
            //这里的全填满需要调用者check
            boolean filled = StringUtils.isNotEmpty(company.getCompanyCode()) &&
                    (StringUtils.isNotEmpty(company.getLicense())) &&
                    (StringUtils.isNotEmpty(company.getCompanyName())) &&
                    (StringUtils.isNotEmpty(company.getContactPhone()));
            if (filled) {
                getMongoTemplate().save(company);
                if (legalInformationModified) {
                    AuditAuthorInformation auditAuthorInformation = new AuditAuthorInformation(company.getUid());
                    getMongoTemplate().save(auditAuthorInformation);
                }
            } else {
                logger.error("Try to modify company Legal information with blank field. " + company.getUid());
                return;
            }
        }
    }
}
