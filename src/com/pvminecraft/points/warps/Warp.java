package com.pvminecraft.points.warps;

import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.utils.Locations;
import org.bukkit.Location;
import org.bukkit.Server;

public class Warp {
    private Location location;
    private String name;
    
    // DO NOT USE THIS
    protected Warp(Location location, String name) {
        this.location = location;
        this.name = name;
    }
    
    /**
     * The static factory method for creating warps.
     * 
     * @param location the target location of the warp
     * 
     * @param name the name of the warp
     * 
     * @return the created warp
     */
    public static Warp createWarp(Location location, String name) {
        return new Warp(location, name);
    }
    
    /**
     * Returns the target location of the warp.
     * 
     * @return the location to which the warp points
     */
    public Location getTarget() {
        return location;
    }
    
    /**
     * Returns the name of the warp.
     * 
     * @return the warp's name
     */
    public String getName() {
        return name;
    }
    
    public static Warp fromRow(Row row, Server server) {
        Location location = Locations.fromRow(row, server);
        return new Warp(location, row.getIndex());
    }
    
    public static Row toRow(Warp warp) {
        Row row = Locations.locToRow(warp.getTarget(), warp.getName());
        return row;
    }
}
