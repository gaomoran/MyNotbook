package com.mrg.mynotbook.ui;

import java.util.List;

/**
 * Created by MrG on 2017-05-06.
 */
public interface NoteView {

    void setBarTitle(String date);
    void setBarSubtitle(String time);
    void setNoteNumber(String number);
    void addImage(String imageUrl);
    String getIntentType();
    String getIntentData();
    void setDeleteEnabled(boolean bool);

}
