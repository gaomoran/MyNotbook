package com.mrg.mynotbook.ui;

import java.util.List;

/**
 * Created by MrG on 2017-05-10.
 */
public interface EditDiaryView {
    /**
     * 设置收藏
     * @param collection
     */
    void setCollection(int collection);

    /**
     * 修改收藏状态
     */
    void updataCollection();

    /**
     * 设置心情
     * @param mood
     */
    void setMood(int mood);

    /**
     * 显示选择心情框
     */

    void showChooseMood();


    /**
     * 显示选择天气框
     */

    void showChooseWeather();


    /**
     * 设置天气
     * @param weather
     */
    void setWeather(int weather);

    /**
     * 设置内容
     * @param number
     */
    void setDiaryContent(String number);

    /**
     * 设置标题
     * @param title
     */
    void setDiaryTitle(String title);

    /**
     * 设置月份
     * @param month
     */
    void setMonth(String month);

    /**
     * 设置天
     * @param day
     */
    void setDay(String day);

    /**
     * 设置星期
     * @param week
     */
    void setWeek(String week);

    /**
     * 显示更多框
     */
    void showMore();

    /**
     * 添加图片
     * @param uri
     */
    void addPhone(String uri);

    /**
     * 显示选择获取图片方法
     */

    void showChooseGetImageDialog();


    /**
     * 打开添加语音界面
     */
    void addVoice();

    /**
     * 设置定位
     * @param location
     */
    void setLocation(String location);

    /**
     * 弹出消息
     * @param info
     */
    void showInfo(String info);

    /**
     * 添加语音到内容
     * @param path
     */
    void addVoiceInEditText(String path);

    /**
     * 关闭底部的工具
     */
    void closeViewUtils();


    /**
     * 检查定位权限
     */
    void checkLocation();


}
