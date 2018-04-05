package com.mrg.mynotbook.model;

import com.mrg.mynotbook.model.entities.Diary;
import com.mrg.mynotbook.model.entities.PhoneUser;
import com.mrg.mynotbook.model.entities.Quite;
import com.mrg.mynotbook.model.entities.SearchEntity;

import java.util.List;

/**
 * Created by MrG on 2018-03-18.
 */

public interface SearchModel {
    /**
     * 根据关键字搜索日记数据
     * @param keyword 关键字
     * @return
     */
    List<SearchEntity> SearchDiaryData(String keyword);

    /**
     * 根据关键字搜索快速记录数据
     * @param keyword 关键字
     * @return
     */
    List<SearchEntity> SearchQuiteData(String keyword);

    /**
     * 根据关键字搜索电话数据
     * @param keyword 关键字
     * @return
     */
    List<SearchEntity> SearchPhoneData(String keyword);
}
