package com.pvminecraft.points.warps;

import com.pvminecraft.FlatDB.Row;
import org.bukkit.Location;
import org.bukkit.Server;

public class OwnedWarp extends Warp {
    private String owner;
    private boolean visible = false;
    
    protected OwnedWarp(Location location, String name) {
        super(location, name);
    }
    
    /**
     * A factory method to create a new warp with ownership.
     * 
     * @param location the location that the warp should point to
     * 
     * @param name the name of the warp
     * 
     * @param owner the owner of the warp
     * 
     * @return the newly created warp
     */
    public static OwnedWarp createWarp(Location location, String name, String owner) {
        OwnedWarp warp = new OwnedWarp(location, name);
        warp.setOwner(owner);
        return warp;
    }
    
    /**
     * A factory method to create a new warp with ownership. Includes the ability
     * to set visibility.
     * 
     * @param location the location that the warp should point to
     * 
     * @param name the name of the warp
     * 
     * @param owner the owner of the warp
     * 
     * @param visible the visibility of the warp (public or private)
     * 
     * @return 
     */
    public static OwnedWarp createWarp(Location location, String name, String owner, boolean visible) {
        OwnedWarp warp = OwnedWarp.createWarp(location, name, owner);
        warp.setVisible(visible);
        return warp;
    }
    
    public static OwnedWarp fromRow(Row row, Server server, String owner) {
        String vis = row.getElement("visible");
        if(vis == null)
            return null;
        Warp warp = Warp.fromRow(row, server);
        OwnedWarp ret = OwnedWarp.createWarp(warp.getTarget(), warp.getName(), owner);
        ret.setOwner(owner);
        ret.setVisible(Boolean.valueOf(vis));
        return ret;
    }
    
    public static Row toRow(OwnedWarp warp) {
        Row row = Warp.toRow(warp);
        row.addElement("visible", String.valueOf(warp.getVisible()));
        return row;
    }
    
    /**
     * Sets the owner of the warp.
     * 
     * @param owner the new owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    /**
     * Returns the owner of the warp
     * 
     * @return the warp's owner
     */
    public String getOwner() {
        return owner;
    }
    
    /**
     * Returns the visibility of the warp.
     * 
     * @return the warp's visibility
     */
    public boolean getVisible() {
        return visible;
    }
    
    /**
     * Sets the visibility of the warp.
     * 
     * @param vis the new visibility
     */
    public void setVisible(boolean vis) {
        visible = vis;
    }
    
}
