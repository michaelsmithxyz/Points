/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.warps;

import com.pvminecraft.points.db.Row;
import com.pvminecraft.points.utils.Locations;
import org.bukkit.Location;
import org.bukkit.Server;

/**
 *
 * @author michael
 */
public class Warp {
    private Location location;
    private String name;
    
    protected Warp(Location location, String name) {
        this.location = location;
        this.name = name;
    }
    
    /**
     * The static factory method for creating warps
     * 
     * @param location The target location of the warp
     * @param name The name of the warp
     * @return The created warp
     */
    public static Warp createWarp(Location location, String name) {
        return new Warp(location, name);
    }
    
    public Location getTarget() {
        return location;
    }
    
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
