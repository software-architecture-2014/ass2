package com.franco.general.couple.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by franco on 21.07.15.
 */
public class SQLHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "couple.dp";

    public static final String TABLE_DIARY = "CoupleEntry";
    public static final String DIARY_COL_ID      = "EntryID";
    public static final String DIARY_COL_DATE    = "EntryDate";
    public static final String DIARY_COL_TEXT    = "EntryText";
    public static final String DIARY_COL_FILE    = "EntryFile";


    private static final String DIARY_NO_URI = "NOURI";

    private static final String CREATE_DIARY =
            "CREATE TABLE " + TABLE_DIARY + " ( "
            + DIARY_COL_ID + " integer primary key, "
            + DIARY_COL_DATE + " integer , "
            + DIARY_COL_TEXT + " text , "
            + DIARY_COL_FILE + " text DEFAULT " + "'" + DIARY_NO_URI +"'" + " );";

    public SQLHelper(Context context)
    {
        super(context, DB_NAME,null,1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int oldversion, int newversion)
    {

    }

    @Override
    public void onCreate(SQLiteDatabase dataBase)
    {
        dataBase.execSQL(CREATE_DIARY);
        dataBase.execSQL("INSERT INTO " + TABLE_DIARY + " ( " + DIARY_COL_DATE + " , " + DIARY_COL_TEXT + " ) "
                                        + "VALUES ( "          +  System.currentTimeMillis() + ", 'Downloaded Couple! And here is my first Diary Entry' );");;
    }
}
