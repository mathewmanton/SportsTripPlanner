package com.example.sportstripplanner;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

public class Team {
    public String name;
    public LatLng arenaPosition;
    public String arenaName;
    public BitmapDescriptor teamIcon;
    private int iconId;

    public Team(String name, LatLng arenaPosition, String arenaName, int iconId)
    {

        this.name = name;
        this.arenaPosition = arenaPosition;
        this.arenaName = arenaName;
        this.iconId = iconId;
        teamIcon = BitmapDescriptorFactory.fromResource(iconId);
    }

}
