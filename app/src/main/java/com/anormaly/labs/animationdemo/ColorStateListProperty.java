package com.anormaly.labs.animationdemo;

import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.util.Property;

/**
 * Created by designfreak on 23/09/17.
 */

public class ColorStateListProperty extends Property<FloatingActionButton,Integer>
{

    private static final String TAG = ColorStateListProperty.class.getSimpleName();
    private ColorStateList mColorStateList;

    /**
     * A constructor that takes an identifying name and {@link #getType() type} for the property.
     *
     * @param name
     */
    public ColorStateListProperty(String name)
    {
        super(Integer.class, name);
    }

    @Override
    public Integer get(FloatingActionButton floatingActionButton)
    {
        Log.d(TAG, "get: ");
        mColorStateList = floatingActionButton.getBackgroundTintList();
        return floatingActionButton.getBackgroundTintList().getDefaultColor();

    }

    @Override
    public void set(FloatingActionButton floatingActionButton, Integer value)
    {
        //floatingActionButton.setBackgroundTintList(new ColorStateList(floatingActionButton);
    }
}
