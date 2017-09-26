package com.anormaly.labs.animationdemo;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.anormaly.labs.animationdemo.animationprinciples.anticipation.AnticipationFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StagingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StagingFragment extends Fragment implements IndexActivity.OnClickListener
{

    private static final String TAG = StagingFragment.class.getSimpleName();


    private float x = Float.MAX_VALUE;
    private float y = Float.MAX_VALUE;
    private float x1 = Float.MAX_VALUE;
    private float y1 = Float.MAX_VALUE;
    private float x2 = Float.MAX_VALUE;
    private float y2 = Float.MAX_VALUE;
    private View mView;

    @BindView(R.id.box)
    View boxMain;

    @BindView(R.id.box_left)
    View boxLeft;

    @BindView(R.id.box_right)
    View boxRight;
    private boolean firstTime = true;

    private boolean isStagingStage = false;


    public StagingFragment()
    {
        // Required empty public constructor
    }

    public static StagingFragment newInstance(float x, float y)
    {
        StagingFragment fragment = new StagingFragment();
        Bundle args = new Bundle();
        args.putFloat(IndexActivity.BOX_POSITION_X, x);
        args.putFloat(IndexActivity.BOX_POSITION_Y, y);
        fragment.setArguments(args);
        return fragment;
    }

    public static StagingFragment newInstance(float x, float y, float x1, float y1, float x2, float y2)
    {
        StagingFragment fragment = new StagingFragment();
        Bundle args = new Bundle();
        args.putFloat(IndexActivity.BOX_POSITION_X, x);
        args.putFloat(IndexActivity.BOX_POSITION_Y, y);
        args.putFloat(IndexActivity.BOX_POSITION_X1, x1);
        args.putFloat(IndexActivity.BOX_POSITION_Y1, y1);
        args.putFloat(IndexActivity.BOX_POSITION_X2, x2);
        args.putFloat(IndexActivity.BOX_POSITION_Y2, y2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        ((IndexActivity) getActivity()).setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            x = getArguments().getFloat(IndexActivity.BOX_POSITION_X, Float.MAX_VALUE);
            y = getArguments().getFloat(IndexActivity.BOX_POSITION_Y, Float.MAX_VALUE);
            x1 = getArguments().getFloat(IndexActivity.BOX_POSITION_X1, Float.MAX_VALUE);
            y1 = getArguments().getFloat(IndexActivity.BOX_POSITION_Y1, Float.MAX_VALUE);
            x2 = getArguments().getFloat(IndexActivity.BOX_POSITION_X2, Float.MAX_VALUE);
            y2 = getArguments().getFloat(IndexActivity.BOX_POSITION_Y2, Float.MAX_VALUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_staging, container, false);
        return view;
    }


    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState)
    {
        super.onInflate(context, attrs, savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                if (firstTime == true)
                {
                    firstTime = false;
                    boxMain.setPivotY(boxMain.getHeight());
                    boxMain.setPivotX(boxMain.getWidth() / 2);
                    if (x != Float.MAX_VALUE || y != Float.MAX_VALUE)
                    {
                        if(x1 == Float.MAX_VALUE || y1 == Float.MAX_VALUE||x2 == Float.MAX_VALUE || y2 == Float.MAX_VALUE)
                        {
                            boxMain.setScaleY(2f);
                            boxMain.setScaleX(2f);
                            boxMain.setY(y - boxMain.getHeight());
                            boxMain.setX(x - boxMain.getWidth() / 2);
                            boxRight.setAlpha(0f);
                            boxLeft.setAlpha(0f);
                            log(getView());
                        }

                        List<Animator> animators = new ArrayList<>();
                        animators.add(ObjectAnimator.ofFloat(boxMain, "x", getView().getWidth() / 2 - boxMain.getWidth() / 2));
                        animators.add(ObjectAnimator.ofFloat(boxMain, "y", getView().getHeight() / 2 - boxMain.getHeight() / 2));
                        animators.add(ObjectAnimator.ofFloat(boxMain, "scaleX", 1f));
                        animators.add(ObjectAnimator.ofFloat(boxMain, "scaleY", 1f));
                        if (x1 == Float.MAX_VALUE || y1 == Float.MAX_VALUE)
                            animators.add(ObjectAnimator.ofFloat(boxLeft, "alpha", 1f));
                        if (x2 == Float.MAX_VALUE || y2 == Float.MAX_VALUE)
                            animators.add(ObjectAnimator.ofFloat(boxRight, "alpha", 1f));


                        AnimatorSet boxMainInitialAnimation = new AnimatorSet();
                        boxMainInitialAnimation.playTogether(
                                animators
                        );
                        boxMainInitialAnimation.start();
                    }
                }
            }
        });
    }

    @Override
    public void onReplayClick()
    {

        if (!isStagingStage)
        {
            isStagingStage = true;
            final AnimatorSet replayAnimator = new AnimatorSet();
            replayAnimator.playTogether(

                    ObjectAnimator.ofFloat(boxMain, "scaleX", 2f),
                    ObjectAnimator.ofFloat(boxMain, "scaleY", 2f),
                    ObjectAnimator.ofFloat(boxMain, "elevation", 3),
                    ObjectAnimator.ofArgb(boxMain, "backgroundColor", ContextCompat.getColor(getContext(), R.color.colorPrimaryDark), 0xff0849E6),
                    ObjectAnimator.ofFloat(boxMain, "translationY", boxMain.getHeight() / 2),
                    ObjectAnimator.ofFloat(boxMain, "rotationX", 10),
                    ObjectAnimator.ofFloat(boxRight, "alpha", 0.5f),
                    ObjectAnimator.ofFloat(boxLeft, "alpha", 0.5f),
                    ObjectAnimator.ofFloat(boxRight, "scaleX", 0.5f),
                    ObjectAnimator.ofFloat(boxRight, "scaleY", 0.5f),
                    ObjectAnimator.ofFloat(boxLeft, "scaleX", 0.5f),
                    ObjectAnimator.ofFloat(boxLeft, "scaleY", 0.5f)
            );
            replayAnimator.setDuration(350);
            replayAnimator.start();
        } else
        {
            isStagingStage = false;
            AnimatorSet replayAnimator = new AnimatorSet();
            replayAnimator.playTogether(
                    ObjectAnimator.ofFloat(boxMain, "scaleX", 1f),
                    ObjectAnimator.ofFloat(boxMain, "scaleY", 1f),
                    ObjectAnimator.ofFloat(boxMain, "elevation", 0),
                    ObjectAnimator.ofArgb(boxMain, "backgroundColor", 0xff0849E6, ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)),
                    ObjectAnimator.ofFloat(boxMain, "translationY", 0),
                    ObjectAnimator.ofFloat(boxMain, "rotationX", 0),
                    ObjectAnimator.ofFloat(boxRight, "alpha", 1f),
                    ObjectAnimator.ofFloat(boxLeft, "alpha", 1f),
                    ObjectAnimator.ofFloat(boxRight, "scaleX", 1f),
                    ObjectAnimator.ofFloat(boxRight, "scaleY", 1f),
                    ObjectAnimator.ofFloat(boxLeft, "scaleX", 1f),
                    ObjectAnimator.ofFloat(boxLeft, "scaleY", 1f)
            );
            replayAnimator.setDuration(350);
            replayAnimator.start();
        }
    }

    @Override
    public void onNextClick()
    {
        if (isStagingStage)
        {
            onReplayClick();
        } else
        {
            boxMain.setPivotX(boxMain.getWidth() / 2);
            boxMain.setPivotY(boxMain.getHeight() / 2);
            ActivityUtils.addFragmentToActivity(getFragmentManager(), ArcFragment.newInstance(boxMain.getX(), boxMain.getY()), R.id.content_view);
        }
    }

    @Override
    public void onPrevClick()
    {
        if (isStagingStage)
        {
            onReplayClick();
        } else
        {
            ActivityUtils.addFragmentToActivity(getFragmentManager(), AnticipationFragment.newInstance(boxMain.getX() - boxMain.getWidth() / 2, boxMain.getY() - boxMain.getHeight(), boxMain.getWidth(), boxMain.getHeight()), R.id.content_view);
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
