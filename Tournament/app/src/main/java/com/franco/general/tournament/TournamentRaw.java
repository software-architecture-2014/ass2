package com.franco.general.tournament;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public interface TournamentRaw {
    void save();
    void load(File instance);
    void newGame(ArrayList<String> group1, ArrayList<String> group2);
    Map<String, Integer> getGroup(short group_nr);
    void nextGame();
}
