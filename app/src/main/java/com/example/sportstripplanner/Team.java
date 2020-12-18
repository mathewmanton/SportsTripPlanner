package com.example.sportstripplanner;

import com.google.android.gms.maps.model.LatLng;

public class Team {
    public String name;
    public LatLng arenaPosition;
    public String arenaName;

    public Team(String name, LatLng arenaposition, String arenaName)
    {
        this.name = name;
        this.arenaPosition = arenaposition;
        this.arenaName = arenaName;
    }

}
