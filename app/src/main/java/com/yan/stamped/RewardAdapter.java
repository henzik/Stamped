package com.yan.stamped;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Henrik on 2/19/2015.
 */
public class RewardAdapter extends BaseAdapter {

    private ArrayList<String> availableRewards;
    private ArrayList<Integer> availableCosts;
    private Context mContext;
    private DatabaseHandler db;

    public RewardAdapter(Context c) {
        mContext = c;
        db = new DatabaseHandler(c.getApplicationContext());
        availableRewards = db.getAvailableRewards();
        availableCosts = db.getRewardsCosts();
    }

    @Override
    public int getCount() {
        return availableCosts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getText(int position) {
        return availableRewards.get(position).split("\\:")[1];
    }

    public String getSchemeName(int position) {
        return availableRewards.get(position).split("\\:")[0];
    }

    public String getCost(int position) {
        return availableCosts.get(position).toString();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_adapter_view, parent, false);

        TextView mainLine = (TextView) convertView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) convertView.findViewById(R.id.secondLine);
        ImageView image = (ImageView) convertView.findViewById(R.id.icon);

        mainLine.setText(getSchemeName(position)+":"+getText(position));
        secondLine.setText(getCost(position) + " Stamps" );
        image.setImageResource(R.drawable.ic_coffee);

        return convertView;
    }
}
