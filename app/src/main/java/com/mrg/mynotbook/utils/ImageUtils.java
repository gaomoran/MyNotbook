package com.mrg.mynotbook.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import com.mrg.mynotbook.R;

import java.io.File;

/**
 * Created by MrG on 2017-06-16.
 * 处理图片的工具类
 */
public class ImageUtils {

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public Bitmap getSmallBitmap(String filePath,Context mContext) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        File file = new File(filePath);
        Bitmap bitmap;
        if (file.exists()) {
            // 缩放比例
            options.inSampleSize = calculateInSampleSize(options, 480, 800)*2;

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeFile(filePath, options);




            return bitmap;

        }else {
            //图片不存在时加载
            Bitmap bmp=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.info);
            return bmp;
        }
    }


    public int[] getSmallImageWidthHeight(String filePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        File file = new File(filePath);
        if (file.exists()) {
            // 缩放比例
            options.inSampleSize = calculateInSampleSize(options, 480, 800) * 2;

            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            return new int[]{options.outWidth,options.outHeight};

        }

        return new int[]{0,0};
    }



    public  Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight,Context mContext) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        File file = new File(filePath);
        Bitmap bitmap;
        if (file.exists()) {
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeFile(filePath, options);
            if (bitmap != null) {
                DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
                int w_screen = dm.widthPixels;
                int w_width = w_screen;
                int b_width = bitmap.getWidth();
                int b_height = bitmap.getHeight();
                int w_height = w_width * b_height / b_width;
            } else {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.info);

            }
            return bitmap;

        } else {
            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.info);
            return bmp;
        }
    }

    //计算图片的缩放值
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
