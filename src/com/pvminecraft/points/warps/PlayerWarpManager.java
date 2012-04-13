package com.pvminecraft.points.warps;

import com.pvminecraft.FlatDB.FlatDB;
import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.log.Level;
import com.pvminecraft.points.log.Stdout;
import java.util.*;
import org.bukkit.entity.Player;

public class PlayerWarpManager {
    private Points plugin;
    private HashMap<String, List<OwnedWarp>> warps;
    private String directory;
    
    public PlayerWarpManager(Points pl) {
        warps = new HashMap<String, List<OwnedWarp>>();
        plugin = pl;
        directory = plugin.getDataFolder().getPath();
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
        Points.teleportTo(player, warp.getTarget());
    }

    public void load() {
        String player, dbFile;
        FlatDB playersDB = new FlatDB(directory, "players.db");
        List<Row> playerRows =  playersDB.getAll();
        for(Row r : playerRows) {
            player = r.getIndex();
            dbFile = r.getElement("db");
            Stdout.println("Loading Player: " + player, Level.DEBUG);
            loadPlayer(player, directory, dbFile);
        }
    }
    
    public void save() {
        for(Map.Entry<String, List<OwnedWarp>> entry : warps.entrySet()) {
            String playerName = entry.getKey();
            savePlayer(playerName, directory);
        }
    }
    
    //Private Utility Methods
    //Mainly for Loading and Saving

    private void loadPlayer(String player, String dir, String dbFile) {
        OwnedWarp warp;
        List<OwnedWarp> playerWarps = new ArrayList<OwnedWarp>();
        FlatDB database = new FlatDB(dir, dbFile);
        List<Row> rows = database.getAll();
        for(Row r : rows) {
            try {
                warp = OwnedWarp.fromRow(r, plugin.getServer(), player);
            } catch(NullPointerException e) {
                Stdout.println("Couldn't Warp " + player + "/" + r.getIndex(), Level.ERROR);
                continue;
            }
            playerWarps.add(warp);
        }
        warps.put(player, playerWarps);
    }

    private void savePlayer(String name, String dir) {
        if(!warps.containsKey(name))
            return;
        List<OwnedWarp> plWarps = warps.get(name);
        FlatDB warpsDB = new FlatDB(dir, name + ".db");
        Row warpRow;
        FlatDB playerDB = new FlatDB(dir, "players.db");
        // Add the player to the players database if they aren't already there.
        if(!playerDB.hasKey(name)) {
            Row r = new Row(name);
            r.addElement("db", name + ".db");
            playerDB.addRow(r);
            playerDB.update();
        }
        for(OwnedWarp w : plWarps) {
            try {
                warpRow = OwnedWarp.toRow(w);
            } catch(NullPointerException e) {
                Stdout.println("Couldn't save warp " + name + "/" + w.getName(), Level.ERROR);
                continue;
            }
            warpsDB.addRow(warpRow);
        }
        warpsDB.update();
        cleanWarps(name, warpsDB);
    }

    private void cleanWarps(String name, FlatDB warpsDB) {
        // Remove deleted warps from the database
        List<OwnedWarp> plWarps = warps.get(name);
        List<String> warpNames = new ArrayList<String>();
        for(OwnedWarp warp : plWarps)
            warpNames.add(warp.getName());
        for(Row row : warpsDB.getAll())
            if(!warpNames.contains(row.getIndex()))
                warpsDB.removeRow(row.getIndex());
        warpsDB.update();
    }
}