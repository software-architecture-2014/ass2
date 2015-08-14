package com.franco.general.couple.main.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.franco.general.couple.FilesAndPictures.PhotoManager;
import com.franco.general.couple.R;
import com.franco.general.couple.main.MainActivity;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<URI> my_dataset_;
    private MainActivity context_;

    private int requested_width_ = -1;
    private int requested_height_ = -1;

    private static final int FOLDER_ = 1;
    private static final int PIC_    = 2;

    private LruCache<String, Bitmap> cached_bitmaps_;

    private GalleryAlbumListenerAlbum listener;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolderFolder extends RecyclerView.ViewHolder
    {
        public RelativeLayout container_;
        public ImageView image_view_;
        public TextView file_name_;

        public ViewHolderFolder(View v) {
            super(v);
            container_ = (RelativeLayout) v.findViewById(R.id.galleryContainer);
            image_view_ = (ImageView) v.findViewById(R.id.galleryImage);
            file_name_ = (TextView) v.findViewById(R.id.txtFileName);

        }
    }

    public static class ViewHolderPicture extends RecyclerView.ViewHolder
    {
        public ImageView image_view_;

        public ViewHolderPicture(View v) {
            super(v);
            image_view_ = (ImageView) v.findViewById(R.id.galleryImagePicture);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GalleryAdapter(MainActivity context, ArrayList<URI> myDataset, RecyclerView recyclerView) {
        my_dataset_ = myDataset;
        context_ = context;

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        requested_width_ = requested_height_ = (metrics.widthPixels / 3);

        cached_bitmaps_ = new LruCache<>((int)(Runtime.getRuntime().maxMemory() / 1024 / 16));

        listener = new GalleryAlbumListenerAlbum(recyclerView,context.photoManager);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            cached_bitmaps_.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return cached_bitmaps_.get(key);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View itemView;
        if (viewType == FOLDER_) {
           itemView  = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_for_gallery, parent, false);
           itemView.setOnClickListener(listener);
           return new ViewHolderFolder(itemView);

        }
        else if (viewType == PIC_)
        {
            itemView  = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_for_pic, parent, false);
            return new ViewHolderPicture(itemView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position)
    {
        URI uri = my_dataset_.get(position);

        if (position == 0 || new File(uri).isDirectory())
            return FOLDER_;
        else
            return PIC_;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hold, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        URI uri = my_dataset_.get(position);

        if (uri.getPath().equals(PhotoManager.NEW_ALBUM_)) {

            ViewHolderFolder holder = (ViewHolderFolder) hold;
            holder.image_view_.setBackgroundDrawable(context_.getResources().getDrawable(R.drawable.ic_add_box_black_48dp));
            holder.file_name_.setText("new Album");
            holder.container_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context_);
                    final EditText input = new EditText(context_);
                    InputFilter[] filter = new InputFilter[1];
                    filter[0] = new InputFilter.LengthFilter(10); //TODO edit the max length
                    input.setFilters(filter);
                    builder.setView(input);
                    builder.setTitle("Give your Album a name!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            newAlbum(input.getText().toString());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
            });
        }
        else if (uri.getPath().equals(PhotoManager.BACK_TO_ROOT_))
        {
            ViewHolderFolder holder = (ViewHolderFolder) hold;
            holder.image_view_.setBackgroundDrawable(context_.getResources().getDrawable(R.drawable.ic_file_upload_black_24dp));
            holder.file_name_.setText("back..");
            holder.container_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backToRoot();
                }
            });
        }
        else
        {
            if (hold instanceof ViewHolderFolder)
            {
                ViewHolderFolder holder = (ViewHolderFolder) hold;
                holder.image_view_.setBackgroundDrawable(context_.getResources().getDrawable(R.drawable.ic_folder_black_48dp));
                holder.image_view_.setAlpha(0.5f);
                String directory_name = uri.toString();
                directory_name = directory_name.substring(0,directory_name.length()-1);
                int index = directory_name.lastIndexOf('/');
                String sub = directory_name.substring(index+1);

                holder.file_name_.setText(sub);
            }
            else
            {
                ViewHolderPicture holder = (ViewHolderPicture) hold;
                String command = uri.getPath();
                Bitmap bmp = getBitmapFromMemCache(command);
                if (bmp != null)
                {
                    holder.image_view_.setImageBitmap(bmp);
                }
                else if (cancelPotentialWork(command, holder.image_view_)) {
                    AsyncPictureLoader task = new AsyncPictureLoader(holder.image_view_, requested_height_, requested_width_,this);
                    AsyncDrawable drawable = new AsyncDrawable(context_.getResources(), null, task);
                    holder.image_view_.setImageDrawable(drawable);
                    task.execute(command);
                }
                holder.image_view_.getLayoutParams().width = requested_width_;
                holder.image_view_.getLayoutParams().height = requested_height_;
                holder.image_view_.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    // from http://developer.android.com/intl/ko/training/displaying-bitmaps/process-bitmap.html
    public static AsyncPictureLoader getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public void backToRoot()
    {
        my_dataset_.clear();
        my_dataset_.addAll(context_.photoManager.getChildURI(context_.photoManager.root_directory_.toURI()));
        notifyDataSetChanged();
    }

    // from http://developer.android.com/intl/ko/training/displaying-bitmaps/process-bitmap.html
    private boolean cancelPotentialWork(String data, ImageView imageView) {
        final AsyncPictureLoader bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.data;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == null) return false;
            else if (bitmapData.equals("") || !bitmapData.equals(data)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private void newAlbum(String name)
    {

        if (name.equals("") || name.length() == 0)
            return;
        URI new_uri = context_.photoManager.newAlbum(name);
        if (new_uri != null)
        {
            my_dataset_.add(1,new_uri);
            notifyItemInserted(1);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return my_dataset_.size();
    }
}