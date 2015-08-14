package com.franco.general.couple.main.Fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.franco.general.couple.FilesAndPictures.PhotoManager;
import com.franco.general.couple.R;
import com.franco.general.couple.main.MainActivity;
import com.franco.general.couple.main.helper.GalleryAdapter;
import com.franco.general.couple.main.helper.MyItemDecoration;

import java.net.URI;
import java.util.ArrayList;

public class GalleryFragment extends android.support.v4.app.Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number_gallery";

    public PhotoManager photo_manager_ = null;

    public ArrayList<URI> my_dataset_ = null;
    public RecyclerView my_recycler_view_ = null;
    public GalleryAdapter my_adapter = null;

    private static MainActivity context_;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GalleryFragment newInstance(int sectionNumber, MainActivity context) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        context_ = context;
        return fragment;
    }

    public void setUpDataSet()
    {
        ArrayList<URI> children_root = photo_manager_.getChildURI(photo_manager_.current_directory_.toURI());
        if (children_root == null)
        {
            return;
        }
        my_dataset_.clear();
        my_dataset_.addAll(children_root); // Gets all Files in the Root Directory
    }

    public GalleryFragment() {
        my_dataset_ = new ArrayList<>();
    }


    private void setUpRecyclerView()
    {
        //TODO Only get 20-30 Entrys and when User want more load more
        //TODO Allow only 150 characters
        //TODO put this in an AsynkTask when starting

        my_adapter = new GalleryAdapter(context_, my_dataset_,my_recycler_view_);
        my_recycler_view_.setAdapter(my_adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        my_recycler_view_ = (RecyclerView) rootView.findViewById(R.id.photoList);

        my_recycler_view_.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        my_recycler_view_.setLayoutManager(gridLayoutManager);
        my_recycler_view_.addItemDecoration(new MyItemDecoration());

        setUpRecyclerView();
        return rootView;
    }
}