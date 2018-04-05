package com.mrg.mynotbook.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;

import com.mrg.mynotbook.R;

import com.mrg.mynotbook.app.OpenAlbumActivity;
import com.mrg.mynotbook.presenter.impi.ImpiNotePresenter;
import com.mrg.mynotbook.utils.StringUtils;
import com.mrg.mynotbook.widget.MyDialogYseNo;
import com.mrg.mynotbook.widget.RichEditorText;


public class NoteActivity extends OpenAlbumActivity implements NoteView, View.OnClickListener {

    private final static int PHOTO = 100;
    private final static int ALBUM = 200;
    private int isDelete = 1;
    public static final String TAG = "mrg";
    private Toolbar toolbar;
    private Intent intent;
    private static String NOTEID = "";
    private ImpiNotePresenter impiNotePresenter;

    private ImageButton mButaddimg;
    private ImageButton mButdel;
    private ImageButton mButtheme;
    private RichEditorText richEditorText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mButaddimg = (ImageButton) findViewById(R.id.btn_addimg);
        mButdel = (ImageButton) findViewById(R.id.btn_del);
        mButtheme = (ImageButton) findViewById(R.id.btn_theme);
        richEditorText = (RichEditorText) findViewById(R.id.ed_content);

        mButaddimg.setOnClickListener(this);
        mButdel.setOnClickListener(this);
        mButtheme.setOnClickListener(this);


        intent = getIntent();
        NOTEID = intent.getStringExtra("Id");
        impiNotePresenter = new ImpiNotePresenter(this, this);
        impiNotePresenter.initView();
        setSupportActionBar(toolbar);
        setToolBar(toolbar);
    }

    @Override
    public void setBarTitle(String date) {
        toolbar.setTitle(date);
    }

    @Override
    public void setBarSubtitle(String time) {
        toolbar.setSubtitle(time);
    }

    @Override
    public void setNoteNumber(String number) {

        richEditorText.setmContentList(number);


    }

    @Override
    public void addImage(String imageUrl) {

        richEditorText.addImage(imageUrl);

    }

    @Override
    public String getIntentType() {
        return intent.getStringExtra("Type");
    }

    @Override
    public String getIntentData() {
        return NOTEID;
    }

    @Override
    public void setDeleteEnabled(boolean bool) {
        mButdel.setEnabled(bool);
    }

    @Override
    public void onClick(View view) {

//        Log.i(TAG, "onClick: 点击事件："+view.getId());
        switch (view.getId()) {
            case R.id.btn_addimg://点击添加图像按


                showChooseGetPhoto();


                break;
            case R.id.btn_del://点击删除按钮
                MyDialogYseNo.Builder builder = new MyDialogYseNo.Builder(this);
                builder.setTitle("警告")
                        .setContentView(View.inflate(this, R.layout.dialog_phone_layout, null))
                        .setMessage("确定要删除便签？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                impiNotePresenter.deleteNote(NOTEID, intent.getStringExtra("Position"));
                                dialogInterface.dismiss();
                                isDelete = 0;
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();


                break;
            case R.id.btn_theme://点击设置主题按钮

                showInfo("素材收集中...");

                break;
            default:
                finish();
                break;
        }
    }


    @Override
    public void atAlbumGetImage(String imgUri) {
        impiNotePresenter.addImage(imgUri);

    }

    @Override
    public void atPhotoGetImage(String fileName) {
        impiNotePresenter.saveImage(fileName, this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String allData = richEditorText.getAllData();
        if (!allData.equals("") && isDelete == 1) {
            impiNotePresenter.saveNote(toolbar.getTitle().toString(),
                    toolbar.getSubtitle().toString(),
                    allData,

                    NOTEID, intent.getStringExtra("Position"));
        }

    }
}
