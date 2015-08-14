package com.franco.general.tournament;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by franco on 13.07.15.
 */
public class Tournament {

    private Map<String, Integer> group1_;
    private Map<String, Integer> group2_;
    private LinkedList<Map.Entry<String,String>> pairings_group1_;
    private LinkedList<Map.Entry<String,String>> pairings_group2_;

    private LinkedList<Map.Entry<String,String>> semi_final;

    private boolean is_group1_ = true;
    private int game_one_ = 0;
    private int game_two_ = 0;

    private String waiting = null;

    private Tournament(ArrayList<String> group1, ArrayList<String> group2)
    {
        group1_ = new HashMap<>();
        group2_ = new HashMap<>();

        for (String current : group1)
        {
            group1_.put(current, 0);
        }

        for (String current : group2)
        {
            group2_.put(current, 0);
        }
        pairings_group1_ = new LinkedList<>();

        pairings_group1_.addAll(getAllGames(group1));
        Collections.shuffle(pairings_group1_);

        pairings_group2_ = new LinkedList<>();
        pairings_group2_.addAll(getAllGames(group2));
        Collections.shuffle(pairings_group2_);
    }

    public LinkedList<Map.Entry<String,String>> getPairings_group1_()
    {
        return pairings_group1_;
    }
    public LinkedList<Map.Entry<String,String>> getPairings_group2_()
    {
        return pairings_group2_;
    }

    public void shufflePairings(int group)
    {
        if (group == 1)
        {
            Collections.shuffle(pairings_group1_);
        }
        else if (group == 2)
        {
            Collections.shuffle(pairings_group2_);
        }
        else
            return;
    }

    private LinkedList<Map.Entry<String,String>> getAllGames(ArrayList<String> set)
    {
        if (set.size() == 1) return new LinkedList<>();
        ArrayList<String> copy_of_set = (ArrayList) set.clone();
        LinkedList<Map.Entry<String,String>> ret_val = new LinkedList<>();
        String first = set.get(0);
        copy_of_set.remove(0);
        ret_val.addAll(getAllGames(copy_of_set));

        for (String current : copy_of_set)
        {
            ret_val.add(new AbstractMap.SimpleEntry<>(first,current));
        }

        return ret_val;
    }

    public static Tournament newGame(ArrayList<String> group1, ArrayList<String> group2)
    {
        return new Tournament(group1,group2);
    }

    public Map<String, Integer> getGroup(short group_nr)
    {
        if (group_nr == 1)
            return sortByComparator(group1_);
        else if (group_nr == 2)
            return sortByComparator(group2_);
        else
            return null;
    }
    public Map.Entry<String,String> nextGame()
    {
        Map.Entry<String,String> ret_val;
        if (is_group1_)
        {
            ret_val = pairings_group1_.get(game_one_);
        }
        else
        {
            ret_val = pairings_group2_.get(game_two_);
        }
        return ret_val;
    }

    public Map.Entry<String,String> nextFinal()
    {
        return semi_final.getFirst();
    }
    public boolean winnerFinal(String winner)
    {
        if (waiting == null) waiting = winner;
        else
        {
            semi_final.add(new AbstractMap.SimpleEntry<>(waiting,winner));
            waiting = null;
        }
        semi_final.removeFirst();
        if (semi_final.isEmpty()) return true;
        return false;
    }

    public boolean winner(String my_winner)
    {
        if (is_group1_)
        {
            game_one_++;
            group1_.put(my_winner,group1_.get(my_winner) + 1);
        }
        else {
            game_two_++;
            group2_.put(my_winner, group2_.get(my_winner) + 1);
        }
        if (game_one_ < pairings_group1_.size() && game_two_ < pairings_group2_.size())
        {
            is_group1_ = !is_group1_;
        }
        else if (game_one_ < pairings_group1_.size())
        {
            is_group1_ = true;
        }
        else if (game_two_ < pairings_group2_.size())
        {
            is_group1_ = false;
        }
        else if (game_one_ == pairings_group1_.size() && pairings_group2_.size() == game_two_)
            return true;
        return false;
    }



    public void startFinals()
    {
        semi_final = new LinkedList<>();
        group1_ = sortByComparator(group1_);
        group2_ = sortByComparator(group2_);

        semi_final.add(new AbstractMap.SimpleEntry<>((String)group1_.keySet().toArray()[0],(String)group2_.keySet().toArray()[1]));
        semi_final.add(new AbstractMap.SimpleEntry<>((String)group1_.keySet().toArray()[1],(String)group2_.keySet().toArray()[0]));
    }
    public LinkedList<Map.Entry<String,String>> showSemiFinals()
    {
        return semi_final;
    }

    private Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {

        // Convert Map to List
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // Convert sorted map back to a Map
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
