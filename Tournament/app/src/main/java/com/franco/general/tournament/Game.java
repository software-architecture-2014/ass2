package com.franco.general.tournament;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.AbstractMap;
import java.util.Deque;
import java.util.Map;
import java.util.Stack;


public class Game extends ActionBarActivity {
    private boolean started_ = false;

    private String player1_;
    private String player2_;

    private TextView txt_player1_;
    private TextView txt_player2_;

    private int points_1_ = 501;
    private int points_2_ = 501;

    private Game current_game = this;

    private boolean player1_turn_ = false;

    private TextView last1_;
    private TextView last2_;

    private RelativeLayout my_layout;

    private TextView txtCurrentPlayer;

    private Stack<Map.Entry<TextView,Integer>> player1_history_;
    private Stack<Map.Entry<TextView,Integer>> player2_history_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (started_) return;
        started_ = true;
        Intent my_intent = getIntent();
        player1_ = my_intent.getStringExtra("Player1");
        player2_ = my_intent.getStringExtra("Player2");

        final String[] players = {player1_, player2_};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Who should start?");
        builder.setSingleChoiceItems(players, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (item == 1)
                {
                    String tmp = player1_;
                    player1_ = player2_;
                    player2_ = tmp;
                }
                setUpGame();
                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void setUpGame()
    {
        txt_player1_ = (TextView) findViewById(R.id.player1);
        txt_player2_ = (TextView) findViewById(R.id.player2);

        txt_player1_.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_2_material));
        txt_player1_.setTextColor(Color.BLACK);
        txt_player2_.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_2_material));
        txt_player2_.setTextColor(Color.BLACK);

        last1_ = (TextView) findViewById(R.id.start1);
        last2_ = (TextView) findViewById(R.id.start2);

        last1_.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_2_material));
        last2_.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_2_material));
        last1_.setTextColor(Color.BLACK);
        last2_.setTextColor(Color.BLACK);

        txt_player1_.setText(player1_);
        txt_player2_.setText(player2_);

        my_layout = (RelativeLayout) findViewById(R.id.currentGame);

        txtCurrentPlayer = (TextView) findViewById(R.id.txtCurrentPlayer);

        player1_history_ = new Stack<>();
        player2_history_ = new Stack<>();

        setTxt();
    }

    public void nextThrow(View view)
    {
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        if (player1_turn_)
            myBuilder.setTitle(player1_ + " threw:");
        else
            myBuilder.setTitle(player2_ + " threw:");

        myBuilder.setView(input);

        myBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tmp = input.getText().toString();
                if (tmp == null || tmp.length() == 0 || tmp.equals("")) {
                    Toast.makeText(getApplicationContext(), "Really?", Toast.LENGTH_LONG).show();
                    return;
                }
                int val = Integer.valueOf(tmp);
                saveInput(val);
            }

            private void saveInput(int input) {

                TextView new_txt = new TextView(getApplicationContext());
                int new_points = 0;
                if (player1_turn_) {
                    new_points = points_1_ - input;
                    if (new_points < 0 || new_points == 1) {
                        passThrow(null);
                        return;
                    }
                    new_txt.setText(String.valueOf(new_points));
                    player1_history_.push(new AbstractMap.SimpleEntry<>(last1_,points_1_));
                    points_1_ = new_points;
                } else {
                    new_points = points_2_ - input;
                    if (new_points < 0 || new_points == 1) {
                        passThrow(null);
                        return;
                    }
                    player2_history_.push(new AbstractMap.SimpleEntry<>(last2_,points_2_));
                    points_2_ = new_points;
                    new_txt.setText(String.valueOf(new_points));
                }

                new_txt.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_2_material));
                new_txt.setTextColor(Color.BLACK);
                new_txt.setId(View.generateViewId());

                if (player1_turn_) {
                    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

                    p.addRule(RelativeLayout.BELOW, last1_.getId());

                    last1_ = new_txt;

                    new_txt.setLayoutParams(p);
                    my_layout.addView(new_txt);
                } else {
                    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);


                    p.addRule(RelativeLayout.BELOW, last2_.getId());
                    last2_ = new_txt;

                    p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    new_txt.setLayoutParams(p);
                    my_layout.addView(new_txt);
                }
                if (new_points == 0) {
                    Intent result = new Intent();
                    if (player1_turn_)
                        result.putExtra("Result", player1_);
                    else
                        result.putExtra("Result", player2_);
                    current_game.setResult(RESULT_OK, result);
                    current_game.finish();
                }
                setTxt();
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

    private void updateHistory()
    {
        if (player1_turn_)
            player1_history_.push(new AbstractMap.SimpleEntry<>(last1_,points_1_));
        else
            player2_history_.push(new AbstractMap.SimpleEntry<>(last2_, points_2_));
    }

    public void passThrow(View view)
    {
        TextView new_txt = new TextView(getApplicationContext());
        new_txt.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_2_material));
        new_txt.setTextColor(Color.BLACK);
        new_txt.setId(View.generateViewId());

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (player1_turn_)
        {
            p.addRule(RelativeLayout.BELOW, last1_.getId());
            new_txt.setText(String.valueOf(points_1_));
            player1_history_.push(new AbstractMap.SimpleEntry<>(last1_,points_1_));
            last1_ = new_txt;
        }
        else
        {
            p.addRule(RelativeLayout.BELOW, last2_.getId());
            p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            new_txt.setText(String.valueOf(points_2_));
            player2_history_.push(new AbstractMap.SimpleEntry<>(last2_,points_2_));
            last2_ = new_txt;
        }
        new_txt.setLayoutParams(p);
        my_layout.addView(new_txt);
        setTxt();
    }

    private void setTxt()
    {
        player1_turn_ = !player1_turn_;
        if (player1_turn_) {
            txtCurrentPlayer.setText(player1_);
        }
        else
        {
            txtCurrentPlayer.setText(player2_);
        }
    }

    public void setRest(View v)
    {
        final TextView new_txt = new TextView(this);
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        if (player1_turn_)
            myBuilder.setTitle(player1_ + " rest:");
        else
            myBuilder.setTitle(player2_ + " rest:");

        myBuilder.setView(input);

        myBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tmp = input.getText().toString();
                if (tmp == null || tmp.length() == 0 || tmp.equals("")) {
                    Toast.makeText(getApplicationContext(), "Really?", Toast.LENGTH_LONG).show();
                    return;
                }
                int new_val = Integer.valueOf(tmp);
                if (new_val < 2)
                {
                    Toast.makeText(getApplicationContext(),"Not possible!",Toast.LENGTH_LONG).show();
                    return;
                }
                updateHistory();
                if (player1_turn_) {
                    points_1_ = new_val;
                }
                else {
                    points_2_ = new_val;
                }
                new_txt.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_2_material));
                new_txt.setTextColor(Color.BLACK);
                new_txt.setId(View.generateViewId());
                new_txt.setText(tmp);
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                if (player1_turn_) {
                    p.addRule(RelativeLayout.BELOW, last1_.getId());
                    last1_ = new_txt;
                }
                else
                {
                    p.addRule(RelativeLayout.BELOW, last2_.getId());
                    p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    last2_ = new_txt;
                }

                new_txt.setLayoutParams(p);
                my_layout.addView(new_txt);
                setTxt();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    private void scoreBack()
    {
        Map.Entry<TextView,Integer> last_pair;
        if (!player1_turn_)
        {
            if (player1_history_.size() != 0) {
                my_layout.removeView(last1_);
                last_pair = player1_history_.pop();
                points_1_ = last_pair.getValue();
                last1_ = last_pair.getKey();
                setTxt();
            }
        }
        else {
            if (player2_history_.size() != 0) {
                my_layout.removeView(last2_);
                last_pair = player2_history_.pop();
                points_2_ = last_pair.getValue();
                last2_ = last_pair.getKey();
                setTxt();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.scoreBack)
        {
            scoreBack();
        }
        else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
