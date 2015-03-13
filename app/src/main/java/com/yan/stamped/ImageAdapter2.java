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

public class ImageAdapter2 extends BaseAdapter {

    private Context mContext;

    private ArrayList<Integer> mThumbIds;

    public ImageAdapter2(Context c, int req, int curr) {
        mContext = c;
        mThumbIds = new ArrayList<Integer>();

        if(curr >= req) {
            curr = req;
            req = 0;
        }
        else
        {
            req = req - curr;
        }

        for(int i=0;i<curr;i++) {
            mThumbIds.add(R.drawable.ic_stamp);
        }

        for(int i=0;i<req;i++) {
            mThumbIds.add(R.drawable.ic_empty);
        }
    }

    public int getCount() {
        return mThumbIds.size();
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
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds.get(position));
        return imageView;
    }

    // references to our images
}