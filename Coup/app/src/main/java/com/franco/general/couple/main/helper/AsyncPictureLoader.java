package com.franco.general.couple.main.helper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

//From the AndroidDeveloper Tutorial http://developer.android.com/intl/ko/training/displaying-bitmaps/process-bitmap.html
public class AsyncPictureLoader extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;

    public String data;

    private int requested_height_;
    private int requested_width_;

    private GalleryAdapter adapter_;

    public AsyncPictureLoader(ImageView imageView, int height, int width, GalleryAdapter adapter) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<>(imageView);

        requested_height_ = height;
        requested_width_ = width;
        adapter_ = adapter;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {
        data = params[0];
        return getBitmap(data);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) return;

        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            final AsyncPictureLoader bitmapWorkerTask =
                    GalleryAdapter.getBitmapWorkerTask(imageView);
            if (bitmapWorkerTask == this && imageView != null) {
                adapter_.addBitmapToMemoryCache(data,bitmap);
                imageView.setImageBitmap(bitmap);
                imageView.setAlpha(1f);
            }
        }
    }

    private Bitmap getBitmap(String file)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(file,options);

        options.inSampleSize = calculateSampleSize(options);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file,options);
    }

    private int calculateSampleSize(BitmapFactory.Options options)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > requested_height_ || width > requested_width_) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > requested_height_
                    && (halfWidth / inSampleSize) > requested_width_) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
