package com.mrg.mynotbook.ui;

import com.mrg.mynotbook.model.entities.PhoneUser;

import java.util.List;

/**
 * Created by MrG on 2017-05-06.
 */
public interface PhoneView {
    void showList(List<PhoneUser> list);
    void showDialog(String phoneNumber);
    void call(String phoneNumber);
    void addItem(PhoneUser phoneUser);
    void deleteItem(String position);
    void setItem(String position,PhoneUser phoneUser);
    void showEditItemDialog(PhoneUser phoneUser);
    void showMessage(String message);
    void showSureDelete(PhoneUser phoneUser);

    void showNull(boolean b);

}
