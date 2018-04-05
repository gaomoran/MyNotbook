package com.mrg.mynotbook.model;

import com.mrg.mynotbook.model.entities.Note;
import com.mrg.mynotbook.model.entities.Quite;

import java.util.List;

/**
 * Created by MrG on 2017-11-15.
 */
public interface QuiteModel {

    List<Quite> getAll();

    Quite getNewQuite();

    Quite getQuite(String id);

    void addQuite(Quite quite);

    void deleteQuite(String id);

    void setQuite(Quite quite);

    String getNowData();

    String getNowTime();

    void importNoteDate(List<Note> list);
}
