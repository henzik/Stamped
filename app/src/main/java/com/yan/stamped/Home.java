package com.yan.stamped;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import org.json.JSONException;
import org.json.JSONObject;

import static android.os.StrictMode.*;


public class Home extends ActionBarActivity {

        // Whether the Log Fragment is currently shown
        private boolean mLogShown;
        private String email;
        GridLayout myGrid;
        View myview;

    private static final String KEY_Length = "length";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            UserFunctions userFunctions = new UserFunctions();
            if(userFunctions.isUserLoggedIn(getApplicationContext())) {
                setContentView(R.layout.activity_home);
                sync();
                if (savedInstanceState == null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
                    transaction.replace(R.id.sample_content_fragment, fragment);
                    transaction.commit();
                }
            }
            else {
                Intent login = new Intent(getApplicationContext(), Login.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                // Closing dashboard screen
                finish();
            }
            //ViewGroup rootView  = inflater.inflate(R.layout.scheme_item, null);

        }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_home, menu);
            return super.onCreateOptionsMenu(menu);
        }

    public void sync() {
        new Thread(new Runnable() {
            public void run() {
                UserFunctions userFunction = new UserFunctions();
                String email = userFunction.getUserEmail(getApplicationContext());
                JSONObject json = userFunction.syncSchemes(email);

                try {
                    if (json.getString(KEY_Length) != null) {
                        //loginErrorMsg.setText("");
                        String res = json.getString(KEY_Length);
                        if(Integer.parseInt(res) == 1){
                            // user successfully logged in
                            // Store user details in SQLite Database
                            Log.e("Schemes!",Integer.parseInt(res)+ "");
                            // Clear all previous data in database
                            // userFunction.logoutUser(getApplicationContext());

                            // Launch Dashboard Screen
                        }else{
                            // Error in login
                            Log.e("String", "WE HAVE NOT LOGGED IN");
                            //loginErrorMsg.setText("Incorrect username/password");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
