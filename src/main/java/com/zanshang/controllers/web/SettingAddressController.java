package com.zanshang.controllers.web;

import com.zanshang.models.Address;
import com.zanshang.models.Setting;
import com.zanshang.services.AddressTrapdoor;
import com.zanshang.services.SettingTrapdoor;
import com.zanshang.services.address.AddressTrapdoorImpl;
import com.zanshang.services.setting.SettingTrapdoorImpl;
import com.zanshang.utils.Ajax;
import com.zanshang.utils.AkkaTrapdoor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;

/**
 * Created by Lookis on 7/5/15.
 */
@Controller
@RequestMapping("/settings/addresses")
public class SettingAddressController {

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    AddressTrapdoor addressTrapdoor;

    SettingTrapdoor settingTrapdoor;

    @PostConstruct
    protected void init() {
        addressTrapdoor = akkaTrapdoor.createTrapdoor(AddressTrapdoor.class, AddressTrapdoorImpl.class);
        settingTrapdoor = akkaTrapdoor.createTrapdoor(SettingTrapdoor.class, SettingTrapdoorImpl.class);
    }

    @RequestMapping(method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public ModelAndView createForm(HttpServletRequest request, @RequestParam(value = "return", required = false) String returnUrl){
        ModelAndView mav = new ModelAndView("7_7");
        mav.addObject("return", returnUrl);
        return mav;
    }

    //创建收货地址
    @RequestMapping(method = RequestMethod.POST)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object create(@Valid AddressForm addressForm, BindingResult result, @RequestParam(value = "return",required = false)String returnUrl, Principal principal) {
        if (result.hasErrors()) {
            return Ajax.failure(result.getFieldErrors());
        } else {
            Address address = new Address(new ObjectId(principal.getName()), addressForm.getRecipient(), addressForm
                    .getTelephone(), addressForm.getPostCode(), addressForm.getAddress());
            addressTrapdoor.save(address, addressForm.isDefault);
        }
        if(returnUrl == null){
            return Ajax.ok();
        }else{
            return Ajax.ok(returnUrl);
        }
    }

    //设为默认收货地址
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object makeDefault(@PathVariable("id") String id, Principal principal) {
        Address address = addressTrapdoor.get(new ObjectId(id));
        Assert.isTrue(address.getUid().equals(new ObjectId(principal.getName())));
        addressTrapdoor.save(address, true);
        return Ajax.ok();
    }

    //删除收货地址
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Secured("ROLE_USER")
    @ResponseBody
    public Object delete(@PathVariable("id") String id, Principal principal) {
        ObjectId addressId = new ObjectId(id);
        Setting setting = settingTrapdoor.get(new ObjectId(principal.getName()));
        if (setting.getAddresses().contains(addressId)) {
            addressTrapdoor.delete(addressId);
        }
        return Ajax.ok();
    }

    static class AddressForm {

        private boolean isDefault;
        @NotNull(message = "{setting.addresses.recipient.notempty}")
        private String recipient;

        @NotNull(message = "{setting.addresses.telephone.notempty}")
        private String telephone;

        @NotNull(message = "{setting.addresses.postCode.notempty}")
        private String postCode;

        @NotNull(message = "{setting.addresses.address.notempty}")
        private String address;

        protected AddressForm() {
        }

        public AddressForm(boolean isDefault, String recipient, String telephone, String postCode, String address) {

            this.isDefault = isDefault;
            this.recipient = recipient;
            this.telephone = telephone;
            this.postCode = postCode;
            this.address = address;
        }

        public boolean isDefault() {

            return isDefault;
        }

        public void setDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRecipient() {
            return recipient;
        }

        public String getTelephone() {
            return telephone;
        }

        public String getPostCode() {
            return postCode;
        }

        public String getAddress() {
            return address;
        }
    }
}
