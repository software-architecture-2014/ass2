package com.thirteen.sa.softwarearchitecturecom;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ContentProviderClient;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SearchActivity extends Activity {

    private DAO myDataAccessObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
       /* SQLiteHelper tmp = new SQLiteHelper(this);
        SQLiteDatabase my_sql = tmp.getWritableDatabase();
        Toast.makeText(this,"i am ready!",Toast.LENGTH_LONG).show();*/

        myDataAccessObject = new DAO(this);
        if (!myDataAccessObject.open())
        {
            Log.d("FEHLER", "Not so good");
        }

       /*EditText v = (EditText) findViewById(R.id.editFrom);
        final Context tmp = this;
        v.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    ArrayList<Stop> dummy = myDataAccessObject.getStopsByName("Jakominiplatz");
                    Toast.makeText(tmp, Integer.toString(dummy.size()),Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });*/


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_search, container, false);
            return rootView;
        }
    }
}
