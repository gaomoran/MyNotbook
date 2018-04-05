package com.mrg.mynotbook.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.mrg.mynotbook.config.MyNoteBookConfig;

import java.io.File;

/**
 * Created by MrG on 2017-12-30.
 * 文件工具类
 */
public class FileUtils {
    /**
     * 初始化路径
     * @param outFilePath
     */
    private void initFilePath(File outFilePath){
        if (!outFilePath.exists()) {
            outFilePath.mkdirs();
        }
    }

    /**
     * 初始化图片路径
     */
    private void initImageFilePath(){

        File outFilePath = new File(MyNoteBookConfig.imageUrl);
        initFilePath(outFilePath);
    }

    /**
     * 初始化语音路径
     */
    private void initAudioFilePath(){
        File outFilePath = new File(MyNoteBookConfig.audioUrl);
        initFilePath(outFilePath);

    }

    /**
     * 保存图片
     * @param fileName
     * @param context
     * @return
     */
    public Uri saveImage(String fileName,Context context){
        initImageFilePath();
        File outFile = new File(MyNoteBookConfig.imageUrl + fileName + ".jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return FileProvider.getUriForFile(context, "com.mrg.mynotbook", outFile);

        } else {

            return Uri.fromFile(outFile);
        }
    }






}
