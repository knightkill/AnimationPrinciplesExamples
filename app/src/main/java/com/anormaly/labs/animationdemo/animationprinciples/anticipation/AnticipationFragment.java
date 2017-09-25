package com.anormaly.labs.animationdemo.animationprinciples.anticipation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anormaly.labs.animationdemo.ActivityUtils;
import com.anormaly.labs.animationdemo.IndexActivity;
import com.anormaly.labs.animationdemo.R;
import com.anormaly.labs.animationdemo.SquashAndStretchFragment;
import com.anormaly.labs.animationdemo.StagingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AnticipationFragment extends Fragment implements IndexActivity.OnClickListener
{private static final String TAG = AnticipationFragment.class.getSimpleName();

    @BindView(R.id.box)
    AnticipationView box;

    private float x = Float.MAX_VALUE;
    private float y = Float.MAX_VALUE;
    private View mView;
    private int width;
    private int height;

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
    public static AnticipationFragment newInstance(float x, float y,int width,int height)
    {
        AnticipationFragment fragment = new AnticipationFragment();
        Bundle bundle = new Bundle();
        bundle.putFloat(IndexActivity.BOX_POSITION_X, x);
        bundle.putFloat(IndexActivity.BOX_POSITION_Y, y);
        bundle.putInt(IndexActivity.BOX_WIDTH,width);
        bundle.putInt(IndexActivity.BOX_HEIGHT,height);
        fragment.setArguments(bundle);
        return fragment;
    }


    /**
     * Retrieves the argument
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            x = getArguments().getFloat(IndexActivity.BOX_POSITION_X, Float.MAX_VALUE);
            y = getArguments().getFloat(IndexActivity.BOX_POSITION_Y, Float.MAX_VALUE);
            width = getArguments().getInt(IndexActivity.BOX_WIDTH,Integer.MAX_VALUE);
            height = getArguments().getInt(IndexActivity.BOX_HEIGHT,Integer.MAX_VALUE);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        animateToPositionIfNot();
    }

    private void animateToPositionIfNot()
    {

        if(x != Float.MAX_VALUE || y != Float.MAX_VALUE){
            if(width!=Integer.MAX_VALUE || height!=Integer.MAX_VALUE)
                box.setXY(x,y,width,height);
            else
                box.setXY(x,y);
        }
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
        box.onReplayClick();
    }

    @Override
    public void onNextClick()
    {
        ActivityUtils.addFragmentToActivity(getFragmentManager(),StagingFragment.newInstance(box.getBoxX(),box.getBoxY()),R.id.content_view);
    }

    @Override
    public void onPrevClick()
    {
        box.onPrevClick();
        ActivityUtils.addFragmentToActivity(getFragmentManager(), SquashAndStretchFragment.newInstance(), R.id.content_view);
    }


}
