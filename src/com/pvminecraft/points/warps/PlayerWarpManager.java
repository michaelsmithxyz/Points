package com.pvminecraft.points.warps;

import com.pvminecraft.FlatDB.FlatDB;
import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.Points;
import java.io.File;
import java.util.*;
import org.bukkit.entity.Player;

public class PlayerWarpManager {
    private Points plugin;
    private HashMap<String, List<OwnedWarp>> warps;
    private File config;
    
    public PlayerWarpManager(Points pl, File config) {
        warps = new HashMap<String, List<OwnedWarp>>();
        plugin = pl;
        this.config = config;
    }
    
    /**
     * Adds the provided warp to the warp table.
     * 
     * @param warp the warp to add
     */
    public void addWarp(OwnedWarp warp) {
        String player = warp.getOwner();
        List<OwnedWarp> plWarps = warps.get(player);
        if(plWarps == null) {
            plWarps = new ArrayList<OwnedWarp>();
            warps.put(player, plWarps);
        }
        plWarps.add(warp);
    }
    
    /**
     * Returns a warp from the given player's warp list.
     * 
     * @param player the owner of the desired warp
     * 
     * @param warpName the name of the warp to be returned
     * 
     * @return the warp specified by the given player and name
     */
    public OwnedWarp getWarp(String player, String warpName) {
        List<OwnedWarp> list = warps.get(player);
        if(list == null)
            return null;
        for(OwnedWarp w : list)
            if(w.getName().equalsIgnoreCase(warpName))
                return w;
        return null;
    }
    
    /**
     * Removes the provided warp from the warp table.
     * 
     * @param warp the warp to be removed
     */
    public void removeWarp(OwnedWarp warp) {
        String player = warp.getOwner();
        List<OwnedWarp> plWarps = warps.get(player);
        if(plWarps == null)
            return;
        plWarps.remove(warp);
    }
    
    /**
     * Returns the provided player's warp list.
     * 
     * @param player the player whose list is to be returned
     * 
     * @return the provided player's warp list
     */
    public List<OwnedWarp> getWarps(String player) {
        return warps.get(player);
    }
    
    /**
     * Returns a collective list of every player's warps
     * 
     * @return all warps in the warp table
     */
    public List<OwnedWarp> getAll() {
        List<OwnedWarp> ret = new ArrayList<OwnedWarp>();
        Set<String> keyset = warps.keySet();
        for(String key : keyset)
            ret.addAll(warps.get(key));
        return ret;
    }
    
    /**
     * Returns the warp table
     * 
     * @return the warp table
     */
    public HashMap<String, List<OwnedWarp>> getTable() {
        return warps;
    }

    /**
     * A utility method to send a player to a warp
     * 
     * @param player the player to be teleported
     * 
     * @param warp the warp to be teleported to
     */
    public static void sendTo(Player player, OwnedWarp warp) {
        if(warp == null)
            return;
        player.teleport(warp.getTarget());
    }

    public void load() {
        
    }
    
    public void save() {
        
    }
}