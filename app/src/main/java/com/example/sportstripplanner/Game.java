package com.example.sportstripplanner;

import java.util.Date;

public class Game {
    public Date datePlayed;
    public Team homeTeam;
    public Team awayTeam;

    public Game(Date datePlayed, Team homeTeam, Team awayTeam)
    {
        this.datePlayed = datePlayed;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

}
