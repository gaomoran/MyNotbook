//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mrg.mynotbook.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

public class DataEditTextView extends EditText {
    private int index;

    public DataEditTextView(Context context) {
        super(context);
}

    public DataEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DataEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
