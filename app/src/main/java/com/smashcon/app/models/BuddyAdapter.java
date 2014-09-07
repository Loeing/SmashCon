package com.smashcon.app.models;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Ari on 9/6/2014.
 */
public class BuddyAdapter extends ArrayAdapter<ParseUser> {
    public BuddyAdapter(Context c, List<ParseUser> users) {
        super(c, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    }
}
