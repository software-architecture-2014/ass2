package com.franco.general.couple.main;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.franco.general.couple.FilesAndPictures.PhotoManager;
import com.franco.general.couple.R;
import com.franco.general.couple.data.DiaryEntry;
import com.franco.general.couple.db.AsynkTaskOperations.AsynkDatabaseEdit;
import com.franco.general.couple.db.AsynkTaskOperations.AsynkDatabaseInsert;
import com.franco.general.couple.db.DAO;
import com.franco.general.couple.main.Fragments.GalleryFragment;
import com.franco.general.couple.main.helper.AnimationListenerFAB;
import com.franco.general.couple.main.helper.MyAdapter;
import com.franco.general.couple.main.helper.RecyclerClickListener;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {


    public static DateFormat sdf_ = SimpleDateFormat.getDateInstance();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    public DiaryFragment diaryFragment = null;
    public GalleryFragment galleryFragment = null;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public DAO dao_ = null;

    private FloatingActionButton fab_send_ = null;
    private FloatingActionButton fab_camera_ = null;
    private FloatingActionButton fab_add_ = null;

    private int old_tab_ = 0;

    private static AnimationListenerFAB listen_fab_ = null;

    public static final int NEW_ENTRY = 1337;
    public static final int EDIT_ENTRY = 1338;
    public static final int TAKE_PHOTO_FAB = 1339;

    public PhotoManager photoManager = null;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        photoManager.stopWatchingExternalStorage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO check what things in the style is really needed @values/styles_couple...
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        //TODO put on AsynkTask
        dao_ = DAO.instance(this);
        diaryFragment = DiaryFragment.newInstance(1);
        diaryFragment.my_dataset = dao_.getAllEntrys();

        photoManager = new PhotoManager(this);
        galleryFragment = GalleryFragment.newInstance(1, this);
        galleryFragment.photo_manager_ = photoManager;
        galleryFragment.setUpDataSet();
    }

    public void diaryAction(View v)
    {
        Intent intent = new Intent(this,NewEntry.class);
        startActivityForResult(intent,NEW_ENTRY);
    }

    public void sendAction(View v)
    {
        Toast.makeText(this,"send was pressed", Toast.LENGTH_SHORT).show();
    }

    public void cameraAction(View v)
    {
        try {
            photoManager.takePhoto();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Something strange happend",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       if (id == R.id.action_settings)
       {
           Toast.makeText(this, "Settings was pressed", Toast.LENGTH_SHORT).show();
       }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private void setUpFAB()
    {
        fab_send_ = (FloatingActionButton) findViewById(R.id.fabSend);
        fab_add_ = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab_camera_ = (FloatingActionButton) findViewById(R.id.fabCamera);

        Animation fab_animation_out_ = AnimationUtils.loadAnimation(this, R.anim.abc_fade_out);
        Animation fab_animation_in_= AnimationUtils.loadAnimation(this, R.anim.abc_fade_in);

        fab_animation_in_.setDuration(150);
        fab_animation_out_.setDuration(150);
        listen_fab_ = new AnimationListenerFAB(fab_animation_out_, fab_animation_in_);
        fab_animation_out_.setAnimationListener(listen_fab_);
    }

    @Override
    public void onTabSelected(final ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        if (fab_send_ == null) {
            setUpFAB();
        }
        else {
            switch (tab.getPosition())
            {
                case 0:
                    listen_fab_.to_show_ = fab_add_;
                    break;
                case 1:
                    listen_fab_.to_show_ = fab_send_;
                    break;
                case 2:
                    listen_fab_.to_show_ = fab_camera_;
                    break;
                default:
            }

           switch (old_tab_)
           {
               case 0:
                   listen_fab_.startAnimation(fab_add_);
                   break;
               case 1:
                   listen_fab_.startAnimation(fab_send_);
                   break;
               case 2:
                   listen_fab_.startAnimation(fab_camera_);
                   break;
               default:
           }
            old_tab_ = tab.getPosition();
        }
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position)
            {
                case 0:
                    return diaryFragment;
                case 2:
                    return galleryFragment;
            }
            return DiaryFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DiaryFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;

        public ArrayList<DiaryEntry> my_dataset = new ArrayList<>();

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DiaryFragment newInstance(int sectionNumber) {
            DiaryFragment fragment = new DiaryFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public DiaryFragment() {

        }

        public void setUpRecyclerView()
        {
            //TODO Only get 20-30 Entrys and when User want more load more
            //TODO Allow only 150 characters
            //TODO put this in an AsynkTask when starting

            mAdapter = new MyAdapter(new RecyclerClickListener(mRecyclerView,(MainActivity)getActivity(),my_dataset),my_dataset);
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.card_fragment_main, container, false);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)

            setUpRecyclerView();

            return rootView;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        //TODO switch not 100 meter if
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == NEW_ENTRY)
        {
            if (resultCode == RESULT_OK)
            {
                String new_date = intent.getStringExtra(NewEntry.KEY_DATE);
                String new_text = intent.getStringExtra(NewEntry.KEY_TEXT);
                String new_uri = intent.getStringExtra(NewEntry.KEY_URI);

                AsynkDatabaseInsert new_insert_task = new AsynkDatabaseInsert(this);
                new_insert_task.execute(new_date, new_text, new_uri);
            }
            else
            {
                Toast.makeText(this,"cancel",Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == EDIT_ENTRY)
        {
            if (resultCode == RESULT_OK)
            {
                String new_date = intent.getStringExtra(NewEntry.KEY_DATE);
                String new_text = intent.getStringExtra(NewEntry.KEY_TEXT);
                String new_uri = intent.getStringExtra(NewEntry.KEY_URI);

                int changed_item = intent.getIntExtra(NewEntry.KEY_EDIT,0);

                long id = diaryFragment.my_dataset.get(changed_item).id_;

                AsynkDatabaseEdit task = new AsynkDatabaseEdit(this, changed_item);
                task.execute(String.valueOf(id), new_date,new_text,new_uri);
            }
            else
            {
                Toast.makeText(this,"cancel",Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == TAKE_PHOTO_FAB)
        {
            if (resultCode == RESULT_OK)
            {
                photoManager.handlePhoto();
            }
            else
            {
                photoManager.deleteTempFile();
                Toast.makeText(this, "No Photo was taken...",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        if (mViewPager.getCurrentItem() == 2 &&
                !photoManager.current_directory_.equals(photoManager.root_directory_))
            galleryFragment.my_adapter.backToRoot();
        else
            super.onBackPressed();

    }
}
