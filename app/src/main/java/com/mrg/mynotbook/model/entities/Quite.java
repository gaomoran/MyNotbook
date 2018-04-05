package com.mrg.mynotbook.model.entities;

/**
 * Created by MrG on 2017-11-15.
 * 快速记录类
 */
public class Quite {
    private String id;//id
    private String number;//内容
    private String type;//类型 T:文字 I:图片 A:语音
    private String date;//日期
    private String time;//时间
    private String week;//星期
    private String position;//角标

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
