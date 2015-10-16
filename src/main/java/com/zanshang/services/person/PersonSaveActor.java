package com.zanshang.services.person;

import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Person;
import com.zanshang.models.WechatAccount;
import com.zanshang.models.WeiboAccount;
import com.zanshang.models.audit.AuditAuthorInformation;
import com.zanshang.models.index.AuthorInformationIndexByIdentity;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by Lookis on 6/4/15.
 */
public class PersonSaveActor extends BaseUntypedActor {

    public PersonSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Person person = (Person) o;
        //如果已经认证过，而且数据有修改，就放弃修改
        Person origin = getMongoTemplate().findById(person.getUid(), Person.class);
        boolean legalInformationModified;
        if (origin == null) {
            legalInformationModified = true;
        } else {
            AuthorInformationIndexByIdentity byIdentity = person.getIdentityCode() == null ? null : getMongoTemplate
                    ().findById(person.getIdentityCode(), AuthorInformationIndexByIdentity.class);
            legalInformationModified = (!StringUtils.equals(origin.getIdentityCode(), person.getIdentityCode())) ||
                    (!StringUtils.equals(origin.getIdentityFront(), person.getIdentityFront())) ||
                    (!StringUtils.equals(origin.getIdentityBack(), person.getIdentityBack())) ||
                    (!StringUtils.equals(origin.getLegalName(), person.getLegalName()));
            boolean verified = byIdentity != null;
            if (verified && legalInformationModified) {
                logger.error("Try to modify Legal information after verified. " + person.getUid());
                person.setIdentityCode(origin.getIdentityCode());
                person.setIdentityFront(origin.getIdentityFront());
                person.setIdentityBack(origin.getIdentityBack());
                person.setLegalName(origin.getLegalName());
            }
        }

        boolean filled = StringUtils.isNotEmpty(person.getIdentityCode()) &&
                (StringUtils.isNotEmpty(person.getIdentityFront())) &&
                (StringUtils.isNotEmpty(person.getIdentityBack())) &&
                (StringUtils.isNotEmpty(person.getLegalName()));
        //如果没认证过，可是数据填满了，就提交审核
        if (legalInformationModified && filled) {
            AuditAuthorInformation auditAuthorInformation = new AuditAuthorInformation(person.getUid());
            getMongoTemplate().save(auditAuthorInformation);
        } else {
            //否则把审核里的数据删除了
            //update: xuming; //如果用户点击修改却没有变动数据则会发生误删除.
//            getMongoTemplate().findAndRemove(Query.query(Criteria.where("_id").is(person.getUid())),
//                    AuditAuthorInformation.class);
        }

        //下面是connect部分
        String originWechatId = origin == null ? null : origin.getWechatId();
        String newWechatId = person.getWechatId();
        if (!StringUtils.equals(originWechatId, newWechatId)) {
            if (originWechatId != null) {
                WechatAccount originWechatAccount = getMongoTemplate().findById(originWechatId, WechatAccount.class);
                if (StringUtils.equals(originWechatAccount.getUsername(), origin.getUid().toHexString())) {
                    getMongoTemplate().remove(originWechatAccount);
                } else {
                    logger.error("Someone connect to a account with error WechatAccount(belong to another one), " +
                            "notfix. uid:" + origin.getUid());
                }
            }
            if (newWechatId != null) {
                WechatAccount connectedAccount = getMongoTemplate().findById(newWechatId, WechatAccount.class);
                if (connectedAccount != null) {
                    //将要创建的 connect账号已经在站内被另一个用户绑定，先解除
                    Person connectedPerson = getMongoTemplate().findById(connectedAccount.getUsername(), Person.class);
                    connectedPerson.setWechatId(null);
                    getMongoTemplate().save(connectedPerson);
                    getMongoTemplate().remove(connectedAccount);
                }
                //绑定新账号
                WechatAccount newWechatAccount = new WechatAccount(person.getUid(), newWechatId);
                getMongoTemplate().save(newWechatAccount);
            }
        }

        String originWeiboId = origin == null ? null : origin.getWeiboId();
        String newWeiboId = person.getWeiboId();
        if (!StringUtils.equals(originWeiboId, newWeiboId)) {
            if (originWeiboId != null) {
                WeiboAccount originWeiboAccount = getMongoTemplate().findById(originWeiboId, WeiboAccount.class);
                if (StringUtils.equals(originWeiboAccount.getUsername(), origin.getUid().toHexString())) {
                    getMongoTemplate().remove(originWeiboAccount);
                } else {
                    logger.error("Someone connect to a account with error WeiboAccount(belong to another one), " +
                            "notfix. uid:" + origin.getUid());
                }
            }
            if (newWeiboId != null) {
                WeiboAccount connectedAccount = getMongoTemplate().findById(newWeiboId, WeiboAccount.class);
                if (connectedAccount != null) {
                    //将要创建的 connect账号已经在站内被另一个用户绑定，先解除
                    Person connectedPerson = getMongoTemplate().findById(connectedAccount.getUsername(), Person.class);
                    connectedPerson.setWeiboId(null);
                    getMongoTemplate().save(connectedPerson);
                    getMongoTemplate().remove(connectedAccount);
                }
                //绑定新账号
                WeiboAccount newWeiboAccount = new WeiboAccount(person.getUid(), newWeiboId);
                getMongoTemplate().save(newWeiboAccount);
            }
        }

        getMongoTemplate().save(person);
    }
}
