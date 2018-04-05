package com.mrg.mynotbook.model;

import android.view.View;

import com.mrg.mynotbook.model.entities.Diary;

import java.util.List;

/**
 * Created by MrG on 2017-05-08.
 */
public interface DiaryModel {
    List<Diary> getDiaryList(String who);
    void addDiary(Diary diary,String who);
    void deleteDiary(String id);
    void setDiary(Diary diary,String id);
    Diary getDiary(String id);
    Diary getNewDairy(String who);
    void deleteTypeDiary(String who);

}
