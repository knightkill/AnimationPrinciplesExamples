package com.anormaly.labs.animationdemo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


public class AnticipationFragment extends Fragment
{
    public AnticipationFragment()
    {
        // Required empty public constructor
    }

    public static AnticipationFragment newInstance(String param1, String param2)
    {
        AnticipationFragment fragment = new AnticipationFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_anticipation, container, false);
        ButterKnife.bind(this,view);
        // Inflate the layout for this fragment
        return view;
    }

}
