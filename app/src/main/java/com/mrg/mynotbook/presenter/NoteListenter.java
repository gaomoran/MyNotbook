package com.mrg.mynotbook.presenter;

import android.content.Context;
import android.net.Uri;


import java.util.List;

/**
 * Created by MrG on 2017-05-06.
 */
public interface NoteListenter {
    /**
     * 保存内容
     * @param data
     * @param time
     * @param number
     */
    void saveNote(String data,String time,String number,String id,String position);

    /**
     * 删除这个note
     * @param id
     */
    void deleteNote(String id,String position);

    void addImage(String url);

    Uri saveImage(String filename,Context context);

}
