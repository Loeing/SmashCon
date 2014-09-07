package com.smashcon.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.smashcon.app.fragments.main.BeamFragment;
import com.smashcon.app.fragments.main.BuddiesFragment;
import com.smashcon.app.implementations.TabListener;
import com.smashcon.app.models.UserProperty;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "YSMCpaGhIeeFsNVUg76zle8fvRYR2pbQqNQ40Yuo",
                "7a71TIj1KWpV8oMCccFyFFske1NXuZTl98pWleZ2");

        ParseFacebookUtils.initialize("717252155013016");

        configureTabs();
        //getAppKeyHash();
    }

    private void configureTabs() {
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab beamTab = getActionBar().newTab()
                .setText("Beam")
                .setTabListener(new TabListener<BeamFragment>(this, "beam", BeamFragment.class));

        /*ActionBar.Tab settingsTab = getActionBar().newTab()
                .setText("Settings")
                .setTabListener(new TabListener<SettingsFragment>(this, "settings",
                        SettingsFragment.class));*/

        ActionBar.Tab buddiesTab = getActionBar().newTab()
                .setText("Buddies")
                .setTabListener(new TabListener<BuddiesFragment>(this, "buddies",
                        BuddiesFragment.class));

        getActionBar().addTab(beamTab);
        //getActionBar().addTab(settingsTab);
        getActionBar().addTab(buddiesTab);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
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
        final ParseUser user = ParseUser.getCurrentUser();

        final Activity thisActivity = this;

        if(user == null) {
            gotoAuthPage();
        }

        else {
            if(!ParseFacebookUtils.isLinked(user)) {
                AlertDialog.Builder fbChoose = new AlertDialog.Builder(thisActivity);
                fbChoose.setTitle("Facebook Connect")
                        .setMessage("Would you like to connect your Facebook account?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ParseFacebookUtils.link(user, thisActivity, new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        ParseFacebookUtils.getSession().requestNewReadPermissions(
                                                new Session.NewPermissionsRequest(
                                                        thisActivity, "public_profile",
                                                        "email", "user_friends", "user_location"));
                                        getUserDataFromFacebook();
                                    }
                                });
                            }
                        });
                fbChoose.create().show();

            }
            else {
                getUserDataFromFacebook();
            }
        }
    }

    private void getUserDataFromFacebook() {
        Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        ParseUser curUser = ParseUser.getCurrentUser();
                        curUser.put(UserProperty.FIRST_NAME.toString(),
                                user.getFirstName());
                        curUser.put(UserProperty.LAST_NAME.toString(),
                                user.getLastName());
                        curUser.put("key", "value");

                        curUser.saveInBackground();
                    }
                }).executeAsync();
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

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;

                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", something);
            }
        }
        catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            Log.e("name not found", e1.toString());
        }

        catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            Log.e("no such an algorithm", e.toString());
        }
        catch (Exception e){
            Log.e("exception", e.toString());
        }

    }

}
