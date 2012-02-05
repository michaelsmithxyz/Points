package com.pvminecraft.points.warps;

import com.pvminecraft.FlatDB.FlatDB;
import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.Points;
import java.util.*;
import org.bukkit.entity.Player;

/**
 *
 * @author s0lder
 */
public class PlayerWarpManager implements WarpManager {
    private Points plugin;
    private HashMap<String, List<Warp>> warps;
    private String directory;
    
    public PlayerWarpManager(Points pl) {
        warps = new HashMap<String, List<Warp>>();
        plugin = pl;
        directory = plugin.getDataFolder().getPath();
    }
    
    @Override
    public void addWarp(Warp warp) {
        String player = warp.getOwner();
        List<Warp> plWarps = warps.get(player);
        if(plWarps == null) {
            plWarps = new ArrayList<Warp>();
            warps.put(player, plWarps);
        }
        plWarps.add(warp);
    }
    
    @Override
    public Warp getWarp(String player, String warpName) {
        List<Warp> list = warps.get(player);
        if(list == null)
            return null;
        for(Warp w : list)
            if(w.getName().equalsIgnoreCase(warpName))
                return w;
        return null;
    }
    
    @Override
    public void removeWarp(Warp warp) {
        String player = warp.getOwner();
        List<Warp> plWarps = warps.get(player);
        if(plWarps == null)
            return;
        plWarps.remove(warp);
    }
    
    @Override
    public List<Warp> getWarps(String player) {
        return warps.get(player);
    }
    
    @Override
    public List<Warp> getAll() {
        List<Warp> ret = new ArrayList<Warp>();
        Set<String> keyset = warps.keySet();
        for(String key : keyset)
            ret.addAll(warps.get(key));
        return ret;
    }

    @Override
    public void sendTo(Player player, Warp warp) {
        if(warp == null)
            return;
        player.teleport(warp.getTarget());
    }

    @Override
    public void load() {
        String player, dbFile;
        FlatDB playersDB = new FlatDB(directory, "players.db");
        List<Row> playerRows =  playersDB.getAll();
        for(Row r : playerRows) {
            player = r.getIndex();
            dbFile = r.getElement("db");
            System.out.println("[Points] Loading Player: " + player);
            loadPlayer(player, directory, dbFile);
        }
    }
    
    @Override
    public void save() {
        for(Map.Entry<String, List<Warp>> entry : warps.entrySet()) {
            String playerName = entry.getKey();
            savePlayer(playerName, directory);
        }
    }
    
    //Private Utility Methods
    //Mainly for Loading and Saving

    private void loadPlayer(String player, String dir, String dbFile) {
        Warp warp;
        List<Warp> playerWarps = new ArrayList<Warp>();
        FlatDB database = new FlatDB(dir, dbFile);
        List<Row> rows = database.getAll();
        for(Row r : rows) {
            try {
                warp = Warp.fromRow(r, plugin, player);
            } catch(NullPointerException e) {
                System.err.println("[Points] Error Loading Warp "
                        + player + "/" + r.getIndex());
                continue;
            }
            playerWarps.add(warp);
        }
        warps.put(player, playerWarps);
    }

    private void savePlayer(String name, String dir) {
        if(!warps.containsKey(name))
            return;
        List<Warp> plWarps = warps.get(name);
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
        for(Warp w : plWarps) {
            try {
                warpRow = Warp.toRow(w);
            } catch(NullPointerException e) {
                System.err.println("[Points] Error saving warp " + name + "/" + w.getName());
                continue;
            }
            warpsDB.addRow(warpRow);
        }
        warpsDB.update();
        cleanWarps(name, warpsDB);
    }

    private void cleanWarps(String name, FlatDB warpsDB) {
        // Remove deleted warps from the database
        List<Warp> plWarps = warps.get(name);
        List<String> warpNames = new ArrayList<String>();
        for(Warp warp : plWarps)
            warpNames.add(warp.getName());
        for(Row row : warpsDB.getAll())
            if(!warpNames.contains(row.getIndex()))
                warpsDB.removeRow(row.getIndex());
        warpsDB.update();
    }
}