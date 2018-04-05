package com.mrg.mynotbook.presenter;

import android.view.View;

import com.mrg.mynotbook.model.entities.Diary;

/**
 * Created by MrG on 2017-05-08.
 */
public interface DiaryListenter {
    void onItemClickListenter(String id,String position);
    void setDiary(Diary diary,String id);
    Diary getDiary(String id);
    void deleteDiary(String id,String position);
    void addDiary(Diary diary,String who);
}
