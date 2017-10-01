package com.anormaly.labs.animationdemo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ArcFragment extends Fragment implements IndexActivity.OnClickListener
{

    private float x;
    private float y;
    private static final String TAG = ArcFragment.class.getSimpleName();

    @BindView(R.id.layout)
    ConstraintLayout layout;

    @BindView(R.id.box)
    View boxMain;

    @BindView(R.id.box_left)
    View boxLeft;

    @BindView(R.id.box_right)
    View boxRight;
    private GestureDetector detector;
    private boolean isInitialState = true;
    private ImageView dashedCircleImageView;
    private int mDistanceBetweenBoxes;
    private AnimatedVectorDrawable drawable;
    private GestureDetector.OnGestureListener mRotateGestureListener;

    public ArcFragment()
    {
        // Required empty public constructor
    }

    public static ArcFragment newInstance(float x, float y)
    {
        ArcFragment fragment = new ArcFragment();
        Bundle args = new Bundle();
        args.putFloat(IndexActivity.BOX_POSITION_X, x);
        args.putFloat(IndexActivity.BOX_POSITION_Y, y);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            x = getArguments().getFloat(IndexActivity.BOX_POSITION_X, Float.MAX_VALUE);
            y = getArguments().getFloat(IndexActivity.BOX_POSITION_Y, Float.MAX_VALUE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_arc, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        ((IndexActivity) getActivity()).setOnClickListener(this);
    }

    @Override
    public void onReplayClick()
    {

        if(isInitialState)
        {
            isInitialState =false;
            addImageView();
            setupArchAnimation(boxMain,boxLeft,boxRight);
            mRotateGestureListener = new GestureDetector.OnGestureListener()
            {
                @Override
                public boolean onDown(MotionEvent e)
                {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e)
                {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent e)
                {
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
                {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e)
                {

                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
                {
                    setUpFlingAnimationOn(boxLeft, velocityX);
                    setUpFlingAnimationOn(boxRight, velocityX);
                    setUpFlingAnimationOn(boxMain, velocityX);
                    return false;
                }
            };
            detector = new GestureDetector(getContext(), mRotateGestureListener);

            layout.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    detector.onTouchEvent(event);
                    return false;
                }
            });
        }else{
            layout.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    return true;
                }
            });

            unsetArcAnimationOnView(boxRight);
            unsetArcAnimationOnView(boxMain);
            unsetArcAnimationOnView(boxLeft);
            if(dashedCircleImageView!=null){
                dashedCircleImageView.animate().alpha(1).setListener(new Animator.AnimatorListener()
                {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        layout.removeView(dashedCircleImageView);
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
            }
            isInitialState = true;
        }
    }

    private void addImageView()
    {

        if(dashedCircleImageView== null)
        {
            mDistanceBetweenBoxes = (int) (Math.abs((boxLeft.getX() + boxLeft.getWidth() / 2) - (boxRight.getX() + boxRight.getWidth() / 2)));
            dashedCircleImageView = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    mDistanceBetweenBoxes,
                    mDistanceBetweenBoxes
            );
            drawable = (AnimatedVectorDrawable) getContext().getDrawable(R.drawable.animated_curved_circle);
            dashedCircleImageView.setLayoutParams(params);

            dashedCircleImageView.setImageDrawable(drawable);
            dashedCircleImageView.setX(layout.getPivotX() - mDistanceBetweenBoxes/2);
            dashedCircleImageView.setY(layout.getPivotY() - mDistanceBetweenBoxes/2);
            dashedCircleImageView.setAlpha(0f);
        }
            drawable.start();

        layout.addView(dashedCircleImageView, 0);
        ObjectAnimator
                .ofFloat(dashedCircleImageView,"alpha",0f,1f)
                .start();

    }

    private void setupArchAnimation(final View...box){
        for(int i =0;i<box.length;i++)
        {
            box[i].setPivotX((boxMain.getX() + boxMain.getPivotX()) - (box[i].getX()));
            box[i].setPivotY((boxMain.getY() + boxMain.getPivotY()) - (box[i].getY()));
            box[i].animate().setDuration(800).setInterpolator(new LinearInterpolator()).rotation(360);
        }
    }

    private void setUpFlingAnimationOn(View box, float velocityX)
    {
        FlingAnimation animation = new FlingAnimation(box, DynamicAnimation.ROTATION);
        animation.setFriction(1f);
        animation.setStartVelocity(velocityX/2);
        animation.start();
    }

    private void unsetArcAnimationOnView(View box)
    {
        box.animate().rotation(0);
    }

    @Override
    public void onNextClick()
    {
        if(!isInitialState){
            onReplayClick();
        }else{

        }
    }

    @Override
    public void onPrevClick()
    {
        if(!isInitialState){
            onReplayClick();
        }else{
            ActivityUtils.addFragmentToActivity(getFragmentManager(),StagingFragment.newInstance(boxMain.getX(),boxMain.getY(),boxLeft.getX(),boxLeft.getY(),boxRight.getX(),boxRight.getY()),R.id.content_view);
        }
    }

    private void log(View view)
    {
        Log.d(TAG,
                "For Y: \n"
                        + view.getScaleY() + "\n"
                        + view.getPivotY() + "\n"
                        + view.getY() + "\n"
                        + view.getTranslationY() + "\n"
                        + view.getRotationY() + "\n"
                        + view.getHeight() + "\n"
                        + view.getMeasuredHeight()
        );
        Log.d(TAG,
                "For X: \n"
                        + view.getScaleX() + "\n"
                        + view.getPivotX() + "\n"
                        + view.getX() + "\n"
                        + view.getTranslationX() + "\n"
                        + view.getRotationX() + "\n"
                        + view.getWidth() + "\n"
                        + view.getMeasuredWidth()
        );
    }
}
