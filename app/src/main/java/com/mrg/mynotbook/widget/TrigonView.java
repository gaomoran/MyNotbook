package com.mrg.mynotbook.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.DrawableContainer;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mrg.mynotbook.R;

/**
 * Created by MrG on 2017-12-29.
 */
public class TrigonView extends View {

    private int color;

    public TrigonView(Context context) {
        super(context);

    }

    public TrigonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        color = ContextCompat.getColor(context, R.color.quiteImageColor);

    }
    public void setTrigonColor(int color){
        this.color=color;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        p.setColor(color);
        Path path = new Path();
        path.moveTo(90, 0);// 此点为多边形的起点
        path.lineTo(0, 47);
        path.lineTo(90, 96);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, p);

    }
}
