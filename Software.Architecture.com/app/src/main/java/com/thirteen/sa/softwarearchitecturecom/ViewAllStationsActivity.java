package com.thirteen.sa.softwarearchitecturecom;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class ViewAllStationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllStationsListFragment myFrag = new AllStationsListFragment();
        myFrag.setStations(getIntent().getStringArrayListExtra("ADAPTER"));
        setContentView(R.layout.activity_view_all_stations);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, myFrag)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_all_stations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**
     * A fragment consisting of a list view of all stations.
     */
    public static class AllStationsListFragment extends Fragment {


        private ArrayAdapter<String> stationNameAdapter;
        private ArrayList<String> stations;
        private final String LOG_TAG = AllStationsListFragment.class.getSimpleName();

        public AllStationsListFragment() {
        }

        public void setStations(ArrayList<String> pStations)
        {
            stations = pStations;
        }

        @Override
        public void onStart() {
            super.onStart();

        }

        private void initStationAdapter() {

            for (String current_stop : stations)
            {
                stationNameAdapter.add(current_stop);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_view_all_stations, container, false);

            // First, we'll get all stations and populate our list
            Activity currentActivity = getActivity();

            stationNameAdapter = new ArrayAdapter<String>(currentActivity,
                    R.layout.list_item_stations,
                    R.id.list_item_stations_textview,
                    new ArrayList<String>());

            final ListView stationListView = (ListView) rootView.findViewById(R.id.listview_all_stations);
            stationListView.setAdapter(stationNameAdapter);

            initStationAdapter();
            Log.i(LOG_TAG, "Populated array adapter");

            // Now we add an event listener to the input field to filter on keyboard input

            EditText filter_input;
            filter_input = (EditText) rootView.findViewById(R.id.all_stations_filter);
            filter_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    stationNameAdapter.clear();

                    String filter_query = s.toString();

                    if (filter_query != "") {
                        filter_query = filter_query.toUpperCase();
                    }

                    for (String current_stop: stations) {
                        if (current_stop.toUpperCase().startsWith(filter_query)) {
                            stationNameAdapter.add(current_stop);
                        }
                    }

                }
            });

            return rootView;
        }
    }
}
