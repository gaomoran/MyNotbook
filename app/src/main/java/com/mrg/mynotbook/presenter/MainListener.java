package com.mrg.mynotbook.presenter;

import android.view.View;

import com.mrg.mynotbook.model.entities.ContentList;

import com.mrg.mynotbook.model.entities.Quite;


/**
 * Created by MrG on 2017-05-05.
 */
public interface MainListener {

    /**
     * 打开一个项目
     * @param contentList
     */
    void goItem(ContentList contentList);

    /**
     * 打开一个便签
     * @param id 便签的id 用于在数据库中查找
     * @param position 便签的角标 用于刷新界面
     */
    void goNoteItem(String id,String position);

    /**
     * 打开一个快速记录文字
     */
    void goQuiteText(Quite quite);



    /**
     * 新建日记
     */
    void addDiary();

    /**
     * 新建便签
     */
    void addNote();

    /**
     * 新建电话簿
     */
    void addPhone();

    /**
     * 删除一个子项目
     * @param contentList
     */
    void deleteItem(ContentList contentList);

    /**
     * 修改一个子项目
     * @param contentList
     */
    void editItem(ContentList contentList);

    /**
     * 显示快速记录文字窗口
     */
    void showQuiteText();

    /**
     * 显示快速录音窗口
     */
    void showQuiteAudio();

    /**
     * 显示选择图片窗口
     */
    void showChooseImage();
    /**
     * 保存一个快速记录的文字内容
     * @param number 内容
     */
    void saveQuiteText(String number);

    void saveQuiteImage(String number);

    /**
     * 删除一个快速记录
     * @param id 快速记录的id 用于在数据库中删除
     * @param position 快速记录的角标 用于刷新界面
     */
    void deleteQuite(String id,String position);

    void updateQuiteText(Quite quite);

}
