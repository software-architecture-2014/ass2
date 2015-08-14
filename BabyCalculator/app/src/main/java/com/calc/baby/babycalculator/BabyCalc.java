package com.calc.baby.babycalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;


public class BabyCalc extends Activity {

    DAO myDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_calc);
        myDAO = new DAO(this);
        myDAO.open();
        myDAO.deleteStuff();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        myDAO.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_baby_calc, menu);
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

    public void safe(View v)
    {
        EditText mili = (EditText) findViewById(R.id.EditMl);
        final String input = mili.getEditableText().toString();

        if (input.length() == 0)
        {
            Toast.makeText(this,"Keine Eingabe!",Toast.LENGTH_LONG).show();

        }
        else
        {
            new AlertDialog.Builder(this).setTitle("Sicher?").setMessage("Speichere " +
                                            input + " ML am \n" + myDAO.convert(new Date().getTime()))
            .setPositiveButton("save",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myDAO.safe(Integer.valueOf(input));

                }
            }).setNegativeButton("Abbruch", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getBaseContext(), "Doch nicht", Toast.LENGTH_LONG).show();
                }
            }).show();

        }
    }

    public void handleVerlauf(View v)
    {
        final TextView myTxt = (TextView) findViewById(R.id.txtOut);
        new AlertDialog.Builder(this).setTitle("Welche Statistik?").setMessage("7 Tage oder 1 Monat?")
                .setPositiveButton("7 Tage",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myTxt.setText(myDAO.showSeven());
                    }
                }).setNegativeButton("1 Monat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myTxt.setText(myDAO.showMonth());
                    }
        }).show();
    }
    public void today(View v)
    {
        final TextView myTxt = (TextView) findViewById(R.id.txtOut);
        myTxt.setText(myDAO.getToday());
    }

    public void Last(View v)
    {
        final TextView myTxt = (TextView) findViewById(R.id.txtOut);
        myTxt.setText(myDAO.lastMeal());
    }
}
