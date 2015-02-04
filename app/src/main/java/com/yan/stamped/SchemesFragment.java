package com.yan.stamped;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Henrik on 16/01/2015.
 */
public class SchemesFragment extends Fragment{
    static final String[] numbers = new String[] {
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};
    private GridView myGrid;
    private View vee;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("EEE","EEE");


        return inflater.inflate(R.layout.schemes_fragment, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = this.getView();
        myGrid = (GridView) v.findViewById(R.id.gweed);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplication(),
                android.R.layout.simple_list_item_1, numbers);

        myGrid.setAdapter(adapter);

        myGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(),
                        ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
            }
        });

       // Log.i("EEE",myGrid);

    }
}
