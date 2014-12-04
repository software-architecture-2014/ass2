package com.thirteen.sa.softwarearchitecturecom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;



//The Data-Access-Object
    //Manages the SQLite DBase Statement
public class DAO {

    private SQLiteDatabase mySQLiteDB;
    private SQLiteHelper mySQLiteHelper;

    Context tmp;

    // Init the SQLiteHelper class for later access.
    public DAO(Context context)
    {
        mySQLiteHelper = new SQLiteHelper(context);tmp = context;
    }

    // opens the DB for access. Also calls onCreate() of SQLiteHelper class if first call.
    // Returns true if success.
    public boolean  open()
    {
        mySQLiteDB = mySQLiteHelper.getWritableDatabase();
        return  mySQLiteDB == null;
    }
    // Closes the SQLite DBase
    public void close()
    {
        mySQLiteDB.close();
    }


    public ArrayList<String> getLines(String From, String To)
    {
        ArrayList<String> retVal = new ArrayList<String>();
        String[] idFrom = intToString(getIdByName(From));
        String[] idTo   = intToString(getIdByName(To));

        if (idFrom.length == 0 || idTo.length == 0) {
            return null;
        }

        String argumentsFrom = "";
        for (int i = 0; i < idFrom.length-1; i++)
        {
            argumentsFrom += SQLiteHelper.MAPPING_STOPS_ID + "=? OR ";
        }
        argumentsFrom += SQLiteHelper.MAPPING_STOPS_ID + "=?";

        Cursor FromRoutes = mySQLiteDB.query(
                SQLiteHelper.MAPPING_TABLE_NAME,  // Table to Query
                new String[] {SQLiteHelper.MAPPING_ROUTES_ID}, // leaving "columns" null just returns all the columns.
                argumentsFrom, // cols for "where" clause
                idFrom, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        String argumentsTo = "";
        for (int i = 0; i < idTo.length-1; i++)
        {
            argumentsTo += SQLiteHelper.MAPPING_STOPS_ID + "=? OR ";
        }
        argumentsTo += SQLiteHelper.MAPPING_STOPS_ID + "=?";

        Cursor ToRoutes = mySQLiteDB.query(
                SQLiteHelper.MAPPING_TABLE_NAME,  // Table to Query
                new String[] {SQLiteHelper.MAPPING_ROUTES_ID}, // leaving "columns" null just returns all the columns.
                argumentsTo, // cols for "where" clause
                idTo, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        ArrayList<String> myIdForRoutes = new ArrayList<String>();
        FromRoutes.moveToFirst();
        while (!FromRoutes.isAfterLast())
        {
            ToRoutes.moveToFirst();
            while (!ToRoutes.isAfterLast())
            {
                if (FromRoutes.getInt(0) == ToRoutes.getInt(0))
                    myIdForRoutes.add(Integer.toString(FromRoutes.getInt(0)));
                ToRoutes.moveToNext();
            }
            FromRoutes.moveToNext();
        }
        String[] tmp = new String[myIdForRoutes.size()];
        tmp = myIdForRoutes.toArray(tmp);
        String argumentsRoutes = "";
        for (int i = 0; i < tmp.length-1; i++)
        {
            argumentsRoutes += SQLiteHelper.ROUTES_ID + "=? OR ";
        }
        argumentsRoutes += SQLiteHelper.ROUTES_ID + "=?";
        Cursor myRoutes = mySQLiteDB.query(
                SQLiteHelper.ROUTES_TABLE_NAME,  // Table to Query
                new String[]{SQLiteHelper.ROUTES_NAME}, // leaving "columns" null just returns all the columns.
                argumentsRoutes, // cols for "where" clause
                tmp, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        if (myRoutes.getCount() == 0)
            return retVal;

        myRoutes.moveToFirst();
        while (!myRoutes.isAfterLast())
        {
            retVal.add(myRoutes.getString(0));
            myRoutes.moveToNext();
        }

        return retVal;
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
            all_names.add(makeCursorToStopName(nameCursor).get_name());
            nameCursor.moveToNext();
        }

        return all_names;
    }

    private int[] getIdByName (String Name)
    {
        final String SELECT_STATEMENT= "SELECT " + SQLiteHelper.STOPS_ID  + " FROM "
                + SQLiteHelper.STOPS_TABLE_NAME + " WHERE " + SQLiteHelper.STOPS_NAME + " = ?";

        Cursor idCursor = mySQLiteDB.rawQuery(SELECT_STATEMENT,new String[] {Name});

        if (idCursor.getCount() == 0)
            return new int[0];
        idCursor.moveToFirst();
        int[] retVal = new int[idCursor.getCount()];
        for (int i = 0; i < idCursor.getCount();i++)
        {
            retVal[i] = idCursor.getInt(0);
            idCursor.moveToNext();
        }
        return retVal;

    }

    //Converts a Cursor Object to a Stop

    private Stop makeCursorToStopName(Cursor cursor)
    {
        return new Stop(cursor.getString(cursor.getColumnIndex(SQLiteHelper.STOPS_NAME)));
    }
    private Stop makeCursorToStop(Cursor cursor)
    {
        return new Stop(cursor.getString(3),cursor.getString(2),cursor.getString(1));
    }

    public void insertNewRoute(ArrayList<String> add)
    {
        Cursor count = mySQLiteDB.query(
                SQLiteHelper.ROUTES_TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );
        int newID =count.getCount();
        ContentValues myVal = new ContentValues();
        myVal.put(SQLiteHelper.ROUTES_ID,newID);
        myVal.put(SQLiteHelper.ROUTES_NAME,add.get(0));

        mySQLiteDB.insert(SQLiteHelper.ROUTES_TABLE_NAME,null,myVal);

        add.remove(0);

        for (String loop : add)
        {
            ContentValues myNewVal = new ContentValues();
            myNewVal.put(SQLiteHelper.MAPPING_ROUTES_ID,newID);
            myNewVal.put(SQLiteHelper.MAPPING_STOPS_ID,Integer.toString(getIdByName(loop)[0]));
            mySQLiteDB.insert(SQLiteHelper.MAPPING_TABLE_NAME,null,myNewVal);
        }

    }

    private String[] intToString(int[] toConvert)
    {
        String[] retVal = new String[toConvert.length];
            for (int i = 0; i < toConvert.length;i++)
                retVal[i] = Integer.toString(toConvert[i]);
        return  retVal;
    }
}
