package com.thirteen.sa.softwarearchitecturecom;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class AddNewLine extends Activity {

    private ArrayList<AutoCompleteTextView> myText = new ArrayList<AutoCompleteTextView>();
    private ArrayAdapter<String> autoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_line);
        autoComplete = new ArrayAdapter<String>(this,  R.layout.list_item_stations,
                R.id.list_item_stations_textview,this.getIntent().getStringArrayListExtra("ADAPTER"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actiob_bar_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.accept)
        {
            Intent returnIntent = this.getIntent();
            EditText name = (EditText) findViewById(R.id.Name);
            if (name.getText().length() == 0)
            {
                this.setResult(RESULT_CANCELED,returnIntent);
                finish();
            }
            returnIntent.putExtra("NAME", name.getText().toString());
            int howmanystops = 0;
            for (int i = 0; i < myText.size();i++)
            {
                if (myText.get(i).getText().length() != 0) {
                    String tmp = myText.get(i).getText().toString();
                    returnIntent.putExtra("STOP " + howmanystops, tmp);
                    howmanystops++;
                }
            }
            returnIntent.putExtra("COUNT",howmanystops);

            this.setResult(RESULT_OK,returnIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addStopLine(View v)
    {
        RelativeLayout.LayoutParams myLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams Button = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout myRelativeLayout = (LinearLayout) findViewById(R.id.addLayout);
        myLayout.addRule(RelativeLayout.BELOW, R.id.Name);

        AutoCompleteTextView tmp = new AutoCompleteTextView(this);
        tmp.setHint("What Stop should be added?");
        tmp.setVisibility(View.VISIBLE);
        tmp.setLayoutParams(myLayout);
        tmp.setAdapter(autoComplete);
        myText.add(tmp);
        myRelativeLayout.addView(tmp);
    }
}
