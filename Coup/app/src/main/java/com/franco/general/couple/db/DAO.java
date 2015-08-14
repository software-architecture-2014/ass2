package com.franco.general.couple.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.franco.general.couple.data.DiaryEntry;
import com.franco.general.couple.main.MainActivity;

import java.util.ArrayList;
import java.util.Date;

public class DAO {

    private static DAO singleton_ = null;
    private SQLHelper db_helper_ = null;

    private DAO(Context context)
    {
        db_helper_ = new SQLHelper(context);
    }

    public static DAO instance(Context context)
    {
        if (singleton_ == null)
            singleton_ = new DAO(context);
        return singleton_;
    }

    public ArrayList<DiaryEntry> getAllEntrys()
    {
        ArrayList<DiaryEntry> ret_val = new ArrayList<>();

        Cursor cursor = db_helper_.getWritableDatabase().query(
                SQLHelper.TABLE_DIARY,
                null,
                null,
                null,
                null,
                null,
                SQLHelper.DIARY_COL_DATE
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            Date date = new Date(cursor.getLong(1));

            Uri uri = Uri.parse(cursor.getString(3));
            DiaryEntry next_entry = new DiaryEntry(cursor.getInt(0),date,cursor.getString(2),uri);
            ret_val.add(next_entry);
            cursor.moveToNext();
        }

        cursor.close();
        db_helper_.close();
        return ret_val;
    }

    public long newEntry(Date date, String text, Uri uri)
    {
        ContentValues values = new ContentValues();
        values.put(SQLHelper.DIARY_COL_DATE,date.getTime());
        values.put(SQLHelper.DIARY_COL_TEXT, text);
        if (uri != null) values.put(SQLHelper.DIARY_COL_FILE, uri.toString());
        long new_id = db_helper_.getWritableDatabase().insert(SQLHelper.TABLE_DIARY,null,values);
        db_helper_.close();
        return new_id;
    }

    public void deleteEntry(long id)
    {
        db_helper_.getWritableDatabase().delete(SQLHelper.TABLE_DIARY, SQLHelper.DIARY_COL_ID + " =?",
                                                new String[] {String.valueOf(id)});
        db_helper_.close();
    }

    public void editEntry(long id, Date date, String text, String uri)
    {
        ContentValues values = new ContentValues();
        values.put(SQLHelper.DIARY_COL_DATE, date.getTime());
        values.put(SQLHelper.DIARY_COL_TEXT, text);
        values.put(SQLHelper.DIARY_COL_FILE,uri);
        db_helper_.getWritableDatabase().update(SQLHelper.TABLE_DIARY,values,SQLHelper.DIARY_COL_ID + "=?",
                new String[] {String.valueOf(id)});
        db_helper_.close();
    }
}
