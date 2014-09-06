package com.smashcon.app.fragments.authorization;



import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.smashcon.app.MainActivity;
import com.smashcon.app.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class SignupFragment extends Fragment {

    private EditText email, username, password;
    private Button signupButton;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        email = (EditText) getView().findViewById(R.id.email_field);
        username = (EditText) getView().findViewById(R.id.username_field);
        password = (EditText) getView().findViewById(R.id.password_field);

        signupButton = (Button) getView().findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    /**
     * Register the user
     */
    public void signUp() {

        ParseUser user = new ParseUser();

        user.setUsername(username.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Registering...");
        pd.setMessage("Please wait while we sign you up.");
        pd.show();

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                pd.dismiss();

                if(e == null) {

                    Intent gotoMainIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(gotoMainIntent);
                }

                else {
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setTitle("Sign Up Error");
                    adb.setPositiveButton("Okay", null);

                    if(e.getCode() == ParseException.USERNAME_TAKEN)
                        adb.setMessage("Sorry, the specified username already exists.");
                    else
                        adb.setMessage("Sorry, one or more of the fields you entered was invalid.");

                    adb.show();
                }

            }
        });
    }
}
