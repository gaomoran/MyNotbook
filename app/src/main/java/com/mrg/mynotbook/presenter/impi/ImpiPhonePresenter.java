package com.mrg.mynotbook.presenter.impi;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.mrg.mynotbook.model.PhoneUserModel;
import com.mrg.mynotbook.model.entities.PhoneUser;
import com.mrg.mynotbook.model.impi.ImpiPhoneUserModel;
import com.mrg.mynotbook.presenter.PhoneListenter;
import com.mrg.mynotbook.presenter.PhonePrenter;
import com.mrg.mynotbook.ui.PhoneView;


import java.util.List;


/**
 * Created by MrG on 2017-05-06.
 */
public class ImpiPhonePresenter extends BroadcastReceiver implements PhonePrenter, PhoneListenter {

    private PhoneView phoneView;
    private PhoneUserModel phoneUserModel;
    private Context context;
    private Activity activity;

    public ImpiPhonePresenter(PhoneView phoneView, Context context, Activity activity) {
        this.phoneView = phoneView;
        phoneUserModel = new ImpiPhoneUserModel(context);
        this.context = context;
        this.activity = activity;
    }


    @Override
    public void initView(String who) {

        List<PhoneUser> phoneUserList = phoneUserModel.getPhoneUserList(who);
        if (phoneUserList.size()==0){
            phoneView.showNull(true);
        }else {

            phoneView.showNull(false);
        }
        phoneView.showList(phoneUserList);

    }

    @Override
    public void editItem(final PhoneUser phoneUser) {

        new Thread() {
            @Override
            public void run() {
                phoneUserModel.setPhone(phoneUser.getId(), phoneUser);
                Intent intent = new Intent();
                intent.setAction("com.mrg.myNoteBook.UI.UP_PHONE_DATA");
                intent.putExtra("Id", phoneUser.getId());
                intent.putExtra("Position", phoneUser.getPosition());
                context.sendBroadcast(intent);

            }
        }.start();
    }

    @Override
    public void sureDelete(PhoneUser phoneUser) {
        phoneView.showSureDelete(phoneUser);
    }


    @Override
    public void onItemClickListener(PhoneUser data) {
        String phoneNumber = data.getPhone();
        phoneView.showDialog(phoneNumber);
    }

    @Override
    public void playCall(String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 123);
        }
        phoneView.call(phoneNumber);
    }

    @Override
    public void onDeleteItem(final PhoneUser phoneUser) {

        new Thread() {
            @Override
            public void run() {
                phoneUserModel.deletePhone(phoneUser.getId());
                Intent intent = new Intent();
                intent.setAction("com.mrg.myNoteBook.UI.REMOVE_PHONE_DATA");
                intent.putExtra("Id", phoneUser.getId());
                intent.putExtra("Position", phoneUser.getPosition());
                context.sendBroadcast(intent);

            }
        }.start();


    }

    @Override
    public void onEditItem(final PhoneUser phoneUser) {

        phoneView.showEditItemDialog(phoneUser);


    }


    @Override
    public void addItem(final String username, final String phone, final String who) {

        new Thread() {
            @Override
            public void run() {
                PhoneUser phoneUser = new PhoneUser();
                phoneUser.setUserName(username);
                phoneUser.setPhone(phone);
                phoneUser.setId("new");
                phoneUserModel.addPhone(phoneUser, who);
                Intent intent = new Intent();
                intent.setAction("com.mrg.myNoteBook.UI.ADD_PHONE_DATA");
                intent.putExtra("Who", who);
                context.sendBroadcast(intent);

            }
        }.start();


    }

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {

            case "com.mrg.myNoteBook.UI.ADD_PHONE_DATA":
                String who = intent.getStringExtra("Who");
                PhoneUser newPhone = phoneUserModel.getNewPhone(who);

                if (newPhone.getUserName() != null) {
                    phoneView.addItem(newPhone);
                } else {
                    phoneView.showMessage("添加失败");
                }
                break;

            case "com.mrg.myNoteBook.UI.REMOVE_PHONE_DATA":
                String id = intent.getStringExtra("Id");
                String position1 = intent.getStringExtra("Position");
                phoneUserModel.deletePhone(id);
                phoneView.deleteItem(position1);
                break;

            case "com.mrg.myNoteBook.UI.UP_PHONE_DATA":
                String id1 = intent.getStringExtra("Id");
                String position = intent.getStringExtra("Position");
                PhoneUser phone = phoneUserModel.getPhone(id1);

                phoneView.setItem(position, phone);
                break;


        }


    }
}
