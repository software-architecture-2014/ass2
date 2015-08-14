package com.franco.general.couple.FilesAndPictures;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.franco.general.couple.main.MainActivity;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


//TODO maybe next thing. Create PhotoManagerException and throw it
public class PhotoManager {

    private MainActivity context_;

    public File root_directory_;
    public File current_directory_;

    private BroadcastReceiver mExternalStorageReceiver;
    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWriteable = false;

    public URI current_photo_path_;

    public static final String NEW_ALBUM_ = "NEWALBUM";
    public static final String BACK_TO_ROOT_ = "ROOOOOT";

    public void deleteTempFile()
    {
        File to_delete = new File(current_photo_path_);
        if (preCheck() && to_delete.delete())
        {
            Log.d("File","Deleted");
        }
        else
        {
            Toast.makeText(context_, "Try again later",Toast.LENGTH_LONG).show();
        }
    }

    public PhotoManager(MainActivity context)
    {
        context_ = context;

        mExternalStorageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("test", "Storage: " + intent.getData());
                updateExternalStorageState();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        context.registerReceiver(mExternalStorageReceiver, filter);
        updateExternalStorageState();

        if (mExternalStorageAvailable && mExternalStorageWriteable) {
            root_directory_ = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Couple/");
            if (!root_directory_.exists())
                if (root_directory_.mkdirs()) {
                    Toast.makeText(context, "YEAH", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, root_directory_.getAbsolutePath(), Toast.LENGTH_LONG).show();
                }
        }
        current_directory_ = root_directory_;
    }

    private File createFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "JPEG_" + timeStamp + "_";
        File storageDir = current_directory_;

        File image = File.createTempFile(
                filename,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        current_photo_path_ = image.toURI();

        return image;
    }

    public void takePhoto() throws IOException
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(context_.getPackageManager()) != null) {
            File file = createFile();

            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(file));
            context_.startActivityForResult(intent, MainActivity.TAKE_PHOTO_FAB);

        }
        else
            Toast.makeText(context_,"A strange error occurred. Try again!",Toast.LENGTH_LONG).show();
    }

    public void handlePhoto()
    {
        context_.galleryFragment.my_dataset_.add(current_photo_path_);
        context_.galleryFragment.my_recycler_view_.getAdapter().
                notifyItemInserted(context_.galleryFragment.my_dataset_.size() - 1);
    }

    void updateExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }

    public void stopWatchingExternalStorage() {
        context_.unregisterReceiver(mExternalStorageReceiver);
    }

    private boolean preCheck()
    {
        return (mExternalStorageAvailable && mExternalStorageWriteable);
    }

    public URI newAlbum(String name)
    {
        if (!preCheck())
        {
            Toast.makeText(context_,"Your Device is not mounted! Do something!",Toast.LENGTH_LONG).show();
            return null;
        }

        File new_album_file = new File(root_directory_.getPath() + "/" + name);
        if (new_album_file.exists())
        {
            Toast.makeText(context_,"There is an album with the same name!",Toast.LENGTH_LONG).show();
            return null;
        }

        if (!new_album_file.mkdirs())
        {
            Toast.makeText(context_, "Something went wrong :(",Toast.LENGTH_LONG).show();
        }
        return new_album_file.toURI();
    }

    public ArrayList<URI> getChildURI(URI uri)
    {
        if (!preCheck()) return null;
        ArrayList<URI> ret_val = new ArrayList<>();

        if (uri.getPath().equals(root_directory_.getPath() + "/"))
        {
            ret_val.add(URI.create(NEW_ALBUM_));
            current_directory_ = root_directory_;
        }
        else {
            ret_val.add(URI.create(BACK_TO_ROOT_));
            current_directory_ = new File(uri);
        }
        File[] all_children = current_directory_.listFiles(); //Later we will implement a Filter


        Collections.sort(Arrays.asList(all_children), new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.isDirectory() && rhs.isDirectory())
                    return 0;
                if (lhs.isDirectory())
                    return -1;
                else
                    return 1;
            }
        });

        for (File current_file : all_children)
        {
            ret_val.add(current_file.toURI());
        }
        return ret_val;
    }
}
