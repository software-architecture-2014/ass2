package com.franco.general.couple.main.helper;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.franco.general.couple.FilesAndPictures.PhotoManager;

import java.net.URI;


public class GalleryAlbumListenerAlbum implements View.OnClickListener {

    private RecyclerView my_recycler_view_;
    private PhotoManager manager_;

    public GalleryAlbumListenerAlbum(RecyclerView recyclerView, PhotoManager manager)
    {
        my_recycler_view_ = recyclerView;
        manager_ = manager;
    }

    @Override
    public void onClick(View v) {
        GalleryAdapter adapter_ = (GalleryAdapter) my_recycler_view_.getAdapter();
        int itemPosition = my_recycler_view_.getChildPosition(v);
        URI current_entry = adapter_.my_dataset_.get(itemPosition);

        adapter_.my_dataset_.clear();
        adapter_.my_dataset_.addAll(manager_.getChildURI(current_entry));
        my_recycler_view_.getAdapter().notifyDataSetChanged();
        Log.d("URI",current_entry.getPath());
    }
}
