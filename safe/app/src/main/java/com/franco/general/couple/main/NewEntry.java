package com.franco.general.couple.main;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.franco.general.couple.FilesAndPictures.PhotoManager;
import com.franco.general.couple.R;
import com.franco.general.couple.data.DiaryEntry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewEntry extends ActionBarActivity{

    private EditText date_ = null;
    private EditText text_ = null;

    public static final String KEY_EDIT = "EDIT";

    public static final String KEY_DATE = "DATE";
    public static final String KEY_TEXT = "TEXT";
    public static final String KEY_URI = "URI";
    public static final String NO_URI = "NOURI";

    private int position_edit_ = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_fragment_edit);

        date_ = (EditText) findViewById(R.id.createDate);
        text_ = (EditText) findViewById(R.id.createText);

        text_.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER  && event.getAction() == KeyEvent.ACTION_DOWN) {

                    if ( ((EditText)v).getLineCount() >= 5 )
                        return true;
                }

                return false;
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(KEY_EDIT))
        {
            position_edit_ = intent.getIntExtra(KEY_EDIT,-1);
            date_.setText(intent.getStringExtra(KEY_DATE));
            text_.setText(intent.getStringExtra(KEY_TEXT));
        }
        else
        {
            date_.setText(MainActivity.sdf_.format(new Date(System.currentTimeMillis())));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.newEntryAccept) {
            exitActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void exitActivity()
    {
        boolean valid_input = true;
        String given_date = null;
        String given_text = null;
        try {
            given_date = date_.getText().toString();
            MainActivity.sdf_.parse(given_date);
            given_text = text_.getText().toString();
            if (given_text.equals("") || given_text.length() == 0)
                valid_input = false;
        }
        catch (ParseException e)
        {
            valid_input = false;
        }

        if (valid_input)
        {
            Intent return_intent = new Intent();
            if (position_edit_ != -1)
                return_intent.putExtra(KEY_EDIT,position_edit_);
            return_intent.putExtra(KEY_DATE,given_date);
            return_intent.putExtra(KEY_TEXT,given_text);
            return_intent.putExtra(KEY_URI,NO_URI);
            setResult(RESULT_OK,return_intent);
            finish();
        }
        else
        {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
