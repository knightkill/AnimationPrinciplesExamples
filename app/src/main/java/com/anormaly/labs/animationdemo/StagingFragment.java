package com.anormaly.labs.animationdemo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StagingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StagingFragment extends Fragment
{

    private float x = Float.MAX_VALUE;
    private float y = Float.MAX_VALUE;


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_staging, container, false);
    }

}
