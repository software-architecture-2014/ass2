package com.afp.franco.testsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.w3c.dom.Comment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by franco on 31.10.14.
 */
public class DAO {

    private SQLiteDatabase database;
    private SQLITEHelper dbHelper;
    private String[] allColumns = { SQLITEHelper.COLUMN_ID,
            SQLITEHelper.COLUMN_LON, SQLITEHelper.COLUMN_LAT, SQLITEHelper.COLUMN_NAME };

    public DAO(Context context) {
        dbHelper = new SQLITEHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Stop createStop(String stop) {
        ContentValues values = new ContentValues();
        values.put(SQLITEHelper.COLUMN_NAME, stop);
        values.put(SQLITEHelper.COLUMN_LAT, "testLAT");
        values.put(SQLITEHelper.COLUMN_LON, "testLON");
        long insertId = database.insert(SQLITEHelper.TABLE_NAME, null,
                values);
        Cursor cursor = database.query(SQLITEHelper.TABLE_NAME,
                allColumns, SQLITEHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Stop new_stop = cursorToStop(cursor);
        cursor.close();
        return new_stop;
    }

    public void deleteStop(Stop stop) {
        long id = stop.get_id();
        System.out.println("com.afp.franco.testsql.Stop deleted with id: " + id);
        database.delete(SQLITEHelper.TABLE_NAME, SQLITEHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Stop> getAllComments() {
        List<Stop> stops = new ArrayList<Stop>();

        Cursor cursor = database.query(SQLITEHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Stop stop = cursorToStop(cursor);
            stops.add(stop);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return stops;
    }

    private Stop cursorToStop(Cursor cursor) {
        Stop stop = new Stop();
        stop.set_id(cursor.getInt(0));
        stop.set_name(cursor.getString(1));
        return stop;
    }
}
