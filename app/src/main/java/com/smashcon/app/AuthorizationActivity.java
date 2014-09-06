package com.smashcon.app;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.smashcon.app.fragments.authorization.LoginFragment;
import com.smashcon.app.fragments.authorization.SignupFragment;
import com.smashcon.app.implementations.TabListener;


public class AuthorizationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        ActionBar.Tab signupTab = getActionBar().newTab()
                .setText("Signup")
                .setTabListener(new TabListener<SignupFragment>(this, "signup",
                        SignupFragment.class));

        ActionBar.Tab loginTab = getActionBar().newTab()
                .setText("Login")
                .setTabListener(new TabListener<LoginFragment>(this, "login",
                        LoginFragment.class));
        getActionBar().addTab(loginTab);

        getActionBar().addTab(signupTab);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.authorization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

}
