package com.anormaly.labs.animationdemo.animationprinciples.anticipation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;

import com.anormaly.labs.animationdemo.ActivityUtils;
import com.anormaly.labs.animationdemo.IndexActivity;
import com.anormaly.labs.animationdemo.R;
import com.anormaly.labs.animationdemo.StagingFragment;

/**
 * Created by ichigo on 24/09/17.
 */

public class AnticipationView extends View implements IndexActivity.OnClickListener
{
    private static final String TAG = AnticipationView.class.getSimpleName();
    private float iX = Float.MAX_VALUE;
    private float iY = Float.MAX_VALUE;
    private Paint paintBox;

    @ColorInt
    private int boxColor;
    private Rect boxRect;
    private int canvasWidth = Integer.MIN_VALUE;
    private int canvasHeight = Integer.MIN_VALUE;
    private float offsetX;
    private float tension;
    private float distanceOffset;
    private float heightOffset;
    private int width = Integer.MAX_VALUE;
    private int height = Integer.MAX_VALUE;
    private boolean initialTranslationRunning;
    private Path pathBox = new Path();
    private float boxX;
    private float boxY;
    private int sWidth= Integer.MAX_VALUE;
    private int sHeight = Integer.MAX_VALUE;

    public AnticipationView(Context context)
    {
        super(context, null);
    }

    public AnticipationView(Context context, int width, int height, float x, float y)
    {
        super(context);
        this.width = width;
        this.height = height;
        this.iX = x;
        this.iY = y;
    }

    public AnticipationView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnticipationView, 0, 0);
        height = a.getDimensionPixelSize(R.styleable.AnticipationView_height, 100);
        width = a.getDimensionPixelSize(R.styleable.AnticipationView_width, 100);

        setupPaint();


    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        //canvas.drawColor(paintBox.getColor());
        if (initialTranslationRunning != true && (iX != Float.MAX_VALUE || iY != Float.MAX_VALUE))
        {
            initialTranslationRunning = true;
            iX = iX + width / 2;
            iY = iY + height;
            performInitialTranslation(iX, iY);
            animateBoxToInitialPosition(canvas);
        } else if (initialTranslationRunning == true && (iX != Float.MAX_VALUE || iY != Float.MAX_VALUE))
        {
            animateBoxToInitialPosition(canvas);
        } else
            drawBox(canvas);

    }

    private void animateBoxToInitialPosition(Canvas canvas)
    {
        //canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));

        //Prospective positions
        /*
        float x1 = getCanvasWidth() / 2 - width / 2 + distanceOffset, y1 = getCanvasHeight() + heightOffset;
        float x2 = getCanvasWidth() / 2 - width / 2 + offsetX + distanceOffset, y2 = getCanvasHeight() - height + heightOffset;
        float x3 = getCanvasWidth() / 2 + width / 2 + offsetX + distanceOffset, y3 = getCanvasHeight() - height + heightOffset;
        float x4 = getCanvasWidth() / 2 + width / 2 + distanceOffset, y4 = getCanvasHeight() + heightOffset;*/


        pathBox.reset();
        pathBox.moveTo(iX - width / 2, iY);
        pathBox.lineTo(iX - width / 2, iY - height);
        pathBox.lineTo(iX + width / 2, iY - height);
        pathBox.lineTo(iX + width / 2, iY);
        canvas.drawPath(pathBox, paintBox);

        boxX = iX;
        boxY = iY;

    }

    private void drawBox(Canvas canvas)
    {
        //canvas.drawRect(boxRect,paintBox);
        pathBox.reset();
        pathBox.moveTo(getCanvasWidth() / 2 - width / 2 + distanceOffset, getCanvasHeight() + heightOffset);
        pathBox.lineTo(getCanvasWidth() / 2 - width / 2 + offsetX + distanceOffset, getCanvasHeight() - height + heightOffset);
        pathBox.lineTo(getCanvasWidth() / 2 + width / 2 + offsetX + distanceOffset, getCanvasHeight() - height + heightOffset);
        pathBox.lineTo(getCanvasWidth() / 2 + width / 2 + distanceOffset, getCanvasHeight() + heightOffset);
        canvas.drawPath(pathBox, paintBox);

        boxX = getCanvasWidth() / 2 + distanceOffset;
        boxY = getCanvasHeight() + heightOffset;

    }

    private void initBox()
    {
        if (width == Integer.MAX_VALUE || height == Integer.MAX_VALUE)
        {
            width = getCanvasWidth() / 4;
            height = getCanvasWidth() / 4;
        }
        boxRect = new Rect();
        boxRect.set(((getCanvasWidth() / 2) - (width / 2)), getCanvasHeight() - height, (getCanvasWidth() / 2) + (width / 2), getCanvasHeight());
    }

    private void setupPaint()
    {
        boxColor = ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
        paintBox = new Paint();
        paintBox.setStyle(Paint.Style.FILL_AND_STROKE);
        paintBox.setColor(boxColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY)
        {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST)
        {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else
        {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY)
        {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST)
        {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else
        {
            //Be whatever you want
            height = desiredHeight;
        }

        canvasWidth = width;
        canvasHeight = height;
        //MUST CALL THIS
        setMeasuredDimension(width, height);
        initBox();
    }


    private int getCanvasWidth()
    {
        if (canvasWidth != Integer.MIN_VALUE)
            return canvasWidth;
        else
        {
            throw new NumberFormatException("Value uninitialized.");
        }
    }

    private int getCanvasHeight()
    {
        if (canvasHeight != Integer.MIN_VALUE)
            return canvasHeight;
        else
        {
            throw new NumberFormatException("Value uninitialized.");
        }
    }

    @Override
    public void onReplayClick()
    {

        //Skew Box to right with Anticipation
        ValueAnimator skewRightAnimator = ValueAnimator.ofFloat(0, width / 3);
        skewRightAnimator.setInterpolator(new AnticipateInterpolator(10));
        skewRightAnimator.setDuration(300);
        skewRightAnimator.setEvaluator(new FloatEvaluator());
        skewRightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                offsetX = (float) animation.getAnimatedValue();
                invalidate();
            }
        });


        //Translate box to Right, then (without animator) translate vertically centered on canvas and skew left
        final ValueAnimator translateXRightAnimator = ValueAnimator.ofFloat(0, (getCanvasWidth() - width / 2));
        translateXRightAnimator.setInterpolator(new LinearInterpolator());
        translateXRightAnimator.setDuration(300);
        translateXRightAnimator.setEvaluator(new FloatEvaluator());
        translateXRightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                distanceOffset = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        translateXRightAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                offsetX = -(getCanvasWidth() / 4) / 3;
                heightOffset = -getCanvasHeight() / 2 + height / 2;
            }

            @Override
            public void onAnimationStart(Animator animation)
            {
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {
            }
        });


        //Translate box left to center of the canvas
        final ValueAnimator translateXLeftAnimator = ValueAnimator.ofFloat((getCanvasWidth() - width / 2), -(getCanvasWidth() - width / 2));
        translateXLeftAnimator.setInterpolator(new LinearInterpolator());
        translateXLeftAnimator.setDuration(600);
        translateXLeftAnimator.setEvaluator(new FloatEvaluator());
        translateXLeftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                distanceOffset = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        translateXLeftAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                offsetX = width / 3;
                heightOffset = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        });
        translateXLeftAnimator.setStartDelay(300);

        ValueAnimator translateXCenterAnimator = ValueAnimator.ofFloat(-(getCanvasWidth() - width / 2), 0);
        translateXCenterAnimator.setInterpolator(new LinearInterpolator());
        translateXCenterAnimator.setDuration(300);
        translateXCenterAnimator.setEvaluator(new FloatEvaluator());
        translateXCenterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                distanceOffset = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        //Skew back to make it rectangle
        final ValueAnimator skewLeftAnimator = ValueAnimator.ofFloat(width / 3, 0);
        skewLeftAnimator.setInterpolator(new AnticipateOvershootInterpolator(5));
        skewLeftAnimator.setDuration(150);
        skewLeftAnimator.setEvaluator(new FloatEvaluator());
        skewLeftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                offsetX = (float) animation.getAnimatedValue();
                invalidate();
            }
        });


        AnimatorSet completeSet = new AnimatorSet();
        completeSet.playSequentially(skewRightAnimator, translateXRightAnimator, translateXLeftAnimator, translateXCenterAnimator, skewLeftAnimator);
        completeSet.start();
    }

    @Override
    public void onNextClick()
    {

    }

    @Override
    public void onPrevClick()
    {

    }


    public void setWidth(int width)
    {
        width = ActivityUtils.dpToPx(width, getContext());
        this.width = width;
    }


    public void setHeight(int height)
    {
        height = ActivityUtils.dpToPx(height, getContext());
        this.height = height;
    }

    public void setXY(float x, float y)
    {
        iX = x;
        iY = y;
        invalidate();
    }

    public void setXY(float x, float y,int width,int height)
    {
        iX = x;
        iY = y;
        sWidth = width;
        sHeight = height;
        invalidate();
    }

    private void performInitialTranslation(float x, float y)
    {
        ValueAnimator translateXAnimator = ValueAnimator.ofFloat(x, getCanvasWidth() / 2);
        translateXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                iX = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        ValueAnimator translateYAnimator = ValueAnimator.ofFloat(y, getCanvasHeight());
        translateYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                iY = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator widthAnimator = ValueAnimator.ofInt(sWidth,width);
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                width = (int) animation.getAnimatedValue();
            }
        });

        ValueAnimator heightAnimator = ValueAnimator.ofInt(sHeight,height);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                height = (int) animation.getAnimatedValue();
            }
        });

        AnimatorSet completeValue = new AnimatorSet();
        if(sWidth==Integer.MAX_VALUE || sHeight==Integer.MAX_VALUE)
            completeValue.playTogether(translateXAnimator, translateYAnimator);
        else
            completeValue.playTogether(translateXAnimator, translateYAnimator,widthAnimator,heightAnimator);
        completeValue.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                iX = Float.MAX_VALUE;
                iY = Float.MAX_VALUE;
                initialTranslationRunning = false;
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        });
        completeValue.start();

        invalidate();
    }

    public float getBoxX()
    {
        return boxX;
    }

    public float getBoxY()
    {
        return boxY;
    }
}
