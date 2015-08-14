package com.franco.general.couple.main.helper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

//FROM http://developer.android.com/intl/ko/training/displaying-bitmaps/process-bitmap.html
class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<AsyncPictureLoader> bitmapWorkerTaskReference;

    public AsyncDrawable(Resources res, Bitmap bitmap,
                         AsyncPictureLoader bitmapWorkerTask) {
        super(res, bitmap);
        bitmapWorkerTaskReference =
                new WeakReference<AsyncPictureLoader>(bitmapWorkerTask);
    }

    public AsyncPictureLoader getBitmapWorkerTask() {
        return bitmapWorkerTaskReference.get();
    }
}