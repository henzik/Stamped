package com.yan.stamped;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by Henrik on 26/01/2015.
 */
public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private DatabaseHandler db;

        public ImageAdapter(Context c) {
            mContext = c;
            db = new DatabaseHandler(mContext);
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            LinearLayout LL;
            TextView tv;
            if (convertView == null) {  // if it's not recycled, initialize some attribute
                LL = new LinearLayout(mContext);
                LL.setOrientation(LinearLayout.VERTICAL);
                LL.setBackgroundColor(Color.MAGENTA);
                LL.setMinimumHeight(230);
                LL.setGravity(Gravity.BOTTOM);
                imageView = new ImageView(mContext);
                tv = new TextView(mContext);
                tv.setText("DDD");
                tv.setGravity(Gravity.CENTER);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setBackgroundColor(Color.argb(70,120,64,64));
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
                //LL.addView(imageView);
                LL.addView(tv);
            } else {
                LL = (LinearLayout)convertView;
                //imageView = (ImageView) convertView;
            }

            //imageView.setImageResource(mThumbIds[position]);
            return LL;
        }
    private String[] mThumbIds = {"1","2"};
}

