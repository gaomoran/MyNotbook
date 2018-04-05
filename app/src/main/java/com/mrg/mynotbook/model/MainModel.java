package com.mrg.mynotbook.model;

import com.mrg.mynotbook.model.entities.ContentList;

import com.mrg.mynotbook.model.entities.Note;


import java.util.List;

/**
 * Created by MrG on 2017-05-05.
 */
public interface MainModel {
    /**
     * 获取全部数据
     * @return
     */
    List<ContentList> getContentList();

    /**
     * 获取最新一条数据
     * @return
     */
    ContentList getNewData();

    /**
     * 获取一条数据
     * @param id
     * @return
     */
    ContentList getMainData(String id);

    /**
     * 删除一条数据
     * @param id
     */
    void deleteMainData(String id);

    /**
     * 新增一个日记本
     * @param number
     */
    void addDiary(String number,String cover);

    /**
     * 新增一个电话本
     * @param number
     */
    void addPhone(String number);

    /**
     * 修改一个日记本
     * @param contentList
     */
    void setDiary(ContentList contentList);

    /**
     * 修改一个电话簿
     * @param contentList
     */
    void setPhone(ContentList contentList);



    List<Note> getNoteAll();

    void deleteAllNote();

}
