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
        private String[] mThumbIds;
        private Integer[] mStampCounts;

        public ImageAdapter(Context c) {
            mContext = c;
            db = new DatabaseHandler(c.getApplicationContext());
            mThumbIds = db.getSchemeNames();
            mStampCounts = db.getSchemeCurrentStamps();
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public void reload() {
            mThumbIds = db.getSchemeNames();
            mStampCounts = db.getSchemeCurrentStamps();
        }

        public Object getItem(int position) {
            return null;
        }

        public String getName(int position) {
            return mThumbIds[position];
        }

        public Integer getStampCount (int position) {return mStampCounts[position];}

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            LinearLayout LL;
            TextView tv;
            TextView st;
            if (convertView == null) {  // if it's not recycled, initialize some attribute
                LL = new LinearLayout(mContext);
                LL.setOrientation(LinearLayout.VERTICAL);
                LL.setBackgroundColor(Color.argb(100,0,89,45));
                LL.setMinimumHeight(230);
                LL.setGravity(Gravity.BOTTOM);
                imageView = new ImageView(mContext);
                tv = new TextView(mContext);
                tv.setText(getName(position));
                tv.setGravity(Gravity.CENTER);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setBackgroundColor(Color.argb(70,120,64,64));

                st = new TextView(mContext);
                st.setText(getStampCount(position).toString());
                //st.setText("0");
                st.setTextSize(40);
                st.setGravity(Gravity.CENTER);
                st.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                st.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                //st.setBackgroundColor(Color.argb(70,120,64,64));

                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
                //LL.addView(imageView);
                LL.addView(st);
                LL.addView(tv);
            } else {
                LL = (LinearLayout)convertView;
                //imageView = (ImageView) convertView;
            }

            //imageView.setImageResource(mThumbIds[position]);
            return LL;
        }
}

