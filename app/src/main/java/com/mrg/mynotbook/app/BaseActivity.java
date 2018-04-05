package com.mrg.mynotbook.app;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mrg.mynotbook.R;

import com.xiaomi.ad.AdSdk;


/**
 * 所有activity基类
 * Created by MrG on 2017-05-05.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String APP_ID = "2882303761517579253";
    private static final int ALBUM = 500;
    private static final int CAMERA = 501;
    private static final int AUDIO = 502;
    private static final int ALBUM_CAMERA = 503;
    private static final int READ_PHONE_STATE = 504;
    private static final int LOCATION=505;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            setContentView(R.layout.activity_base);
        }else {
            setContentView(R.layout.activity_base);

        }

//        AdSdk.setDebugOn();
        AdSdk.initialize(this, APP_ID);

    }

    public void showInfo(String info) {

        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置统一标题栏
     * @param toolbar
     */
    public void setToolBar(Toolbar toolbar){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        toolbar.setNavigationIcon(R.drawable.ic_bar_back);
        toolbar.setNavigationOnClickListener(this);
    }

    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public void closeSoftKeyboard() {

        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

    }


    /**
     * 检查权限
     *
     * @param permissions 权限值
     * @return 是否开启权限
     */
    private boolean checkSelfPermissions(String permissions) {

        return checkSelfPermission(permissions) == PackageManager.PERMISSION_GRANTED ? true : false;
    }

    /**
     * 检查读取相册和拍照两个权限
     *
     * @return
     */
    public boolean[] checkOpenAlbumAndCamerPermissions() {
        boolean[] req = {false, false};
        req[0] = checkOpenAlbumPermissions();
        req[1] = checkCameraPermissions();
        return req;
    }

    /**
     * 检查读取相册权限
     */
    public boolean checkOpenAlbumPermissions() {
        return checkSelfPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 检查打开相机权限
     */
    public boolean checkCameraPermissions() {

        return checkSelfPermissions(Manifest.permission.CAMERA);
    }

    /**
     * 检查录音权限
     */
    public boolean checkRecordAudioPermissions() {

        return checkSelfPermissions(Manifest.permission.RECORD_AUDIO);

    }

    /**
     * 检查读取手机状态权限
     *
     * @return 是否有该权限
     */
    public boolean checkReadPhoneStartPermission() {
        return checkSelfPermissions(Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * 检查定位权限
     * @return
     */
    public boolean checkLocationPermission(){
        return checkSelfPermissions(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    /**
     * 检查写入文件权限
     * @return
     */
    public boolean checkWritePermission(){

        return checkSelfPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    public boolean checkReadPermission(){
        return checkSelfPermissions(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
    }
    /**
     * 申请权限
     *
     * @param permissions 要申请权限列表
     * @param code        返回标识码
     */

    private void requestPermissionss(String[] permissions, int code) {


        requestPermissions(permissions, code);

    }

    public void requestNecessary(){
        requestPermissionss(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
                ,Manifest.permission.READ_PHONE_STATE},499);


    }

    /**
     * 本地读写权限
     */
    public void requestReadAndWrite(){

        requestPermissionss(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, ALBUM);

    }

    /**
     * 申请读取相册权限
     */
    public void requestOpenAlbumPermissions() {
        requestPermissionss(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ALBUM);
    }

    /**
     * 申请打开相机权限
     */
    public void requestCameraPermissions() {

        requestPermissionss(new String[]{Manifest.permission.CAMERA}, CAMERA);
    }

    /**
     * 申请录音权限
     */
    public void requestRecordAudioPermissions() {

        requestPermissionss(new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE}, AUDIO);

    }

    /**
     * 申请读取相册和拍照两个权限
     */
    public void reqestCamerAndAlbumPsemissions() {

        requestPermissionss(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, ALBUM_CAMERA);

    }

    /**
     * 获取读取手机状态权限
     */
    public void requestReadPhoneStartPsemission() {

        requestPermissionss(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
    }

    /**
     * 申请定位权限
     */
    public void requestLocationPermission(){
        requestPermissionss(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION);

    }
    /**
     * 显示没有权限对话框
     */
    public void showNotPermissionInfo(final int code) {
        new AlertDialog.Builder(this)
                .setTitle("需要权限！")
                .setMessage("没有权限功能将无法正常使用\n是否获取？")
                .setNeutralButton("获取", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPermission(code);
                    }
                })
                .setPositiveButton("取消", null)
                .create().show();
    }
    public void showNotPermissionInfo(final int code,String title,String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("获取", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPermission(code);
                    }
                })
                .setPositiveButton("取消", null)
                .create().show();
    }
    /**
     * 再次获取权限
     */
    public void getPermission(int code) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length != 0) {
            if (grantResults[0] == 0) {
                showInfo("获取权限成功(｡･∀･)ﾉﾞ");
            } else {
                showInfo("获取权限失败╮(╯▽╰)╭");
            }
        } else {
            showInfo("拒绝申请！");
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
