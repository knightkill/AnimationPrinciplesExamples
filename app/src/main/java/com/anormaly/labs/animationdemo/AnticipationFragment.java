package com.anormaly.labs.animationdemo;


import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AnticipationFragment extends Fragment implements IndexActivity.OnClickListener
{private static final String TAG = AnticipationFragment.class.getSimpleName();

    @BindView(R.id.box)
    View box;

    private float x = Float.MAX_VALUE;
    private float y = Float.MAX_VALUE;
    private View mView;

    public AnticipationFragment()
    {
        // Required empty public constructor
    }

    public static AnticipationFragment newInstance(float x, float y)
    {
        AnticipationFragment fragment = new AnticipationFragment();
        Bundle bundle = new Bundle();
        bundle.putFloat(IndexActivity.BOX_POSITION_X, x);
        bundle.putFloat(IndexActivity.BOX_POSITION_Y, y);
        fragment.setArguments(bundle);
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
        View view = inflater.inflate(R.layout.fragment_anticipation, container, false);
        ButterKnife.bind(this, view);
        mView = view;

        // Inflate the layout for this fragment
        return view;
    }

    private void animateToPositionIfNot()
    {

        if(x != Float.MAX_VALUE || y != Float.MAX_VALUE){
            float temp_x = box.getX();
            float temp_y = box.getY();
            box.setX(x);
            box.setY(y);
            box.animate().x(temp_x).y(temp_y).setDuration(500);
        }
    }

    private void log(String tag,View view)
    {
        Log.d(TAG + " ||||||" + tag,
                "For Y: \n"
                        + view.getScaleY() + "\n"
                        + view.getPivotY() + "\n"
                        + view.getY() + "\n"
                        + view.getTranslationY() + "\n"
                        + view.getRotationY() + "\n"
                        + view.getHeight() + "\n"
                        + view.getMeasuredHeight()
        );
        Log.d(TAG+ " ||||||" + tag,
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        ((IndexActivity) getActivity()).setOnClickListener(this);
    }

    @Override
    public void onReplayClick()
    {
        //box.setRotationY(20);
        log("replay",box);
    }

    @Override
    public void onNextClick()
    {

    }

    @Override
    public void onPrevClick()
    {
        ActivityUtils.addFragmentToActivity(getFragmentManager(), SquashAndStretchFragment.newInstance(), R.id.content_view);
    }
}
