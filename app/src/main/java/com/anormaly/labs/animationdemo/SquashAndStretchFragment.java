package com.anormaly.labs.animationdemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.anormaly.labs.animationdemo.animationprinciples.anticipation.AnticipationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class SquashAndStretchFragment extends Fragment implements IndexActivity.OnClickListener
{

    private static final String TAG = SquashAndStretchFragment.class.getSimpleName();

    @Nullable
    @BindView(R.id.box)
    View box;

    @BindView(R.id.layout)
    ConstraintLayout layout;
    private boolean jumped = false;
    private float x = Float.MAX_VALUE;
    private float y = Float.MAX_VALUE;
    private boolean firstTime = true;

    public SquashAndStretchFragment()
    {
        //Mandatory Empty Constructor.
    }

    public static SquashAndStretchFragment newInstance()
    {
        SquashAndStretchFragment fragment = new SquashAndStretchFragment();
        return fragment;
    }

    public static SquashAndStretchFragment newInstance(float x,float y)
    {
        SquashAndStretchFragment fragment = new SquashAndStretchFragment();
        Bundle bundle = new Bundle();
        bundle.putFloat(IndexActivity.BOX_POSITION_X, x);
        bundle.putFloat(IndexActivity.BOX_POSITION_Y, y);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            x = getArguments().getFloat(IndexActivity.BOX_POSITION_X, Float.MAX_VALUE);
            y = getArguments().getFloat(IndexActivity.BOX_POSITION_Y, Float.MAX_VALUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_squash_and_stretch, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                if(firstTime)
                {
                    firstTime = false;
                    if (x != Float.MAX_VALUE || y != Float.MAX_VALUE)
                    {
                        box.setX(x-box.getWidth()/2);
                        box.setY(y-box.getHeight());
                        box.setPivotY(box.getHeight());
                        box.animate().y(layout.getHeight()-box.getHeight()).x(layout.getWidth()/2-box.getWidth()/2);
                    }
                }
            }
        });
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

        box.setPivotY(box.getHeight());
        if(jumped){
            pushDown();
            jumped = false;
        }else{
            jumpUp();
            jumped = true;
        }

        //log();

    }

    @Override
    public void onNextClick()
    {
        ActivityUtils.addFragmentToActivity(getFragmentManager(), AnticipationFragment.newInstance(box.getX(),box.getY()),R.id.content_view);
    }

    boolean appCloseConfirmation = false;

    @Override
    public void onPrevClick()
    {
        if (appCloseConfirmation) {
            getActivity().finish();
            return;
        }

        this.appCloseConfirmation = true;
        Toast.makeText(getContext(), "Click again to exit this Awesomeness!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                appCloseConfirmation =false;
            }
        }, 2000);
    }

    private void pushDown()
    {
        ObjectAnimator freefallAnimator = ObjectAnimator.ofFloat(box, "y", box.getY(), layout.getHeight()-box.getHeight()).setDuration(700);
        freefallAnimator.setInterpolator(new AccelerateInterpolator(3f));

        AnimatorSet freefallReactor = new AnimatorSet();

        freefallReactor.playTogether(
                ObjectAnimator.ofFloat(box,"scaleY",1f,0.05f),
                ObjectAnimator.ofFloat(box,"scaleX",1f,1.9f)
        );

        freefallReactor.setInterpolator(new FastOutSlowInInterpolator());
        freefallReactor.setDuration(100);

        AnimatorSet boxRetainPosition = new AnimatorSet();

        boxRetainPosition.playTogether(
                ObjectAnimator.ofFloat(box,"scaleY",0.05f,1f),
                ObjectAnimator.ofFloat(box,"scaleX",1.9f,1f)
        );


        boxRetainPosition.setInterpolator(new BounceInterpolator());
        //boxRetainPosition.setInterpolator(new OvershootInterpolator(2f));
        boxRetainPosition.setDuration(500);

        AnimatorSet sequentializeAnimation = new AnimatorSet();

        sequentializeAnimation.playSequentially(freefallAnimator,freefallReactor,boxRetainPosition);

        sequentializeAnimation.start();
    }

    private void jumpUp()
    {

        AnimatorSet boxAnticipate = new AnimatorSet();
        boxAnticipate.playTogether(
                ObjectAnimator.ofFloat(box,"scaleY",1f,0.7f),
                ObjectAnimator.ofFloat(box,"scaleX",1f,1.4f)
        );
        boxAnticipate.setInterpolator(new FastOutLinearInInterpolator());
        boxAnticipate.setDuration(300);

        float boxToHalfLayoutRatio = 1f +  ((layout.getHeight() / 2) / box.getHeight());

        AnimatorSet boxAnticipateReleaseAndElongate = new AnimatorSet();
        boxAnticipateReleaseAndElongate.playTogether(
                ObjectAnimator.ofFloat(box,"scaleY",0.9f,boxToHalfLayoutRatio),
                ObjectAnimator.ofFloat(box,"scaleX",1.3f,0.8f)
        );
        boxAnticipateReleaseAndElongate.setInterpolator(new DecelerateInterpolator(3f));
        boxAnticipateReleaseAndElongate.setDuration(400);
        boxAnticipateReleaseAndElongate.setStartDelay(100);

        AnimatorSet boxOvershootShot = new AnimatorSet();
        boxOvershootShot.playTogether(
                ObjectAnimator.ofFloat(box,"scaleY",boxToHalfLayoutRatio,1f),
                ObjectAnimator.ofFloat(box,"scaleX",0.8f,1f),
                ObjectAnimator.ofFloat(box,"y",box.getY(),(-box.getHeight()/2)+(layout.getHeight()/2))
        );
        boxOvershootShot.setInterpolator(new AnticipateOvershootInterpolator(5f));
        boxOvershootShot.setDuration(200);
        boxOvershootShot.setStartDelay(0);

        AnimatorSet sequentializeAnimation = new AnimatorSet();
        sequentializeAnimation.playSequentially(boxAnticipate,boxAnticipateReleaseAndElongate,boxOvershootShot);
        sequentializeAnimation.setStartDelay(0);
        sequentializeAnimation.start();
    }

    private void log()
    {
        Log.d(TAG,
                "For Y: \n"
                        + box.getScaleY() + "\n"
                        + box.getPivotY() + "\n"
                        + box.getY() + "\n"
                        + box.getTranslationY() + "\n"
                        + box.getRotationY() + "\n"
                        + box.getHeight() + "\n"
                        + box.getMeasuredHeight()
        );
        Log.d(TAG,
                "For X: \n"
                        + box.getScaleX() + "\n"
                        + box.getPivotX() + "\n"
                        + box.getX() + "\n"
                        + box.getTranslationX() + "\n"
                        + box.getRotationX() + "\n"
                        + box.getWidth() + "\n"
                        + box.getMeasuredWidth()
        );
    }
}
