package com.mrg.mynotbook.model;

import com.mrg.mynotbook.model.entities.PhoneUser;

import java.util.List;

/**
 * Created by MrG on 2017-05-06.
 */
public interface PhoneUserModel {

    List<PhoneUser> getPhoneUserList(String who);
    void addPhone(PhoneUser phoneUser,String who);
    void deletePhone(String id);
    void setPhone(String id,PhoneUser phoneUser);
    PhoneUser getPhone(String id);
    PhoneUser getNewPhone(String who);
    void deleteTypePhone(String who);
}
