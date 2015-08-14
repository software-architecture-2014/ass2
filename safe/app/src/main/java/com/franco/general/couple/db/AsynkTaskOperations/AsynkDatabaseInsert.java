package com.franco.general.couple.db.AsynkTaskOperations;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.franco.general.couple.data.DiaryEntry;
import com.franco.general.couple.db.DAO;
import com.franco.general.couple.main.MainActivity;
import com.franco.general.couple.main.NewEntry;

import org.apache.http.client.utils.URIUtils;

import java.text.ParseException;
import java.util.Date;

public class AsynkDatabaseInsert extends AsyncTask<String,Void,DiaryEntry> {

    private MainActivity context_;

    private static final String FAILED = "FAILED";
    private static final String SUCCEED = "SUCCEED";

    public AsynkDatabaseInsert(MainActivity context)
    {
        super();
        context_ = context;
    }

    @Override
    protected DiaryEntry doInBackground(String... params) {

        DiaryEntry new_entry;
        try
        {
            Date date = MainActivity.sdf_.parse(params[0]);
            Uri uri = Uri.parse(params[2]);

            long id = DAO.instance(context_).newEntry(date,params[1],uri);
            new_entry = new DiaryEntry(id, date,params[1],uri);
        }
        catch (NullPointerException e)
        {
            Log.d("Internal Error in Asynk","Null Pointer error");
            e.printStackTrace();
            return null;
        }
        catch (ParseException e)
        {
            Log.d("Internal Error in Asynk","Parse error");
            e.printStackTrace();
            return null;
        }
        return new_entry;
    }

    @Override
    protected void onPostExecute (DiaryEntry result)
    {
        if (result != null) {
            context_.diaryFragment.my_dataset.add(0,result);
            context_.diaryFragment.mRecyclerView.getAdapter().notifyItemInserted(0);
            context_.diaryFragment.mRecyclerView.smoothScrollToPosition(0);
        }
        else
        {
            Toast.makeText(context_,"A internal Error occurred :(",Toast.LENGTH_LONG).show();
        }
    }
}
