package com.thirteen.sa.softwarearchitecturecom;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class BrowseLatLon extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_lat_lon);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse_lat_lon, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        ArrayList<Stop> myStopList;
        ArrayAdapter<String> myAdap;
        boolean greaterLat;
        boolean greaterLon;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Thread myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    DAO myDao = new DAO(getActivity());
                    myDao.open();

                    myStopList = myDao.getAllStops();
                }
            });
            myThread.start();
            final View rootView = inflater.inflate(R.layout.fragment_browse_lat_lon, container, false);

            greaterLon = true;
            greaterLat = true;

            myAdap = new ArrayAdapter<String>(getActivity(),
                    R.layout.list_item_stations,
                    R.id.list_item_stations_textview,
                    new ArrayList<String>());

            Button myButton = (Button) rootView.findViewById(R.id.SearchLatLon);
            myButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String SearchLat = ((EditText) rootView.findViewById(R.id.lat)).getText().toString();
                        String SearchLon = ((EditText) rootView.findViewById(R.id.lon)).getText().toString();

                        boolean latSet = (SearchLat.length() != 0);
                        boolean lonSet = (SearchLon.length() != 0);
                        myAdap.clear();

                        if (latSet == true && lonSet == true)
                        {
                            for (Stop currentStop : myStopList)
                            {
                                if (stringGreater(SearchLat, currentStop.get_lat()) == greaterLat
                                    && stringGreater(SearchLon, currentStop.get_lon()) == greaterLon) {
                                    myAdap.add(currentStop.get_name());
                                }
                            }
                        }
                        else if (latSet == true && lonSet == false)
                        {
                            for (Stop currentStop : myStopList) {
                                if (stringGreater(SearchLat, currentStop.get_lat()) == greaterLat) {
                                    myAdap.add(currentStop.get_name());
                                }
                            }
                        }
                        else if (latSet == false && lonSet == true)
                        {
                            for (Stop currentStop : myStopList) {
                                if (stringGreater(SearchLon, currentStop.get_lon()) == greaterLon) {
                                    myAdap.add(currentStop.get_name());
                                }
                            }
                        }
                    }
                }
            );

            ListView myList = (ListView) rootView.findViewById(R.id.ListLatLon);
            myList.setAdapter(myAdap);

            Button changeLat = (Button) rootView.findViewById(R.id.latButton);
            changeLat.setText(">");
            changeLat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button tmp = (Button) v;
                    if (tmp.getText().toString() == ">")
                    {
                        tmp.setText("<");
                    }
                    else
                    {
                        tmp.setText(">");
                    }
                    greaterLat = !greaterLat;
                }
            });

            Button changeLon = (Button) rootView.findViewById(R.id.lonButton);
            changeLon.setText(">");
            changeLon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button tmp = (Button) v;
                    if (tmp.getText().toString() == ">")
                    {
                        tmp.setText("<");
                    }
                    else
                    {
                        tmp.setText(">");
                    }
                    greaterLon = !greaterLon;
                }
            });
            return rootView;
        }

        final private boolean stringGreater(String left, String right)
        {
            for (int i = 0; i < left.length();i++)
            {
                if (left.charAt(i) > right.charAt(i))
                {
                    return true;
                }
                if (left.charAt(i) < right.charAt(i))
                {
                    return false;
                }
            }

            return false;
        }
    }
}
