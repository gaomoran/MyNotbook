package com.mrg.mynotbook.presenter;

import android.view.View;

import com.mrg.mynotbook.model.entities.PhoneUser;

/**
 * Created by MrG on 2017-05-06.
 */
public interface PhoneListenter {
    void onItemClickListener(PhoneUser data);
    void playCall(String phoneNumber);
    void onDeleteItem(PhoneUser phoneUser);
    void onEditItem(PhoneUser phoneUser);
    void addItem(String username,String phone,String who);

}
