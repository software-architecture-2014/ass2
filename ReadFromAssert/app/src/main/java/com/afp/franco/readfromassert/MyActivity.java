package com.afp.franco.readfromassert;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        AssetManager dummy = this.getAssets();
        BufferedReader  tmp;
        String test = "";
        try
        {
            tmp = new BufferedReader(new InputStreamReader(dummy.open("sqlfile"),"UTF8"));

            while ((test = tmp.readLine()) != null) {
                Log.d("Read: ", test);
            }

            Log.d("READY", "READY");

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        TextView hello = (TextView) findViewById(R.id.txtOut);
        hello.setText(test);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

}
