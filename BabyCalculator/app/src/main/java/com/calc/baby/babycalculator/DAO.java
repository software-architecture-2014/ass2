package com.calc.baby.babycalculator;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DAO {
    private SQLiteDatabase myDB;
    private Helper myHelper;

    private SimpleDateFormat dfm;

    private static final long SEVEN_OFF = 604800000L;
    private static final long MONTH_OFF = 2592000000L;

    public DAO(Context context)
    {
        myHelper = new Helper(context);

    }

    public boolean open()
    {
        myDB = myHelper.getWritableDatabase();
        return  myDB == null;
    }

    public void close()
    {
        myDB.close();
    }

    public void safe(Integer toSave)
    {

        ContentValues myVal = new ContentValues();
        myVal.put(myHelper.AMOUNT, toSave);
        myVal.put(myHelper.DATE, new Date().getTime());

        myDB.insert(myHelper.TABLE_NAME,null,myVal);
    }

    public String convert(long toConv)
    {
        Date df = new java.util.Date(toConv);
        String retVal = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(df);
        return retVal;
    }

    public void deleteStuff()
    {
        myDB.delete(myHelper.TABLE_NAME,myHelper.DATE + " < " + (new Date().getTime() - MONTH_OFF), null);
    }

    public String showSeven()
    {
        String retVal = "";
        int counter;
        long over_all = 0;
        Cursor count = myDB.query(
                myHelper.TABLE_NAME,  // Table to Query
                new String[] {myHelper.AMOUNT}, // leaving "columns" null just returns all the columns.
                myHelper.DATE + ">?", // cols for "where" clause
                new String[] {Long.toString(new Date().getTime() - SEVEN_OFF)}, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        counter = count.getCount();
        count.moveToFirst();
        while (!count.isAfterLast())
        {
            over_all += count.getInt(0);
            count.moveToNext();
        }
        if (counter == 0) return "Noch keine Eingaben!";
        retVal = "Durchschnittsmenge: " + over_all/counter + " ML\nFläschchenmenge: " + counter;
        return retVal;
    }

    public String showMonth()
    {
        String retVal = "";
        int counter;
        long over_all = 0;
        Cursor count = myDB.query(
                myHelper.TABLE_NAME,  // Table to Query
                new String[] {myHelper.AMOUNT}, // leaving "columns" null just returns all the columns.
                myHelper.DATE + ">?", // cols for "where" clause
                new String[] {Long.toString(new Date().getTime() - MONTH_OFF)}, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        counter = count.getCount();
        count.moveToFirst();
        while (!count.isAfterLast())
        {
            over_all += count.getInt(0);
            count.moveToNext();
        }
        if (counter == 0) return "Noch keine Eingaben!";
        retVal = "Durchschnittsmenge: " + over_all/counter + " ML\nFläschchenmenge: " + counter;
        return retVal;
    }
    public String getToday()
    {
        String retVal = "";

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        long myMask = c.getTimeInMillis();

        Cursor myCursor = myDB.query(
                myHelper.TABLE_NAME,  // Table to Query
                new String[] {myHelper.AMOUNT, myHelper.DATE}, // leaving "columns" null just returns all the columns.
                myHelper.DATE + ">?", // cols for "where" clause
                new String[] {Long.toString(myMask)}, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        if (myCursor.getCount() == 0) return "Heute wurde noch nicht gefüttert";
        myCursor.moveToFirst();
        retVal = "Emilia hat heute " + myCursor.getCount() + " mal getrunken\n";
        while (!myCursor.isAfterLast())
        {
            String toShow = convert(myCursor.getLong(1));
            toShow = toShow.substring(11);
            retVal += "Um " + toShow + " " + myCursor.getInt(0) + "ML\n";
            myCursor.moveToNext();
        }
        return retVal;
    }
    public String lastMeal()
    {
        String retVal;

        Cursor lastMeal = myDB.query(
                myHelper.TABLE_NAME,  // Table to Query
                new String[] {"max ( "+ myHelper.DATE + " )",myHelper.AMOUNT}, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        if (lastMeal.getCount() == 0) return "Noch keine Eingaben";
        lastMeal.moveToFirst();
        String tmp = convert(lastMeal.getLong(0));
        tmp = tmp.substring(11);
        retVal = "Die letzte Mahlzeit war um " + tmp + " " + lastMeal.getInt(1)+ "ML";
        return retVal;
    }
}
