//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mrg.mynotbook.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DataAudioView extends TextView {
    private String audioPath;
    private String audioTime;
    private int index;

    public DataAudioView(Context context) {
        super(context);
}

    public DataAudioView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DataAudioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getAudioTime() {
        return audioTime;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getAudioData() {
        return audioPath+"#"+audioTime;
    }

    public void setAudioTime(String audioTime) {
        this.audioTime = audioTime;
        this.setText(audioTime);
    }
}
