package com.franco.general.couple.data;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;

public class DiaryEntry{
    public long id_;
    public Date date_;
    public String entry_;
    public Uri uri_to_file_ = null;

    public DiaryEntry(long id, Date p_date, String p_entry, Uri p_uri_to_file)
    {
        id_ = id;
        date_ = p_date;
        entry_ = p_entry;
        uri_to_file_ = p_uri_to_file;
    }
}
