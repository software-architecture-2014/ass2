import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by franco on 01.11.14.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    Context my_context;

    public static final String DB_NAME = "SA.db";

    public static final String TABLE_NAME = "STOPS";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LAT = "_lat";
    public static final String COLUMN_LON = "_lon";
    public static final String COLUMN_NAME = "_name";

    private static final String CREATE_STATEMENT = "create table " + TABLE_NAME
            + "(" + COLUMN_ID
            + " integer primary key, " + COLUMN_LON
            + " text not null, " + COLUMN_LAT
            + " text not null, " + COLUMN_NAME
            + " text not null);";

    public SQLiteHelper(Context context)
    {
        super(context, DB_NAME,null,1);
        my_context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase mySQL)
    {
        mySQL.execSQL(CREATE_STATEMENT);

        AssetManager my_asset_manager = my_context.getAssets();
        BufferedReader my_buffered_reader = null;

        try {
            my_buffered_reader = new BufferedReader(new InputStreamReader(my_asset_manager.open("SQLFile"),"UTF8"));

            String execute_insert = "";

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
        onCreate(mySQL);
    }
}
