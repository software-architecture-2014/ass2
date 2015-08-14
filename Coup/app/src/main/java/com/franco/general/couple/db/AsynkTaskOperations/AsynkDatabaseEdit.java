package com.franco.general.couple.db.AsynkTaskOperations;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.franco.general.couple.data.DiaryEntry;
import com.franco.general.couple.db.DAO;
import com.franco.general.couple.main.MainActivity;

import java.util.Date;

/**
 * Created by franco on 24.07.15.
 */
public class AsynkDatabaseEdit extends AsyncTask<String, Void, Boolean> {

    private MainActivity context_;
    private int changed_item_;

    private Date date_;
    private String text_;
    private String uri_;

    public AsynkDatabaseEdit(MainActivity context, int changed_item)
    {
        super();
        context_ = context;
        changed_item_ = changed_item;
    }

    @Override
    protected Boolean doInBackground(String ... params) {

        Boolean ret_val = true;
        try {
            long id = Long.valueOf(params[0]);
            date_ = MainActivity.sdf_.parse(params[1]);
            text_ = params[2];
            uri_ = params[3];
            DAO.instance(context_).editEntry(id,date_,text_,uri_);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ret_val = false;
        }
        return ret_val;
    }

    @Override
    protected void onPostExecute (Boolean result)
    {
        if (result)
        {
            DiaryEntry diaryEntry = context_.diaryFragment.my_dataset.get(changed_item_);
            diaryEntry.entry_ = text_;
            diaryEntry.uri_to_file_ = Uri.parse(uri_);
            diaryEntry.date_ = date_;

            context_.diaryFragment.mRecyclerView.getAdapter().notifyItemChanged(changed_item_);
        }
        else
        {
            Toast.makeText(context_, "An internal Error occurred!",Toast.LENGTH_LONG).show();
        }
    }
}
