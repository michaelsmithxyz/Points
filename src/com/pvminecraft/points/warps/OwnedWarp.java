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
    
    public static OwnedWarp createWarp(Location location, String name, String owner) {
        OwnedWarp warp = new OwnedWarp(location, name);
        warp.setOwner(owner);
        return warp;
    }
    
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
    
    public void setOwner(String owner) {
        this.owner = owner;
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
    
}
