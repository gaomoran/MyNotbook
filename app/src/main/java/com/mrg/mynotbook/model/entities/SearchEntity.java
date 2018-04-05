package com.mrg.mynotbook.model.entities;

/**
 * Created by MrG on 2018-03-20.
 */

public class SearchEntity {
    private String date;
    private String searchNumber;
    private String id;
    private String SearchType;
    private String title;
    private String who;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchNumber() {
        return searchNumber;
    }

    public void setSearchNumber(String searchNumber) {
        this.searchNumber = searchNumber;
    }

    public String getSearchType() {

        return SearchType;
    }

    public void setSearchType(String searchType) {
        SearchType = searchType;
    }


}
