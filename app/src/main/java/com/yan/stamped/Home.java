package com.yan.stamped;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.os.StrictMode.*;


public class Home extends ActionBarActivity {

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;
    private String email;
    GridLayout myGrid;
    View myview;
    UserFunctions userFunctions = new UserFunctions();

    private static final String KEY_Length = "schemes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userFunctions = new UserFunctions();
        if (userFunctions.isUserLoggedIn(getApplicationContext())) {
            setContentView(R.layout.activity_home);
            userFunctions.sync(getApplicationContext());
            if (savedInstanceState == null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
                transaction.replace(R.id.sample_content_fragment, fragment);
                transaction.commit();
            }
            //nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,null);
        } else {
            Intent login = new Intent(getApplicationContext(), Login.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            finish();

        }
        //ViewGroup rootView  = inflater.inflate(R.layout.scheme_item, null);

    }@Override
     public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_sync:
                Toast.makeText(getApplicationContext(),
                        "Synching Stampbook", Toast.LENGTH_SHORT).show();
                userFunctions.sync(getApplicationContext());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}