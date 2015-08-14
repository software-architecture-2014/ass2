package com.franco.general.couple.main.helper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.franco.general.couple.R;
import com.franco.general.couple.data.DiaryEntry;
import com.franco.general.couple.main.MainActivity;

import java.text.DateFormat;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public ArrayList<DiaryEntry> mDataset;
    private DateFormat my_format = null;
    private RecyclerClickListener myClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtFirst;
        public TextView txtSecond;
        public View viewFile;

        public MyViewHolder(View v) {
            super(v);
            txtFirst =  (TextView) v.findViewById(R.id.txtDate);
            txtSecond = (TextView)  v.findViewById(R.id.txtEntry);
            viewFile = v.findViewById(R.id.viewImage);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(RecyclerClickListener new_listener, ArrayList<DiaryEntry> myDataset) {
        mDataset = myDataset;
        myClickListener= new_listener;
        my_format = MainActivity.sdf_;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.my_diary_card, parent, false);
        itemView.setOnClickListener(myClickListener);
        itemView.setOnLongClickListener(myClickListener);
        return new MyViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        DiaryEntry de = mDataset.get(position);
        holder.txtFirst.setText(my_format.format(de.date_));
        holder.txtSecond.setText(de.entry_);
        if (de.uri_to_file_.toString().equals("NOURI"))
            holder.viewFile.setBackgroundDrawable(null);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}