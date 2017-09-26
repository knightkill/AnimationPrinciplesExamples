package com.anormaly.labs.animationdemo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


public class ArcFragment extends Fragment implements IndexActivity.OnClickListener
{

    private float x;
    private float y;

    public ArcFragment()
    {
        // Required empty public constructor
    }

    public static ArcFragment newInstance(float x,float y)
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
            x = getArguments().getFloat(IndexActivity.BOX_POSITION_X,Float.MAX_VALUE);
            y = getArguments().getFloat(IndexActivity.BOX_POSITION_Y,Float.MAX_VALUE);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_arc, container, false);
        ButterKnife.bind(this,view);
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

    }

    @Override
    public void onNextClick()
    {

    }

    @Override
    public void onPrevClick()
    {

    }
}
