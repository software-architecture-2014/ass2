package com.afp.franco.testsql;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by franco on 31.10.14.
 */
public class SQLITEHelper extends SQLiteOpenHelper {

    private Context my_context;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LON= "_lon";
    public static final String COLUMN_LAT = "_lat";
    public static final String COLUMN_NAME = "_name";
    public static final String TABLE_NAME = "STOPS";
    private static final String DATABASE_NAME = "STOP.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME
            + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_LON
            + " text not null, " + COLUMN_LAT
            + " text not null, " + COLUMN_NAME
            + " text not null);";

    public SQLITEHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        my_context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        if (doesDatabaseExist(my_context,DATABASE_NAME))
        {
            database.execSQL(DATABASE_CREATE);
        }
        else
        {
            database.execSQL(DATABASE_CREATE);
            AssetManager dummy = my_context.getAssets();
            BufferedReader tmp;
            String test = "";
            try
            {
                tmp = new BufferedReader(new InputStreamReader(dummy.open("sqlbackup"),"UTF8"));
                if (tmp == null) {
                    Log.d("hallo", "hallo biatch"); return;
                }
                while ((test = tmp.readLine()) != null) {
                    Log.d("Read: ", test);
                }

                Log.d("READY", "READY");

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLITEHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS ");
        onCreate(db);
    }
    private boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
