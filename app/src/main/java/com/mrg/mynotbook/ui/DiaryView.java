package com.mrg.mynotbook.ui;

import android.view.View;

import com.mrg.mynotbook.model.entities.Diary;

import java.util.List;

/**
 * Created by MrG on 2017-05-08.
 */
public interface DiaryView {

    void showList(List<Diary> list);
    Diary getNowDiary();
    void startEditDiary(String id,String position);
    void addItem(Diary diary);
    void deleteItem(String id);
    void updataItem(String id,Diary diary);
    void showSureDelete(String id,String position);


    void showNull(boolean b);

}
