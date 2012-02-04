/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.warps;

import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.utils.Locations;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author s0lder
 */
public class Warp {
    private Location target;
    private String owner;
    private boolean visible = false;
    private String name;
    
    public Warp(Location to, String owner, String name) {
        target = to;
        this.owner = owner;
        this.name = name;
    }
    
    protected void setOwner(String owner) {
        this.owner = owner;
    }
    
    public static Warp fromRow(Row row, JavaPlugin pl, String owner) {
        Location loc = Locations.fromRow(row, pl);
        boolean vis = "true".equals(row.getElement("visible"))?true:false;
        return new Warp(loc, owner, row.getIndex(), vis);
    }
    
    public static Row toRow(Warp w) {
        Row r = Locations.locToRow(w.getTarget(), w.getName());
        r.addElement("visible", String.valueOf(w.getVisible()));
        return r;
    }
    
    public Warp(Location to, String owner, String name, boolean vis) {
        this(to, owner, name);
        visible = vis;
    }
    
    public Location getTarget() {
        return target;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public boolean getVisible() {
        return visible;
    }
    
    public void setVisible(boolean vis) {
        visible = vis;
    }
    
    public String getName() {
        return name;
    }
}
