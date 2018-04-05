package com.mrg.mynotbook.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.List;

/**
 * Created by MrG on 2017-05-10.
 */
public interface EditDiaryPresenter {

    void initView(Intent intent);
    void saveDiary(String id,
                   String title,
                   String number,
                   String month,
                   String day,
                   String week,
                   int collection,
                   int mood,
                   int weather,
                   String position,
                   String who,
                   String location);
    Uri saveImage(String fileName,Context context);
    void addImage(String uri);

    void loaction();


}
