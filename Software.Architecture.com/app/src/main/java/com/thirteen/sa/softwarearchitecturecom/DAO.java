package com.thirteen.sa.softwarearchitecturecom;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by franco on 01.11.14.
 */


//The Data-Access-Object
    //Manages the SQLite DBase Statement
public class DAO {

    SQLiteDatabase mySQLiteDB;
    SQLiteHelper mySQLiteHelper;

    // Init the SQLiteHelper class for later access.
    public DAO(Context context)
    {
        mySQLiteHelper = new SQLiteHelper(context);
    }

    // opens the DB for access. Also calls onCreate() of SQLiteHelper class if first call.
    // Returns true if success.
    public boolean  open()
    {
        mySQLiteDB = mySQLiteHelper.getWritableDatabase();
        if (mySQLiteDB == null)
            return false;
        return true;
    }
    // Closes the SQLite DBase
    public void close()
    {
        mySQLiteDB.close();
    }


    // Returns all Stops with the name given by the parameter
    // returns an ArrayList of Stops (in c++ it would be an vector)
    public ArrayList<Stop> getStopsByName(String name)
    {
        // The Statement as String (SELECT _name FROM STOPS WHERE _name = ?
        final String SELECT_STATEMENT= "SELECT " + SQLiteHelper.COLUMN_NAME + " FROM "
                + SQLiteHelper.TABLE_NAME + " WHERE " + SQLiteHelper.COLUMN_NAME + " = ?";
        ArrayList<Stop> all_stops = new ArrayList<Stop>();

        String[] filter = {name}; // After what should we filter (THE PARAMETER)

        // Executes on Statement and returns a Cursor to the given result
        // RawQuery takes two arguments:
        // FIRST - the SQLStatement (when there is a WHERE Statement you use the sec parameter as filer
        // SECOND - the filter for the WHERE statement
        Cursor my_cursor = mySQLiteDB.rawQuery(SELECT_STATEMENT, filter);

        my_cursor.moveToFirst();
        while (!my_cursor.isAfterLast())
        {
            all_stops.add(makeCursorToStop(my_cursor));
            my_cursor.moveToNext();
        }

        return all_stops;
    }

    //Converts a Cursor Object to a Stop
    // TODO Complete the Statement (now only NAME is handled)
    private Stop makeCursorToStop(Cursor cursor)
    {
        return new Stop(cursor.getString(0));
    }
}