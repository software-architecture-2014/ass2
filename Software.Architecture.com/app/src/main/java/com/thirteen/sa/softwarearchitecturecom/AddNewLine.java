package com.thirteen.sa.softwarearchitecturecom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


public class AddNewLine extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_line);
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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.accept)
        {
            Intent returnIntent = this.getIntent();
            EditText name = (EditText) findViewById(R.id.todo);
            if (name.getText().length() == 0)
            {
                this.setResult(RESULT_CANCELED,returnIntent);
                finish();
            }
            returnIntent.putExtra("NAME", name.getText().toString());
            this.setResult(RESULT_OK,returnIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
