package com.franco.general.tournament;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;


public class TournamentLayout extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private Tournament myGame;
    private boolean first_start = true;

    private boolean finals_ = false;

    private boolean accept_1_ = false;
    private boolean accept_2_ = false;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_layout);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        File myFile = new File("instance");
        if (myFile.exists())
            try {
             //   myGame = load(myFile);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        else
        {
            if (first_start && myGame == null)
            {
                first_start = false;
                startActivityForResult(new Intent(this, newGame.class),0);
            }
        }
    }
    private void save(File myFile) throws IOException
    {
        FileOutputStream fos = this.openFileOutput("save", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(this);
        os.close();
        fos.close();
    }

  /*  private Tournament load(File myFile)
    {

    }*/
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                if (myGame != null) showGroups();
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                if (myGame != null) showPairings(myGame.getPairings_group1_(),1);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                if (myGame != null) showPairings(myGame.getPairings_group2_(),2);
                break;
            case 4:
                if (finals_)
                {
                    mTitle = getString(R.string.finals);
                    showFinals(0);
                }
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.tournament_layout, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_example)
        {
            if (!accept_1_ || !accept_2_) {
                Toast.makeText(this,"You need to accept the pairings!",Toast.LENGTH_LONG).show();
            }
            else
                getNextGame();
        }
        //noinspection SimplifiableIfStatement
        else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final java.lang.String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tournament_layout, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((TournamentLayout) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0)
        {
            if (resultCode == RESULT_OK)
            {
                Bundle extras = intent.getExtras();
                ArrayList<java.lang.String> tmp = extras.getStringArrayList("Groups");
                ArrayList<String> group1 = new ArrayList<>();
                ArrayList<String> group2 = new ArrayList<>();

                for (java.lang.String current : tmp)
                {
                    if (current.startsWith("1"))
                    {
                        group1.add(new String(current.substring(1)));
                    }
                    else if (current.startsWith("2"))
                    {
                        group2.add(new String(current.substring(1)));
                    }
                    else
                    {
                        Toast.makeText(this,"Why? " + current,Toast.LENGTH_LONG).show();
                    }
                }
                myGame = Tournament.newGame(group1,group2);
                showGroups();
            }
            else
            {
                Toast.makeText(this,"FAIL!!!!",Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                String my_winner = intent.getStringExtra("Result");
                if (!finals_) {
                    if (myGame.winner(my_winner)) {
                        myGame.startFinals();
                        finals_ = true;

                    }
                    showGroups();
                }
                else
                {
                    if (myGame.winnerFinal(my_winner))
                    {
                        Toast.makeText(this,"WIINNER IS:   " + my_winner,Toast.LENGTH_LONG).show();
                    }
                }
            }
            else
            {
                Toast.makeText(this,"Something went wrong.. do it again!",Toast.LENGTH_LONG).show();
            }
        }
    }
    private void showGroups()
    {
        if (myGame == null) return;

        TextView last_txt;

        RelativeLayout my_layout = (RelativeLayout) findViewById(R.id.container);
        my_layout.removeAllViews();
        Map<String,Integer> group1 = myGame.getGroup((short) 1);
        Map<String,Integer> group2 = myGame.getGroup((short) 2);

        TextView group1_txt = new TextView(this);
        group1_txt.setText("Group 1");
        group1_txt.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_2_material));
        group1_txt.setTextColor(Color.BLACK);
        group1_txt.setId(View.generateViewId());
        my_layout.addView(group1_txt);

        last_txt = group1_txt;

        for (Map.Entry<String, Integer> entry : group1.entrySet())
        {
            TextView tmp = new TextView(this);
            tmp.setText(entry.getValue() + "   " + entry.getKey());
            tmp.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_2_material));
            tmp.setTextColor(Color.BLACK);
            tmp.setId(View.generateViewId());

            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            p.addRule(RelativeLayout.BELOW, last_txt.getId());
            tmp.setLayoutParams(p);

            my_layout.addView(tmp);
            last_txt = tmp;
        }

        TextView group2_txt = new TextView(this);
        group2_txt.setText("Group 2");
        group2_txt.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_1_material));
        group2_txt.setTextColor(Color.BLACK);
        group2_txt.setId(View.generateViewId());

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        group2_txt.setLayoutParams(p);
        my_layout.addView(group2_txt);

        last_txt = group2_txt;

        for (Map.Entry<String, Integer> entry : group2.entrySet())
        {
            TextView tmp = new TextView(this);
            tmp.setText(entry.getKey().toString() + "   " + entry.getValue());
            tmp.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_1_material));
            tmp.setTextColor(Color.BLACK);
            tmp.setId(View.generateViewId());

            RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            para.addRule(RelativeLayout.BELOW, last_txt.getId());
            para.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tmp.setLayoutParams(para);

            my_layout.addView(tmp);
            last_txt = tmp;
        }

    }

    private void showPairings(final LinkedList<Map.Entry<String,String>> sets, final int group)
    {

        TextView last_txt = null;

        RelativeLayout my_layout = (RelativeLayout) findViewById(R.id.container);

        my_layout.removeAllViews();

        if (group == 1 && !accept_1_)
        {
            Button shuffle = new Button(this);
            shuffle.setText("Shuffle again");
            shuffle.setId(View.generateViewId());
            shuffle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myGame.shufflePairings(1);
                    showPairings(sets,group);
                }
            });
            Button accept = new Button(this);
            accept.setText("Accept pairings");
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accept_1_ = true;
                    showPairings(sets, group);
                }
            });
            RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            para.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            shuffle.setLayoutParams(para);
            my_layout.addView(shuffle);

            RelativeLayout.LayoutParams paraAccept = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            paraAccept.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paraAccept.addRule(RelativeLayout.BELOW, shuffle.getId());
            accept.setLayoutParams(paraAccept);
            my_layout.addView(accept);

        }
        else if (group == 2 && !accept_2_)
        {
            Button shuffle = new Button(this);
            shuffle.setText("Shuffle again");
            shuffle.setId(View.generateViewId());
            shuffle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myGame.shufflePairings(2);
                    showPairings(sets,group);
                }
            });
            Button accept = new Button(this);
            accept.setText("Accept pairings");
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accept_2_ = true;
                    showPairings(sets,group);
                }
            });
            RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            para.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            shuffle.setLayoutParams(para);
            my_layout.addView(shuffle);

            RelativeLayout.LayoutParams paraAccept = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            paraAccept.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paraAccept.addRule(RelativeLayout.BELOW, shuffle.getId());
            accept.setLayoutParams(paraAccept);
            my_layout.addView(accept);
        }

        for (Map.Entry<String,String> current : sets)
        {
            TextView tmp = new TextView(this);
            tmp.setText(current.getKey().toString() + " vs " + current.getValue().toString());
            tmp.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_1_material));
            tmp.setTextColor(Color.BLACK);
            tmp.setId(View.generateViewId());

            RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            if (last_txt != null) para.addRule(RelativeLayout.BELOW, last_txt.getId());
            last_txt = tmp;
            tmp.setLayoutParams(para);

            my_layout.addView(tmp);
        }
    }

    private void getNextGame()
    {
        if (!finals_) {
            Intent new_intent = new Intent(this, Game.class);
            Map.Entry<String, String> tmp = myGame.nextGame();
            new_intent.putExtra("Player1", tmp.getKey());
            new_intent.putExtra("Player2", tmp.getValue());
            startActivityForResult(new_intent, 1);
        }
        else {
            Intent new_intent = new Intent(this, Game.class);
            Map.Entry<String, String> tmp = myGame.nextFinal();
            new_intent.putExtra("Player1", tmp.getKey());
            new_intent.putExtra("Player2", tmp.getValue());
            startActivityForResult(new_intent, 1);
        }
    }

    private void showFinals(int what)
    {
        TextView last_txt = null;

        RelativeLayout my_layout = (RelativeLayout) findViewById(R.id.container);

        my_layout.removeAllViews();
        LinkedList<Map.Entry<String,String>> to_show = null;

        if (what == 0)
        {
            to_show = myGame.showSemiFinals();
        }
        else return;

        for (Map.Entry<String,String> current : to_show)
        {
            TextView tmp = new TextView(this);
            tmp.setText(current.getKey().toString() + " vs " + current.getValue().toString());
            tmp.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_1_material));
            tmp.setTextColor(Color.BLACK);
            tmp.setId(View.generateViewId());

            RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            if (last_txt != null) para.addRule(RelativeLayout.BELOW, last_txt.getId());
            last_txt = tmp;
            tmp.setLayoutParams(para);

            my_layout.addView(tmp);
        }
    }

}
