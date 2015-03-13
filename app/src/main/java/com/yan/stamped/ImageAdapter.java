package com.yan.stamped;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Henrik on 26/01/2015.
 */
public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private DatabaseHandler db;
        private ArrayList<String> mThumbIds;
        private ArrayList<Integer> mStampCounts;

        public ImageAdapter(Context c) {
            mContext = c;
            db = new DatabaseHandler(c.getApplicationContext());
            mThumbIds = db.getSchemeNames();
            mStampCounts = db.getSchemeCurrentStamps();
        }

        public void addQuick() {
            mThumbIds.add("LOL");
            mThumbIds.remove("LOL");
        }

        public int getCount() {
            return mThumbIds.size();
        }

        public void reload() {
            mThumbIds = db.getSchemeNames();
            mStampCounts = db.getSchemeCurrentStamps();
        }

        public Object getItem(int position) {
            return null;
        }

        public String getName(int position) {
            return mThumbIds.get(position);
        }

        public Integer getStampCount (int position) {return mStampCounts.get(position);}

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            CardView LL;
            LinearLayout holder;
            TextView tv;
            TextView st;
            //if (convertView == null) {  // if it's not recycled, initialize some attribute
                LL = (CardView)convertView;
                holder = new LinearLayout(mContext);
                LL = new CardView(mContext);
                holder.setOrientation(LinearLayout.VERTICAL);
                holder.setMinimumHeight(150);
                if(getStampCount(position) >= Integer.parseInt(db.getRewards().get(position).get("Cost"))) {

                    LL.setCardBackgroundColor(Color.rgb(255, 235, 59));
                } else {
                    LL.setCardBackgroundColor(Color.rgb(197, 202, 233));
                }
                LL.setRadius(100);
                LL.setMinimumHeight(150);
                holder.setGravity(Gravity.CENTER);
                imageView = new ImageView(mContext);
                tv = new TextView(mContext);
                tv.setText(getName(position));
                tv.setGravity(Gravity.CENTER);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                //tv.setBackgroundColor(Color.argb(70,120,64,64));

                st = new TextView(mContext);
                st.setText(getStampCount(position).toString());
                //st.setText("0");
                st.setTextSize(30);
                st.setGravity(Gravity.CENTER);
                st.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                st.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                //st.setBackgroundColor(Color.argb(70,120,64,64));

                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
                //LL.addView(imageView);
                holder.addView(st);
                holder.addView(tv);
                LL.addView(holder);
           // } else {
             //   LL = (LinearLayout)convertView;
                //imageView = (ImageView) convertView;
          //  }

            //imageView.setImageResource(mThumbIds[position]);
            return LL;
        }
}

