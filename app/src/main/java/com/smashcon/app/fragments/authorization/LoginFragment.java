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
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.smashcon.app.MainActivity;
import com.smashcon.app.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class LoginFragment extends Fragment {

    private EditText username, password;
    private Button login;
    private ImageButton fbLogin;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        username = (EditText) getView().findViewById(R.id.username_field);
        password = (EditText) getView().findViewById(R.id.password_field);

        login = (Button) getView().findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        fbLogin = (ImageButton) getActivity().findViewById(R.id.fb_login_button);
        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithFb();
            }
        });
    }

    private void loginWithFb() {
        ParseFacebookUtils.logIn(getActivity(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if(parseUser == null) {
                    Toast.makeText(getActivity(),
                            "There is no account associated with this Facebook.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Intent gotoMainIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(gotoMainIntent);
                }
            }
        });
    }

    /**
     * Login the user
     */
    public void login() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Signing in...");
        pd.setMessage("Please wait while we log you in.");
        pd.show();

        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(),
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        pd.dismiss();

                        if(user != null) {

                            Intent gotoMainIntent = new Intent(getActivity(), MainActivity.class);
                            startActivity(gotoMainIntent);
                        }

                        else {
                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                            adb.setTitle("Login Error");
                            adb.setPositiveButton("Okay", null);

                            adb.setMessage("Sorry, your credentials are incorrect.");

                            adb.show();
                        }
                    }
                });
    }
}
