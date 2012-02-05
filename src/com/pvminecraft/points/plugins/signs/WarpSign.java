/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.plugins.signs;

import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.utils.Locations;
import com.pvminecraft.points.warps.OwnedWarp;
import com.pvminecraft.points.warps.PlayerWarpManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author s0lder
 */
public class WarpSign {
    private Sign sign;
    private OwnedWarp target;
    private Location location;
    
    public WarpSign(Sign block, OwnedWarp warp, Location loc) {
        sign = block;
        target = warp;
        location = loc;
    }
    
    public static WarpSign fromRow(Row row, JavaPlugin pl, PlayerWarpManager wm) {
        Location loc = Locations.fromRow(row, pl.getServer());
        Block bl = loc.getBlock();
        if(!(bl.getState() instanceof Sign))
            return null;
        Sign sign = (Sign) bl.getState();
        String wName = row.getElement("warp");
        OwnedWarp warp = wm.getWarp(wName.split(";")[0], wName.split(";")[1]);
        if(warp == null)
            return null;
        return new WarpSign(sign, warp, loc);
    }
    
    public Sign getSign() {
        return sign;
    }
    
    public OwnedWarp getTarget() {
        return target;
    }
    
    public Location getLocation() {
        return location;
    }
}
