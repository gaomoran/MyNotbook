package com.mrg.mynotbook.ui;


import android.view.View;

import com.mrg.mynotbook.model.entities.ContentList;
import com.mrg.mynotbook.model.entities.Quite;

import com.mrg.mynotbook.presenter.impi.ImpiMainPresenter;

import java.util.List;

/**
 * Created by MrG on 2017-05-05.
 */
public interface MainView {
    /**
     * 显示list view
     * @param list
     */

    void showList(List<ContentList> list,List<Quite> quiteList);


    /**
     * 打开便签界面
     * @param intent
     */
    void startNoteActivity(String intent,String id,String position);

    /**
     * 打开电话界面
     * @param intent
     */
    void startPhoneActivity(String intent,String id,String position,String who);

    /**
     * 打开日记界面
     * @param intent
     */
    void startDiaryActivity(String intent,String id,String position,String who);

    /**
     * 更新一个数据
     * @param position
     * @param contentList
     */

    void updateListView(int position,ContentList contentList);


    /**
     * 增加一个数据
     * @param contentList
     */
    void addDataListView(ContentList contentList);

    /**
     *删除一个数据
     * @param position
     */
    void removeDataListView(int position);

    /**
     * 显示一个新建日记界面
     */
    void addDiaryItem();

    /**
     * 显示一个新建电话本界面
     */
    void addPhoneItem();

    /**
     * 显示修改日记本界面
     * @param contentList
     */
    void setDiaryItem(ContentList contentList);

    /**
     * 显示修改电话簿界面
     * @param contentList
     */
    void setPhoneItem(ContentList contentList);

    /**
     * 显示确认删除窗口
     */
    void showSureDelete(ContentList contentList);

    /**
     * 显示/关闭没有内容提醒文字
     * @param bool
     */
    void showIsNullText(boolean bool);


    /**
     * 显示快速添加文字界面
     */
    void showQuiteText();

    /**
     * 显示快速添加图片界面
     */
    void showQuiteImage();

    /**
     * 显示快速录音界面
     */
    void showQuiteAudio();

    /**
     * 添加一个快速记录
     * @param newQuite
     */
    void addQuiteDateList(Quite newQuite);

    /**
     * 删除一个快速记录
     * @param position 角标
     */
    void deleteQuiteItem(int position);

    /**
     * 修改一个快速记录
     * @param position 角标
     * @param quite    快速记录对象
     */
    void updateQuiteItem(int position,Quite quite);

    /**
     * 打开一个快速记录
     * @param quite 快速记录对象
     */
    void openQuiteTextItem(Quite quite);

    void getPermission();



}
