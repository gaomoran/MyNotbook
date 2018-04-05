package com.mrg.mynotbook.model.entities;

import java.util.List;

/**
 * Created by MrG on 2017-05-06.
 */
public class Note {
    private long sort;//排序标识
    private String date;//日期
    private String time;//时间
    private String number;//内容
    private String id;//id

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
