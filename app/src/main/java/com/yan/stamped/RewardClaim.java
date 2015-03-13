package com.yan.stamped;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;


public class RewardClaim extends ActionBarActivity {

    ImageView thumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_claim);
        getSupportActionBar().hide();
        thumb = (ImageView) findViewById(R.id.imageView2);
        String url = "http://www.clker.com/cliparts/9/f/X/3/W/X/black-wifi-icon-hi.png";
        Picasso.with(getApplicationContext()).load(url).into(thumb);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reward_claim, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
