package com.example.sportstripplanner;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class League {
    public String name;
    public HashMap<String,Team> teams;
    public Vector<Game> games;

    public League(String name, HashMap<String, Team> teams)
    {
        this.name = name;
        this.teams = teams;
        this.games = new Vector<Game>();
    }

    public void AddGame(Game game)
    {
        this.games.add(game);
    }


}
