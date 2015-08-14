package com.calc.baby.babycalculator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Helper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BABY.db";
    public static final String TABLE_NAME = "Stuff";
    public static final String ID       = "id";
    public static final String AMOUNT   = "amount";
    public static final String DATE     = "date";
    private static final String CREATE = "create table " + TABLE_NAME + " ( " +
            ID + " integer primary key, " +
            AMOUNT + " integer, " +
            DATE + " long);";
    public Helper(Context context)
    {
        super(context, DB_NAME,null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase myDB)
    {
        myDB.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDB, int oldVersion, int newVersion)
    {

    }
}
