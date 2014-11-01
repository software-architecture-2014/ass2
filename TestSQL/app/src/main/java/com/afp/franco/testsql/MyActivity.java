package com.afp.franco.testsql;

import java.io.File;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

public class MyActivity extends ListActivity {
    private DAO datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        try
        {
            datasource.open();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        List<Stop> values = datasource.getAllComments();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Stop> adapter = new ArrayAdapter<Stop>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        Log.d("bla","bla");
        @SuppressWarnings("unchecked")
        ArrayAdapter<Stop> adapter = (ArrayAdapter<Stop>) getListAdapter();
        Stop stop = null;
        switch (view.getId()) {
            case R.id.add:
                String[] comments = new String[] { "Cool", "Very nice", "Hate it" };
                int nextInt = new Random().nextInt(3);
                // save the new comment to the database
                stop = datasource.createStop(comments[nextInt]);
                adapter.add(stop);
                break;
            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    stop = (Stop) getListAdapter().getItem(0);
                    datasource.deleteStop(stop);
                    adapter.remove(stop);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        try
        {
            datasource.open();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}