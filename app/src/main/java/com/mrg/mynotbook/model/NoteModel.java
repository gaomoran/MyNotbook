package com.mrg.mynotbook.model;

import com.mrg.mynotbook.model.entities.Note;

import java.util.List;

/**
 * Created by MrG on 2017-05-06.
 */
public interface NoteModel {
    /**
     * 新建一个note
     * @param note
     */
    void addNote(Note note);

    /**
     * 删除一个note
     * @param id
     */
    void deleteNote(String id);

    /**
     * 修改一个note
     * @param note
     */
    void setNote(Note note);

    /**
     * 获取一个note
     * @param id
     */
    Note getNote(String id);

    /**
     * 获取全部note
     * @return
     */
    List<Note> getNoteAll();

    /**
     * 获取现在系统日期
     * @return
     */
    String getNowData();

    /**
     * 获取现在系统时间
     * @return
     */
    String getNowTime();
}
