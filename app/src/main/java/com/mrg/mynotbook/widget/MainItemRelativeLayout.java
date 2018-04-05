package com.mrg.mynotbook.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by MrG on 2017-05-11.

 * 自定义layout
>>>>>>> parent of c2419a8... Revert "2.0版本更新"
 */
public class MainItemRelativeLayout extends RelativeLayout {
    public MainItemRelativeLayout(Context context) {
        super(context);
    }

    public MainItemRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainItemRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childWidthSize = getMeasuredWidth();
        //宽高黄金比例( •̀ ω •́ )y
        float h = (float) childWidthSize / 0.618f;

        setMeasuredDimension(childWidthSize,(int)h);
    }
}
