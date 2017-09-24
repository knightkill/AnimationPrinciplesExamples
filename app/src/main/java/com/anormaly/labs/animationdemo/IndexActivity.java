package com.anormaly.labs.animationdemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class IndexActivity extends AppCompatActivity
{

    private static final String TAG = IndexActivity.class.getSimpleName();

    @BindView(R.id.next_fab)
    FloatingActionButton nextFAB;

    @BindView(R.id.prev_fab)
    FloatingActionButton prevFAB;

    @BindView(R.id.replay_fab)
    FloatingActionButton replayFAB;

    @BindView(R.id.pager)
    ViewPager pager;

    private AnimatorSet replayAnimator;
    private OnReplayClicked mOnReplayClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        animations();
        initPager();
    }

    private void initPager()
    {
        pager.setAdapter(new IndexPagerAdapter(getSupportFragmentManager()));
    }

    private void animations()
    {
        nextAnimation();
    }

    private void nextAnimation()
    {
        float percent = 0.80f;
        float principlePercent = 1.1f;
        long duration = 300;
        DecelerateInterpolator interpolator = new DecelerateInterpolator();

        AnimatorSet replayDownAnimator = new AnimatorSet();
        replayDownAnimator.playTogether(
                ObjectAnimator.ofFloat(nextFAB, "scaleX", nextFAB.getScaleX(), nextFAB.getScaleX() * percent),
                ObjectAnimator.ofFloat(nextFAB, "scaleY", nextFAB.getScaleY(), nextFAB.getScaleY() * percent),
                ObjectAnimator.ofFloat(prevFAB, "scaleX", prevFAB.getScaleX(), prevFAB.getScaleX() * percent),
                ObjectAnimator.ofFloat(prevFAB, "scaleY", prevFAB.getScaleY(), prevFAB.getScaleY() * percent),
                ObjectAnimator.ofFloat(replayFAB, "scaleX", replayFAB.getScaleX(), replayFAB.getScaleX() * principlePercent),
                ObjectAnimator.ofFloat(replayFAB, "scaleY", replayFAB.getScaleY(), replayFAB.getScaleY() * principlePercent)
        );

        AnimatorSet replayUpAnimator = new AnimatorSet();
        replayUpAnimator.playTogether(
                ObjectAnimator.ofFloat(nextFAB, "scaleX", nextFAB.getScaleX() * percent, nextFAB.getScaleX()),
                ObjectAnimator.ofFloat(nextFAB, "scaleY", nextFAB.getScaleY() * percent, nextFAB.getScaleY()),
                ObjectAnimator.ofFloat(prevFAB, "scaleX", prevFAB.getScaleX() * percent, prevFAB.getScaleX()),
                ObjectAnimator.ofFloat(prevFAB, "scaleY", prevFAB.getScaleY() * percent, prevFAB.getScaleY()),
                ObjectAnimator.ofFloat(replayFAB, "scaleX", replayFAB.getScaleX() * principlePercent, replayFAB.getScaleX()),
                ObjectAnimator.ofFloat(replayFAB, "scaleY", replayFAB.getScaleY() * principlePercent, replayFAB.getScaleY())

        );

        replayAnimator = new AnimatorSet();
        replayAnimator.playSequentially(replayDownAnimator, replayUpAnimator);
        replayAnimator.setInterpolator(interpolator);
        replayAnimator.setStartDelay(0);
        replayAnimator.setDuration(duration);

    }

    @OnClick(R.id.next_fab)
    public void onNextClick(View view)
    {
    }

    @OnTouch(R.id.replay_fab)
    public boolean onReplayTouch(View view, MotionEvent event)
    {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                replayAnimator.start();
                return true;
            case MotionEvent.ACTION_UP:
                if (mOnReplayClicked != null)
                {
                    mOnReplayClicked.onReplayClick();
                }
                return true;
            default:
                return false;
        }
    }

    @OnClick(R.id.prev_fab)
    public void onPrevClick(View view)
    {

    }

    public void setOnReplayClickedListener(OnReplayClicked onReplayClicked)
    {
        mOnReplayClicked = onReplayClicked;
    }

    public interface OnReplayClicked
    {
        void onReplayClick();
    }
}
