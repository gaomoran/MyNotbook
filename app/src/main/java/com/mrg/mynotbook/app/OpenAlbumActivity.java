package com.mrg.mynotbook.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;

import com.mrg.mynotbook.utils.FileUtils;
import com.mrg.mynotbook.utils.StringUtils;

/**
 * Created by MrG on 2017-12-30.
 */
public class OpenAlbumActivity extends BaseActivity {
    private final static int PHOTO = 100;
    private final static int ALBUM = 200;
    private String fileName;

    public void showChooseGetPhoto() {

        //动态权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查相册和拍照权限
            boolean[] booleans = checkOpenAlbumAndCamerPermissions();
            if (booleans[0]&&booleans[1]){
                //有权限则打开
                showChoosePhone();
            }else {
                //有其中一个没申请就警告
                showNotPermissionInfo(503);
            }
        }else {
            showChoosePhone();
        }


    }

    /**
     * 弹出选择窗
     */
    private void showChoosePhone(){
        final CharSequence[] items = {"图库选择", "相机拍摄"};
        AlertDialog dlg = new AlertDialog.Builder(this).setTitle("选择图片").setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 1) {//拍照
                            fileName = StringUtils.getRandomFileName();
                            Uri uri = new FileUtils().saveImage(fileName, getApplicationContext());
                            Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
                                getImageByCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                                getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);//将拍取的照片保存到指定URI
                                startActivityForResult(getImageByCamera, PHOTO);
                            } else {
                                //6.0以下

                                getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                startActivityForResult(getImageByCamera, PHOTO);
                            }
                        } else {//相册
                            Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                            getImage.setType("image/*");
                            startActivityForResult(getImage, ALBUM);
                        }
                    }
                }).create();
        dlg.show();


    }

    @Override
    public void getPermission(int code) {
        //重新申请两个权限
        if (code==503) {
            reqestCamerAndAlbumPsemissions();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO://手机拍摄
                    atPhotoGetImage(fileName);
                    break;
                case ALBUM://相册获取

                    Uri url = data.getData();
                    String imgUri = StringUtils.getImageAbsolutePath(this, url);
                    atAlbumGetImage(imgUri);
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 拍照后获取图片
     *
     * @param fileName
     */
    public void atPhotoGetImage(String fileName) {
    }

    /**
     * 从图库获取图片
     *
     * @param imgUri
     */
    public void atAlbumGetImage(String imgUri) {
    }

    ;

}
