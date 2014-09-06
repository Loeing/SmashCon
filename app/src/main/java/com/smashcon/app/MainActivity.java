package com.smashcon.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseUser;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "YSMCpaGhIeeFsNVUg76zle8fvRYR2pbQqNQ40Yuo",
                "7a71TIj1KWpV8oMCccFyFFske1NXuZTl98pWleZ2");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(ParseUser.getCurrentUser() == null) {
            gotoAuthPage();
        }
    }

    private void gotoAuthPage() {
        Intent startAuthorizationIntent = new Intent(this, AuthorizationActivity.class);

        startActivity(startAuthorizationIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.logout_menu_item:
                ParseUser.logOut();
                gotoAuthPage();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
