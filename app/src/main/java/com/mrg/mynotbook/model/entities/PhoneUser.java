package com.mrg.mynotbook.model.entities;


import android.widget.PopupWindow;

import com.mrg.mynotbook.utils.PhoneNameInspect;

/**
 * Created by MrG on 2017-05-06.
 */
public class PhoneUser{
    private String userName;

    private String phone;

    private String id;

    private String sort;

    private String position;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhone() {
        return phone;
    }

}
