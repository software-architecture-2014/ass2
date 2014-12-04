package com.thirteen.sa.softwarearchitecturecom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends Activity {

    private DAO myDataAccessObject;

    private ArrayList<String> allNames;
    private Handler handleThread;
    private ProgressDialog myDialog;

    private static final int REQUEST_CODE    = 1;
    private static final int RUN_READY       = 2;
    private static final int NO_SUCH_STOP    = 3;
    private static final int LIST_OF_LINES   = 4;
    private static final int INSERT_LINES    = 5;
    private static final int NOT_CORRECT_ARG = 6;

    public static final String KEY_INTENT = "ADAPTER";

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        handleThread = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == RUN_READY)
                {
                    allNames = myDataAccessObject.getAllNames();
                    ArrayAdapter<String> autoComplete;
                    autoComplete = new ArrayAdapter<String>(getBaseContext(),
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
                else if (msg.what == LIST_OF_LINES || msg.what == NO_SUCH_STOP)
                {
                    ArrayList<String> lines;
                    if (msg.what == NO_SUCH_STOP)
                    {
                        lines = new ArrayList<String>();
                        lines.add("No such stop in the database!");
                    }
                    else{
                        lines = (ArrayList) msg.obj;
                        if (lines.size() == 0)
                        {
                          lines.add("Sorry! There is no connection between those stops!");
                        }
                    }
                    ArrayAdapter<String> myRoutes = new ArrayAdapter<String>(getBaseContext(),
                            R.layout.list_item_stations,
                            R.id.list_item_stations_textview,
                            lines);

                    ListView myListView = (ListView) findViewById(R.id.Routes);
                    myListView.setAdapter(myRoutes);
                }
                else if (msg.what == NOT_CORRECT_ARG)
                {
                    Toast.makeText(getBaseContext(), "You need to add at least two stops!", Toast.LENGTH_LONG).show();
                }
                myDialog.dismiss();
            }
        };
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        myDialog = ProgressDialog.show(this,"Database creation","Wait");
        CreateDatabase();
    }

    private void CreateDatabase()
    {
        Thread openDatabase = new Thread((new Runnable() {
            @Override
            public void run() {
                myDataAccessObject = new DAO(getBaseContext());
                if (!myDataAccessObject.open()) {
                    Log.d("FEHLER", "Not so good");
                }
                handleThread.sendEmptyMessage(RUN_READY);
            }
        }));
        openDatabase.start();
    }

    public void performSearch() {

        AutoCompleteTextView editTextFrom = (AutoCompleteTextView) findViewById(R.id.editFrom);
        AutoCompleteTextView editTextTo = (AutoCompleteTextView) findViewById(R.id.editTo);

        final String From = editTextFrom.getText().toString();
        final String To = editTextTo.getText().toString();

        if (From.length() == 0 || To.length() == 0) {
            Toast.makeText(this,"Fill in Stops!",Toast.LENGTH_LONG).show();
            return;
        }

        myDialog = ProgressDialog.show(this,"Searching for Routes","wait");
        Thread linesThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                }catch (Exception e)
                {
                    Log.d("FEHLER","Code 2");
                }
                ArrayList<String> lines = myDataAccessObject.getLines(From, To);
                if (lines == null)
                {
                    handleThread.sendEmptyMessage(NO_SUCH_STOP);
                    return;
                }
                Message msg = Message.obtain(handleThread);
                msg.obj = lines;
                msg.what = LIST_OF_LINES;
                handleThread.sendMessage(msg);
            }
        });
        linesThread.start();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        myDataAccessObject.close();
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
        if (id == R.id.action_all_stations) {
            Intent browse = new Intent(this,ViewAllStationsActivity.class);
            browse.putExtra(KEY_INTENT,allNames);
            startActivity(browse);
            return true;
        }
        else if (id == R.id.add) {
            Intent add = new Intent(this, AddNewLine.class);
            add.putExtra(KEY_INTENT,allNames);
            startActivityForResult(add, REQUEST_CODE);
            return true;
        }
        else if (id == R.id.action_lat_lon)
        {
            Intent latLon = new Intent(this,BrowseLatLon.class);
            startActivity(latLon);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SearchListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
            }
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,final Intent data)
    {
        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                myDialog = ProgressDialog.show(this,"Insertion into Database!","wait");
                Thread insert = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (data.getIntExtra("COUNT",0) < 2) {
                            handleThread.sendEmptyMessage(NOT_CORRECT_ARG);
                            return;
                        }
                        ArrayList<String> toAdd = new ArrayList<String>();
                        toAdd.add(data.getStringExtra("NAME"));

                        for (int i = 0; i<data.getIntExtra("COUNT",0);i++){
                            toAdd.add(data.getStringExtra("STOP " + i));
                        }
                        myDataAccessObject.insertNewRoute(toAdd);
                        handleThread.sendEmptyMessage(INSERT_LINES);
                    }
                });
                insert.start();
            }
            else if (resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this,"There is no name for your Route",Toast.LENGTH_LONG).show();
            }
        }
    }
}
