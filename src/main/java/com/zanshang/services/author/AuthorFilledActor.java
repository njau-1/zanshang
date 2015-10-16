package com.zanshang.services.author;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import com.zanshang.constants.ActorConstant;
import com.zanshang.framework.BaseUntypedActor;
import com.zanshang.models.Company;
import com.zanshang.models.Person;
import com.zanshang.services.company.CompanyGetActor;
import com.zanshang.services.person.PersonGetActor;
import com.zanshang.utils.AkkaTrapdoor;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Future;

/**
 * Created by Lookis on 6/9/15.
 */
public class AuthorFilledActor extends BaseUntypedActor {

    ActorRef personActor;

    ActorRef companyActor;

    public AuthorFilledActor(ApplicationContext spring) {
        super(spring);
        personActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(PersonGetActor.class, spring)));
        companyActor = getContext().actorOf(Props.create(AkkaTrapdoor.creator(CompanyGetActor.class, spring)));
    }

    @Override
    public void onReceive(Object o) throws Exception {
        //o is uid
        Future<Object> askPerson = Patterns.ask(personActor, o.toString(), ActorConstant.DEFAULT_TIMEOUT);
        ActorRef sender = getSender();
        final ActorRef self = getSelf();
        askPerson.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object personInformation) throws Throwable {
                if (personInformation != null) {
                    Person information = (Person) personInformation;
                    if (StringUtils.isEmpty(information.getIdentityCode()) ||
                            StringUtils.isEmpty(information.getIdentityFront()) ||
                            StringUtils.isEmpty(information.getIdentityBack()) ||
                            StringUtils.isEmpty(information.getLegalName())) {
                        sender.tell(Boolean.FALSE, self);
                    } else {
                        sender.tell(Boolean.TRUE, self);
                    }
                } else {
                    Future<Object> askCompany = Patterns.ask(companyActor, o.toString(), ActorConstant.DEFAULT_TIMEOUT);
                    askCompany.onSuccess(new OnSuccess<Object>() {
                        @Override
                        public void onSuccess(Object companyInformation) throws Throwable {
                            if (companyInformation != null) {
                                Company information = (Company) companyInformation;
                                if (StringUtils.isEmpty(information.getCompanyCode()) ||
                                        StringUtils.isEmpty(information.getLicense()) ||
                                        StringUtils.isEmpty(information.getContactPhone())) {
                                    sender.tell(Boolean.FALSE, self);
                                } else {
                                    sender.tell(Boolean.TRUE, self);
                                }
                            } else {
                                Exception exception = new Exception("Found user neither has Person information nor " +
                                        "Company " +
                                        "information:" + o);
                                sender.tell(new Status.Failure(exception), self);
                                throw exception;
                            }
                        }
                    }, getContext().dispatcher());
                }
            }
        }, getContext().dispatcher());
    }
}
