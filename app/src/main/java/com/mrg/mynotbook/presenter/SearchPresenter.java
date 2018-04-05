package com.mrg.mynotbook.presenter;

import com.mrg.mynotbook.model.entities.SearchEntity;

/**
 * Created by MrG on 2018-03-18.
 */

public interface SearchPresenter {
    /**
     * 搜索
     * @param keyword 关键字
     */
    void Search(String keyword);

    void updateQuite(SearchEntity entity);
}
