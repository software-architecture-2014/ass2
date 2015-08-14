package com.franco.general.couple.main.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.franco.general.couple.data.DiaryEntry;
import com.franco.general.couple.db.AsynkTaskOperations.AsynkDatabaseDelete;
import com.franco.general.couple.main.MainActivity;
import com.franco.general.couple.main.NewEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerClickListener implements View.OnClickListener, View.OnLongClickListener {

    private RecyclerView mRecyclerView;
    private ArrayList<DiaryEntry> my_dataset;
    private MainActivity context;

    private static final String[] OPTIONS = {"Edit", "Delete","Link to File"};

    private View clicked_view_ = null;

    public RecyclerClickListener(RecyclerView rv,  MainActivity cnt, ArrayList<DiaryEntry> data)
    {
        mRecyclerView = rv;
        context = cnt;
        my_dataset = data;
    }

    @Override
    public void onClick(View v) {
        int itemPosition = mRecyclerView.getChildPosition(v);
        DiaryEntry current_entry = my_dataset.get(itemPosition);
        current_entry.entry_ ="JUHUUU FRANCO";
        mRecyclerView.getAdapter().notifyItemChanged(itemPosition);
    }
    @Override
    public boolean onLongClick(View v)
    {
        clicked_view_ = v;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Options");
        builder.setItems(OPTIONS, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                actionPressed(item);
            }
        });
        builder.show();
        return true;
    }

    private void actionPressed(int item_pressed)
    {
        switch (item_pressed) {
            case 0:
                editPressed(mRecyclerView.getChildPosition(clicked_view_));
                break;
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure you want to delete this entry?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePressed(mRecyclerView.getChildPosition(clicked_view_));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;
            case 2:
                Toast.makeText(context,"File Stuff was pressed!",Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(context, "This is not possible", Toast.LENGTH_SHORT).show();
        }
    }

    private void deletePressed(int position)
    {
        AsynkDatabaseDelete task = new AsynkDatabaseDelete(context);
        long id = context.diaryFragment.my_dataset.get(position).id_;
        task.execute(id,(long) position);
    }

    private void editPressed(int position)
    {
        DiaryEntry diaryEntry = context.diaryFragment.my_dataset.get(position);

        Intent intent = new Intent(context,NewEntry.class);
        intent.putExtra(NewEntry.KEY_EDIT,position);
        intent.putExtra(NewEntry.KEY_DATE,MainActivity.sdf_.format(diaryEntry.date_));
        intent.putExtra(NewEntry.KEY_TEXT,diaryEntry.entry_);
        intent.putExtra(NewEntry.KEY_URI, diaryEntry.uri_to_file_.toString());
        context.startActivityForResult(intent, MainActivity.EDIT_ENTRY);
    }
}
