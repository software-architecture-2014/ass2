package com.thirteen.sa.softwarearchitecturecom;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class SQLiteHelper extends SQLiteOpenHelper {

    Context my_context;

    public static final String DB_NAME = "SA.db";

    public static final String STOPS_TABLE_NAME = "STOPS";
    public static final String ROUTES_TABLE_NAME = "ROUTES";
    public static final String MAPPING_TABLE_NAME = "MAPPING";

    public static final String STOPS_ID = "_id";
    public static final String STOPS_LAT = "_lat";
    public static final String STOPS_LON = "_lon";
    public static final String STOPS_NAME = "_name";

    public static final String ROUTES_ID = "_id";
    public static final String ROUTES_NAME = "_name";

    public static final String MAPPING_ROUTES_ID = "_routes_id";
    public static final String MAPPING_STOPS_ID = "_stops_id";

    private static final String CREATE_STOP = "create table " + STOPS_TABLE_NAME
            + "(" + STOPS_ID
            + " integer primary key, " + STOPS_LON
            + " text not null, " + STOPS_LAT
            + " text not null, " + STOPS_NAME
            + " text not null);";
    private static final  String CREATE_ROUTES = "create table " + ROUTES_TABLE_NAME
            + "(" + ROUTES_ID
            +" integer primary key, " + ROUTES_NAME
            + " text not null);";
    private static final String CREATE_MAPPING = "create table " + MAPPING_TABLE_NAME
            + "(" + MAPPING_ROUTES_ID
            +" integer, " + MAPPING_STOPS_ID
            + " integer);";

    public SQLiteHelper(Context context)
    {
        super(context, DB_NAME,null,1);
        my_context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase mySQL)
    {
        mySQL.execSQL(CREATE_STOP);
        mySQL.execSQL(CREATE_ROUTES);
        mySQL.execSQL(CREATE_MAPPING);

        AssetManager my_asset_manager = my_context.getAssets();
        BufferedReader my_buffered_reader;

        try {
            my_buffered_reader = new BufferedReader(new InputStreamReader(my_asset_manager.open("SQLFile"),"UTF8"));

            String execute_insert;

            while ((execute_insert = my_buffered_reader.readLine()) != null){
                mySQL.execSQL(execute_insert);
            }
        }
        catch (IOException e)
        {//TODO
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase mySQL, int oldversion, int newversion)
    {
        mySQL.execSQL("DROP TABLE IF EXISTS ");
        onCreate(mySQL);
    }
}
