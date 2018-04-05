package com.mrg.mynotbook.ui;

import com.mrg.mynotbook.model.entities.Diary;
import com.mrg.mynotbook.model.entities.PhoneUser;
import com.mrg.mynotbook.model.entities.Quite;
import com.mrg.mynotbook.model.entities.SearchEntity;

import java.util.List;

/**
 * Created by MrG on 2018-03-18.
 */

public interface SearchView {
    /**
     * 显示查找出来的日记数据
     * @param data 日记数据列表
     */
    void showSearchDiaryData(List<SearchEntity> data);

    /**
     * 显示查找出来的快速记录
     * @param data 快速记录数据
     */
    void showSearchQuiteData(List<SearchEntity> data);

    /**
     * 显示查找出来的电话数据
     * @param data 电话数据
     */
    void showSearchPhoneData(List<SearchEntity> data);

    /**
     * 清空显示的查询数据
     */
    void removeSearchData();

    /**
     * 刷新数据
     */
    void reloadSearchData();
}
