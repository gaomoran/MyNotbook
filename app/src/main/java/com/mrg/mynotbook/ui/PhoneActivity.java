package com.mrg.mynotbook.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import android.widget.TextView;


import com.mrg.mynotbook.R;
import com.mrg.mynotbook.adapter.PhoneUserListAdapter;
import com.mrg.mynotbook.app.BaseActivity;
import com.mrg.mynotbook.model.entities.PhoneUser;
import com.mrg.mynotbook.presenter.impi.ImpiPhonePresenter;
import com.mrg.mynotbook.widget.HintSideBar;
import com.mrg.mynotbook.widget.MyDialogYseNo;
import com.mrg.mynotbook.widget.SideBar;


import org.w3c.dom.Text;


import java.util.List;


public class PhoneActivity extends BaseActivity implements PhoneView, SideBar.OnChooseLetterChangedListener, PhoneUserListAdapter.OnRecyclerViewItemClickListener, Toolbar.OnMenuItemClickListener, View.OnClickListener {

    public static final String TAG = "mrg";
    private PhoneUserListAdapter adapter;
    private LinearLayoutManager manager;
    private HintSideBar hintSideBar;
    private RecyclerView recyclerView;
    private ImpiPhonePresenter impiPhonePresenter;
    private String who;

    private TextView tv_null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("电话簿");
        setToolBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_phone);
        toolbar.setOnMenuItemClickListener(this);
        hintSideBar = (HintSideBar) findViewById(R.id.hintSideBar);
        recyclerView = (RecyclerView) findViewById(R.id.rv_userList);

        tv_null = (TextView) findViewById(R.id.tv_null);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        hintSideBar.setOnChooseLetterChangedListener(this);
        who = getIntent().getStringExtra("Who");
        impiPhonePresenter = new ImpiPhonePresenter(this, this, this);
//        Log.i(TAG, "onCreate: 识别标号："+who);
        impiPhonePresenter.initView(who);


        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.mrg.myNoteBook.UI.ADD_PHONE_DATA");
        intentFilter.addAction("com.mrg.myNoteBook.UI.UP_PHONE_DATA");
        intentFilter.addAction("com.mrg.myNoteBook.UI.REMOVE_PHONE_DATA");
        registerReceiver(impiPhonePresenter, intentFilter);
    }

    @Override
    public void onChooseLetter(String s) {
        int i = adapter.getFirstPositionByChar(s.charAt(0));
        if (i == -1) {
            return;
        }
        manager.scrollToPositionWithOffset(i, 0);
    }

    @Override
    public void onNoChooseLetter() {
    }

    @Override
    public void showList(List<PhoneUser> list) {
        adapter = new PhoneUserListAdapter(this, list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showDialog(final String phoneNumber) {

        MyDialogYseNo.Builder builder = new MyDialogYseNo.Builder(this);
        View view = View.inflate(this, R.layout.dialog_phone_layout, null);
        builder.setTitle("提醒")
                .setContentView(view)
                .setMessage("确定以下拨打电话?\n" + phoneNumber)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        impiPhonePresenter.playCall(phoneNumber);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.create().show();

    }

    @Override
    public void call(final String phoneNumber) {

        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            showInfo("没有权限");
            return;
        }
        startActivity(intent);
    }

    @Override
    public void addItem(PhoneUser phoneUser) {

        adapter.addData(phoneUser);

        tv_null.setVisibility(View.GONE);

    }

    @Override
    public void deleteItem(String position) {
        adapter.removeData(Integer.parseInt(position));
    }

    @Override
    public void setItem(String position, PhoneUser phoneUser) {
        adapter.upData(Integer.parseInt(position), phoneUser);
    }

    @Override
    public void showEditItemDialog(final PhoneUser phoneUser) {
        final MyDialogYseNo.Builder builder = new MyDialogYseNo.Builder(this);
        final View view = View.inflate(this, R.layout.dialog_content_phone, null);
        final EditText username = (EditText) view.findViewById(R.id.ed_dialog_phone_username);
        final EditText phone = (EditText) view.findViewById(R.id.ed_dialog_phone_phone);
        username.setText(phoneUser.getUserName());
        username.setSelection(phoneUser.getUserName().length());
        phone.setText(phoneUser.getPhone());
        phone.setSelection(phoneUser.getPhone().length());
        builder.setTitle("修改联系人")
                .setContentView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        phoneUser.setPhone(phone.getText().toString());
                        phoneUser.setUserName(username.getText().toString());
                        impiPhonePresenter.editItem(phoneUser);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.create().show();

    }

    @Override
    public void showMessage(String message) {
        showInfo(message);
    }

    @Override
    public void showSureDelete(final PhoneUser phoneUser) {
        MyDialogYseNo.Builder builder = new MyDialogYseNo.Builder(this);
        builder.setTitle("警告")
                .setContentView(View.inflate(this, R.layout.dialog_phone_layout, null))
                .setMessage("确定要删除这个联系人吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        impiPhonePresenter.onDeleteItem(phoneUser);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    @Override

    public void showNull(boolean b) {
        if (b) {
            tv_null.setVisibility(View.VISIBLE);
        } else {
            tv_null.setVisibility(View.GONE);
        }
    }

    @Override

    public void onItemClick(PhoneUser data) {
        impiPhonePresenter.onItemClickListener(data);
    }

    @Override
    public void onDeleteClick(PhoneUser phoneUser) {
        impiPhonePresenter.sureDelete(phoneUser);
    }

    @Override
    public void onEditClick(PhoneUser phoneUser) {
        impiPhonePresenter.onEditItem(phoneUser);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        MyDialogYseNo.Builder builder = new MyDialogYseNo.Builder(this);
        final View view = View.inflate(this, R.layout.dialog_content_phone, null);
        builder.setTitle("新建联系人")
                .setContentView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText username = (EditText) view.findViewById(R.id.ed_dialog_phone_username);
                        EditText phone = (EditText) view.findViewById(R.id.ed_dialog_phone_phone);

                        impiPhonePresenter.addItem(username.getText().toString(), phone.getText().toString(), who);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.create().show();

        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销广播
        unregisterReceiver(impiPhonePresenter);
    }


}
