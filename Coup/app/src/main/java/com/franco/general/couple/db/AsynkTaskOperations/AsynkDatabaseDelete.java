package com.franco.general.couple.db.AsynkTaskOperations;

import android.os.AsyncTask;
import android.widget.Toast;

import com.franco.general.couple.db.DAO;
import com.franco.general.couple.main.MainActivity;


public class AsynkDatabaseDelete extends AsyncTask<Long,Void,Long> {

    private MainActivity context_;

    public AsynkDatabaseDelete(MainActivity context)
    {
        super();
        context_ = context;
    }

    @Override
    protected Long doInBackground(Long ... params) {

        Long ret_val;
        try {
            ret_val = params[1];
            DAO.instance(context_).deleteEntry(params[0]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1l;
        }
        return ret_val;
    }

    @Override
    protected void onPostExecute (Long result)
    {
        if (result == -1l)
        {
            Toast.makeText(context_, "An internal error occurred",Toast.LENGTH_LONG).show();
            return;
        }
        int position = Integer.valueOf(Long.toString(result));
        context_.diaryFragment.my_dataset.remove(position);
        context_.diaryFragment.mRecyclerView.getAdapter().notifyItemRemoved(position);
        Toast.makeText(context_,"Diary Entry deleted!",Toast.LENGTH_LONG).show();
    }
}
