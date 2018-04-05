package com.mrg.mynotbook.config;

import android.os.Environment;

/**
 * Created by MrG on 2017-05-09.
 */
public class MyNoteBookConfig {
    public final static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public final static String imageUrl=PATH+"/MyNoteBook/image/";
    public final static String audioUrl=PATH+"/MyNoteBook/audio/";
}
