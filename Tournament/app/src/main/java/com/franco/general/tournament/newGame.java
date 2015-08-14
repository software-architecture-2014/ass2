package com.franco.general.tournament;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.sax.RootElement;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class newGame extends ActionBarActivity {

    private ArrayList<String> group1_;
    private ArrayList<String> group2_;

    private TextView last1_ = null;
    private TextView last2_ = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        group1_ = new ArrayList<>();
        group2_ = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_game_action,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.done)
        {
            if (group1_.size() > 1 && group2_.size() > 1) {
                Intent result = new Intent();
                ArrayList<String> groups = new ArrayList<>();
                for (String current : group1_)
                {
                    groups.add("1" + current);
                }
                for (String current : group2_)
                {
                    groups.add("2" + current);
                }
                result.putExtra("Groups", groups);
                setResult(RESULT_OK, result);
                finish();
            }
            else {
                Toast.makeText(this,"Come on.. At least two in each group...",Toast.LENGTH_LONG).show();
            }
        }
        else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void newPlayer(final View clicked)
    {
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);

        myBuilder.setTitle("Next Player");
        myBuilder.setView(input);

        myBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tmp = input.getText().toString();
                if (tmp == null || tmp.length() == 0 || tmp.equals(""))
                    return;
                saveInput(tmp);
            }

            private void saveInput(String input) {
                RelativeLayout my_layout = (RelativeLayout) findViewById(R.id.newGame);
                TextView new_txt = new TextView(getApplicationContext());
                new_txt.setText(input);
                new_txt.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_2_material));
                new_txt.setTextColor(Color.BLACK);
                new_txt.setId(View.generateViewId());

                if (clicked.getId() == R.id.group1) {
                    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

                    p.addRule(RelativeLayout.BELOW, R.id.group1);
                    if (last1_ == null)
                        p.addRule(RelativeLayout.BELOW, R.id.group1);
                    else {
                        p.addRule(RelativeLayout.BELOW, last1_.getId());
                    }
                    last1_ = new_txt;

                    new_txt.setLayoutParams(p);
                    my_layout.addView(new_txt);
                    group1_.add(input);
                } else {
                    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

                    if (last2_ == null)
                        p.addRule(RelativeLayout.BELOW, R.id.group2);
                    else {
                        p.addRule(RelativeLayout.BELOW, last2_.getId());
                    }
                    last2_ = new_txt;

                    p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    new_txt.setLayoutParams(p);
                    my_layout.addView(new_txt);
                    group2_.add(input);
                }
            }
        });
        myBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        myBuilder.show();


    }
}
