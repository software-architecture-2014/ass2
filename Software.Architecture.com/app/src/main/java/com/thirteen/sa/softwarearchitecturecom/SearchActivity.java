package com.thirteen.sa.softwarearchitecturecom;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileReader;
import java.util.ArrayList;


public class SearchActivity extends Activity {

    private DAO myDataAccessObject;

    private ArrayAdapter<String> autoComplete;
    private ArrayList<String> allNames;

    private String From;
    private String To;

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        myDataAccessObject = new DAO(this);
        if (!myDataAccessObject.open()) {
            Log.d("FEHLER", "Not so good");
        }

        allNames = myDataAccessObject.getAllNames();

        autoComplete = new ArrayAdapter<String>(this,
                R.layout.list_item_stations,
                R.id.list_item_stations_textview,
                allNames);

        AutoCompleteTextView editTextFrom = (AutoCompleteTextView) findViewById(R.id.editFrom);
        AutoCompleteTextView editTextTo = (AutoCompleteTextView) findViewById(R.id.editTo);
        editTextFrom.setAdapter(autoComplete);
        editTextTo.setAdapter(autoComplete);

        editTextFrom.setOnEditorActionListener(new SearchListener());
        editTextTo.setOnEditorActionListener(new SearchListener());

    }

    public void performSearch() {
        AutoCompleteTextView editTextFrom = (AutoCompleteTextView) findViewById(R.id.editFrom);
        AutoCompleteTextView editTextTo = (AutoCompleteTextView) findViewById(R.id.editTo);

        From = editTextFrom.getText().toString();
        To = editTextTo.getText().toString();

        if (From.length() == 0 || To.length() == 0) {
            Toast.makeText(this, "Fill Fields!", Toast.LENGTH_LONG).show();
            return;
        }
        int idFrom = myDataAccessObject.getIdByName(From);
        int idTo   = myDataAccessObject.getIdByName(To);
        if (idFrom == 0 || idTo == 0) {
            Toast.makeText(this, "No such Stop!", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "SEARCH " + idFrom + " " + idTo, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        getMenuInflater().inflate(R.menu.action_bar_items, menu);
        return super.onCreateOptionsMenu(menu);
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
        if (id == R.id.action_all_stations) {
            startActivity(new Intent(this, ViewAllStationsActivity.class));
            return true;
        }
        if (id == R.id.add) {
            startActivityForResult(new Intent(this, AddNewLine.class), REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SearchListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
            }
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                String name = data.getStringExtra("NAME");
                Toast.makeText(this,name,Toast.LENGTH_LONG).show();
            }
            else if (resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this,"Cancel",Toast.LENGTH_LONG).show();
            }
        }
    }
}
