package com.mrg.mynotbook.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import com.mrg.mynotbook.R;
import com.mrg.mynotbook.presenter.impi.ImpiMainPresenter;
import com.mrg.mynotbook.widget.MyDialogYseNo;

/**
 * Created by MrG on 2017-11-16.
 */
public class DialogUtils implements DialogInterface.OnClickListener {
    private Context context;
    private MyDialogYseNo.Builder builder;
    public final static int ADD_DIARY = 0;
    public final static int ADD_NOTE = 1;
    public final static int ADD_PHONE = 2;
    public final static int QUITE_TEXT = 3;
    public final static int QUITE_IMAGE = 4;
    public final static int QUITE_AUDIO = 5;
    private View dialogView;


    public DialogUtils(Context context) {
        this.context = context;
        builder = new MyDialogYseNo.Builder(context);
    }


    public View getDialogView(int type) {
        switch (type) {
            case ADD_DIARY:
                dialogView = View.inflate(context, R.layout.dialog_add_diary, null);
                break;

            case ADD_PHONE:
                dialogView = View.inflate(context, R.layout.dialog_add_phone, null);
                break;
        }

        return dialogView;

    }

    public MyDialogYseNo.Builder getAddPhoneDialog() {
        getDialogView(ADD_PHONE);
        builder.setTitle("新建电话簿");
        builder.setContentView(dialogView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText username = (EditText) dialogView.findViewById(R.id.ed_dialog_phone_username);
                String name = username.getText().toString();

                if (name.equals("")) {

                } else {

                }
                dialog.dismiss();
            }
        });

        return builder;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
