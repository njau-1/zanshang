package com.zanshang.services.salon.member;

import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.Salon;
import com.zanshang.models.index.SalonIndexByMemberId;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Created by Lookis on 7/3/15.
 */
public class SalonAddMemberActor extends AddToIndexActor<ObjectId, Salon> {

    public SalonAddMemberActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        Salon salon = extractValue(o);
        if (!salon.getMembers().contains(extractKey(o))) {
            salon.getMembers().add(extractKey(o));
            Update update = new Update();
            update.push(Salon.FIELD_MEMBERS, extractKey(o));
            getMongoTemplate().findAndModify(Query.query(Criteria.where("_id").is(salon.getUid())), update, Salon
                    .class);
        }
        super.onReceive(o);
    }

    @Override
    public Salon extractValue(Object o) {
        Params params = (Params) o;
        return params.getSalon();
    }

    @Override
    public ObjectId extractKey(Object o) {
        Params params = (Params) o;
        return params.getMemberId();
    }

    @Override
    public Class<? extends BaseArrayIndex<ObjectId, Salon>> indexClz() {
        return SalonIndexByMemberId.class;
    }

    public static class Params {

        private ObjectId memberId;

        private Salon salon;

        public Params(ObjectId memberId, Salon salon) {
            this.memberId = memberId;
            this.salon = salon;
        }

        public ObjectId getMemberId() {

            return memberId;
        }

        public Salon getSalon() {
            return salon;
        }
    }
}
