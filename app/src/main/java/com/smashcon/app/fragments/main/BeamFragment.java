package com.smashcon.app.fragments.main;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.smashcon.app.R;
import com.smashcon.app.models.UserProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class BeamFragment extends Fragment {

    private TextView userFullname;
    private ImageView profilePic;
    private Spinner conChooser;

    public BeamFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        userFullname = (TextView) getView().findViewById(R.id.user_fullname);
        profilePic = (ImageView) getView().findViewById(R.id.profile_pic);
        conChooser = (Spinner) getView().findViewById(R.id.con_chooser);

        final ParseUser user = ParseUser.getCurrentUser();

        if(user != null) {
            userFullname.setText(String.format("%s %s",
                    user.getString(UserProperty.FIRST_NAME.toString()),
                    user.getString(UserProperty.LAST_NAME.toString())));

            ParseFile profilePicture = (ParseFile) user.get("profilePic");
            if(profilePicture != null) {
                profilePicture.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        Bitmap bmf = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        profilePic.setImageBitmap(bmf);
                    }
                });
            }

            ParseQuery<ParseObject> conQuery = ParseQuery.getQuery("Convention");
            conQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    List<CharSequence> conventionNames = new ArrayList<CharSequence>();
                    for (ParseObject convention : parseObjects)
                        conventionNames.add(convention.getString("name"));

                    ArrayAdapter<CharSequence> spinnerItems = new ArrayAdapter<CharSequence>
                            (getActivity(),
                            R.layout.spinner_template, conventionNames);

                    spinnerItems.setDropDownViewResource(
                            R.layout.spinner_dropdown_template);

                    conChooser.setAdapter(spinnerItems);

                    Object convention = user.get(UserProperty.CONVENTION.toString());
                    if(convention != null) {
                        conChooser.setSelection(spinnerItems.getPosition(convention.toString()));
                    }

                    conChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                            user.put(UserProperty.CONVENTION.toString(),
                                    adapterView.getItemAtPosition(pos).toString());
                            user.saveInBackground();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            // Do nothing
                        }
                    });
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beam, container, false);
    }


}
