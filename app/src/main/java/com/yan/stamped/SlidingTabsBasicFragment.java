package com.yan.stamped;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * to display a custom {@link android.support.v4.view.ViewPager} title strip which gives continuous feedback to the user
 * when scrolling.
 */
public class SlidingTabsBasicFragment extends Fragment {

    static final String LOG_TAG = "SlidingTabsBasicFragment";
    SchemesFragment SCHEMES = new SchemesFragment();
    private FragmentActivity myContext;
    ImageAdapter imageadapt;
    DatabaseHandler db;
    UserFunctions userFunctions;
    ArrayList<String> availableRewards;
    List<Map<String , String>> availableRewardsExtra;
    //ArrayAdapter<String> arrayAdapter;
    RewardAdapter rewardAdapter;
    Typeface font;

    /**
     * A custom {@link android.support.v4.view.ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link android.support.v4.view.ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    private GridView myGrid;

    /**
     * Inflates the {@link android.view.View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imageadapt = new ImageAdapter(getActivity());
        rewardAdapter = new RewardAdapter(getActivity());
        db = new DatabaseHandler(getActivity());
        userFunctions = new UserFunctions();
        availableRewards = new ArrayList<String>();
        availableRewards = db.getAvailableRewards();
        availableRewardsExtra = db.getRewards();
        font = Typeface.createFromAsset( getActivity().getApplicationContext().getAssets(), "fontawesome-webfont.ttf" );
        //arrayAdapter = new ArrayAdapter<String>(getActivity());
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    // BEGIN_INCLUDE (fragment_onviewcreated)
    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     *
     * We set the {@link ViewPager}'s adapter to be an instance of {@link SamplePagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)
    }
    // END_INCLUDE (fragment_onviewcreated)

    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 3;
        }
        public String[] pages = new String[]{"PROFILE", "MY SCHEMES", "REWARDS"};

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return pages[position];
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            FragmentManager fragManager = myContext.getSupportFragmentManager();
            View view;
            switch(position) {
                case 0:
                    imageadapt.reload();
                    imageadapt.addQuick();
                    imageadapt.notifyDataSetChanged();
                    rewardAdapter.notifyDataSetChanged();
                    view = getActivity().getLayoutInflater().inflate(R.layout.brofile_fragment,
                            container, false);
                    container.addView(view);
                    TextView icon2 = (TextView) container.getRootView().findViewById(R.id.stampscore_icon);
                    TextView icon3 = (TextView) container.getRootView().findViewById(R.id.availrewards_icon);
                    TextView fullname = (TextView) container.getRootView().findViewById(R.id.username);

                    fullname.setText(db.getUserDetails().get("Username"));
                    icon2.setTypeface(font);
                    icon3.setTypeface(font);
                    TextView stampScore = (TextView) container.getRootView().findViewById(R.id.stamp_score);
                    stampScore.setText(db.getStampScore().toString());
                    TextView availtext = (TextView) container.getRootView().findViewById(R.id.avail_text);
                    availtext.setText(availableRewards.size()+ "");
                    break;
                case 1:
                   // fragManager.beginTransaction().add(R.id.viewpager, SCHEMES).commit();
                   // view = SCHEMES.getView();
                    view = getActivity().getLayoutInflater().inflate(R.layout.schemes_fragment,
                            container, false);
                    INITIATE(view);
                    imageadapt.reload();
                    rewardAdapter.notifyDataSetChanged();
                    imageadapt.notifyDataSetChanged();
                    imageadapt.addQuick();
                    myGrid.invalidate();
                    container.addView(view);
                    break;
                case 2:
                    imageadapt.reload();
                    imageadapt.addQuick();
                    imageadapt.notifyDataSetChanged();
                    view = getActivity().getLayoutInflater().inflate(R.layout.rewards_fragment,
                        container, false);
                    myGrid.invalidate();
                    container.addView(view);
                    INITIATEREWARDS(container);
                    rewardAdapter.notifyDataSetChanged();
                    break;
                default:
                    view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                            container, false);
                    container.addView(view);
            }
            // Retrieve a TextView from the inflated View, and update it's text
            // title = (TextView) view.findViewById(R.id.item_title);
            //title.setText(String.valueOf(position + 1));

            Log.i(LOG_TAG, "instantiateItem() [position: " + position + "]");

            // Return the View
            return view;
        }

        public void INITIATEREWARDS(View v) {
            ListView rewardlist = (ListView) v.getRootView().findViewById(R.id.listView2);
            rewardlist.setAdapter(null);
            availableRewards = db.getAvailableRewards();
            availableRewardsExtra = db.getRewards();
            //arrayAdapter = new ArrayAdapter<String>(v.getRootView().getContext(), android.R.layout.simple_list_item_1, availableRewards);

            rewardlist.setAdapter(rewardAdapter);

            rewardlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(final AdapterView<?> parent, View v,
                                        int position, long id) {
                    Log.e("rewardmessage", "." + availableRewardsExtra.get(position).get("SchemeID") + "." + availableRewardsExtra.get(0).get("Cost"));
                    userFunctions.setRewardMessage("|" + availableRewardsExtra.get(position).get("SchemeID") + "|" + availableRewardsExtra.get(0).get("Cost"));
                    //b.putString("name", imageadapt.getName(position)); //Your id
                    //b.putInt("stamps", imageadapt.getStampCount(position));
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialoglayout = inflater.inflate(R.layout.alert, null);
                    TextView title = (TextView) dialoglayout.findViewById(R.id.reward_name);
                    TextView desc = (TextView) dialoglayout.findViewById(R.id.reward_description);
                    TextView cost = (TextView) dialoglayout.findViewById(R.id.reward_cost);
                    Button cancel = (Button) dialoglayout.findViewById(R.id.reward_cancel);


                    title.setText(availableRewardsExtra.get(position).get("Name").split("\\: ")[1]);
                    desc.setText(availableRewardsExtra.get(position).get("Description"));
                    cost.setText(availableRewardsExtra.get(position).get("Cost")+" stamps");

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(dialoglayout);
                    builder.setCancelable(false);

                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                         // userFunctions.setRewardMessage("");
                        }
                    });

                    final AlertDialog dialog = builder.create();
                    userFunctions.addAlert(dialog);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("test","clearing");
                           userFunctions.dismissAllDialogs();
                        }
                    });

                    dialog.show();
                }
            });
        }

        public void INITIATE(View v){
            myGrid = (GridView) v.findViewById(R.id.gweed);
            imageadapt.reload();
            imageadapt.notifyDataSetChanged();
            imageadapt.addQuick();
            myGrid.setAdapter(null);
            myGrid.setAdapter(imageadapt);

            myGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    //Toast.makeText(getActivity(),
                    //        imageadapt.getName(position) + imageadapt.getStampCount(position), Toast.LENGTH_SHORT).show();
                    //Intent scheme = new Intent(getActivity(), Scheme.class);
                    //Bundle b = new Bundle();
                    //b.putString("name", imageadapt.getName(position)); //Your id
                    //b.putInt("stamps",  imageadapt.getStampCount(position));
                    //scheme.putExtras(b); //Put your id to your next Intent
                    //startActivity(scheme);

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialoglayout = inflater.inflate(R.layout.scheme_details, null);
                    TextView title = (TextView) dialoglayout.findViewById(R.id.reward_name);
                    TextView cost = (TextView) dialoglayout.findViewById(R.id.reward_text);
                    GridView gGrid = (GridView) dialoglayout.findViewById(R.id.stamp_grid);


                    title.setText(imageadapt.getName(position));
                    //desc.setText(availableRewardsExtra.get(position).get("Description"));

                    cost.setText(db.getRewards().get(position).get("Cost") +" Stamps - "+db.getRewards().get(position).get("Name").split("\\: ")[1]);
                    gGrid.setAdapter(new ImageAdapter2(getActivity(),Integer.parseInt(db.getRewards().get(position).get("Cost")),imageadapt.getStampCount(position)));

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(dialoglayout);
                    final AlertDialog dialog = builder.create();
                    userFunctions.addAlert(dialog);
                    dialog.show();
                }
            });


        }
        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
        }
    }
}