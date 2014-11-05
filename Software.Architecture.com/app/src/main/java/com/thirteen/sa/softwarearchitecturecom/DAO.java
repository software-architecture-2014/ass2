package com.thirteen.sa.softwarearchitecturecom;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        final String SELECT_STATEMENT= "SELECT " + SQLiteHelper.STOPS_NAME + " FROM "
                + SQLiteHelper.STOPS_TABLE_NAME + " WHERE " + SQLiteHelper.STOPS_NAME + " = ?";
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

    // Returns all stops from DB.
    public ArrayList<Stop> getAllStops()
    {
        ArrayList<Stop> all_stops = new ArrayList<Stop>();

        Cursor weatherCursor = mySQLiteDB.query(
                SQLiteHelper.STOPS_TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                SQLiteHelper.STOPS_NAME, // columns to group by
                null, // columns to filter by row groups
                SQLiteHelper.STOPS_NAME  // sort order
        );

        weatherCursor.moveToFirst();
        while (!weatherCursor.isAfterLast())
        {
            all_stops.add(makeCursorToStop(weatherCursor));
            weatherCursor.moveToNext();
        }

        return all_stops;
    }

    // Get all Names from DB
    public ArrayList<String> getAllNames()
    {
        ArrayList<String> all_names = new ArrayList<String>();

        Cursor nameCursor = mySQLiteDB.query(
                SQLiteHelper.STOPS_TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                SQLiteHelper.STOPS_NAME, // columns to group by
                null, // columns to filter by row groups
                SQLiteHelper.STOPS_NAME  // sort order
        );

        nameCursor.moveToFirst();
        while (!nameCursor.isAfterLast())
        {
            all_names.add(makeCursorToStop(nameCursor).get_name());
            nameCursor.moveToNext();
        }

        return all_names;
    }

    public int getIdByName (String Name)
    {
        int id;

        final String SELECT_STATEMENT= "SELECT " + SQLiteHelper.STOPS_ID  + " FROM "
                + SQLiteHelper.STOPS_TABLE_NAME + " WHERE " + SQLiteHelper.STOPS_NAME + " = ?";

        Cursor idCursor = mySQLiteDB.rawQuery(SELECT_STATEMENT,new String[] {Name});

        if (idCursor.getCount() == 0)
            return 0;
        idCursor.moveToFirst();
        return idCursor.getInt(0);

    }

    //Converts a Cursor Object to a Stop
    // TODO Complete the Statement (now only NAME is handled)
    private Stop makeCursorToStop(Cursor cursor)
    {
        return new Stop(cursor.getString(cursor.getColumnIndex(SQLiteHelper.STOPS_NAME)));
    }
}
