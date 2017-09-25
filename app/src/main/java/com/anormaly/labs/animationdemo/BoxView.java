package com.anormaly.labs.animationdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ichigo on 24/09/17.
 */

public class BoxView extends View
{

    private Paint paintBox;

    @ColorInt
    private int boxColor;

    public BoxView(Context context)
    {
        super(context,null);
        setupPaint();
    }

    public BoxView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        //canvas.drawColor(paintBox.getColor());
        //canvas.drawRect();
    }

    private void setupPaint() {
        boxColor = ContextCompat.getColor(getContext(),R.color.colorPrimaryLight);
        paintBox = new Paint();
        paintBox.setStyle(Paint.Style.FILL);
        paintBox.setColor(boxColor);
        paintBox.setTextSize(30);
    }

}
