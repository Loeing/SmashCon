package com.smashcon.app.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Ari on 9/6/2014.
 */
public class BuddyView extends RelativeLayout {
    public static BuddyView create(ViewGroup parent) {
        return (BuddyView) LayoutInflater.from(parent.getContext()).inflate(R.laypout)
    }
}
