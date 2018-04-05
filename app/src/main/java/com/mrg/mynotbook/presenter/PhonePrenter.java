package com.mrg.mynotbook.presenter;

import com.mrg.mynotbook.model.entities.PhoneUser;

/**
 * Created by MrG on 2017-05-06.
 */
public interface PhonePrenter {
    void initView(String who);
    void editItem(PhoneUser phoneUser);
    void sureDelete(PhoneUser phoneUser);
}
