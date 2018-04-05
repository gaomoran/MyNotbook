package com.mrg.mynotbook.presenter;


import android.view.View;

import com.mrg.mynotbook.model.entities.ContentList;

import java.util.List;


/**
 * Created by MrG on 2017-05-05.
 */
public interface MainPresenter {
   /**
    * 初始化视图
    */
   void initview();
   ContentList getNewData();

   /**
    * 保存一个日记

    * @param number 日记内容
    * @param cover 日记封面序号

    */
   void saveDiary(String number,String cover);

   /**
    * 保存一个电话本

    * @param number 电话号码

    */
   void savePhone(String number);

   /**
    * 更新一个日记
    * @param contentList
    */
   void updataDiary(ContentList contentList);

   /**
    * 更新一个电话本
    * @param contentList
    */
   void updataPhone(ContentList contentList);

   /**
    * 确认删除
    * @param contentList
    */
   void sureDelete(ContentList contentList);


   /**
    * 开始录音
    */
   void startAudio();

   /**
    * 保存录音
    */
   void saveAudio();

   /**
    * 取消录音
    */
   void cancelAudio();



}
