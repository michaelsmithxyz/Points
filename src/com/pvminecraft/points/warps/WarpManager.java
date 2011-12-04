package com.pvminecraft.points.warps;

import com.pvminecraft.FlatDB.FlatDB;
import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.Points;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;

/**
 *
 * @author s0lder
 */
public class WarpManager {
    private Points plugin;
    private HashMap<String, List<Warp>> warps;
    
    public WarpManager(Points pl) {
        warps = new HashMap<String, List<Warp>>();
        plugin = pl;
    }    
    
    public Warp getWarp(Player pl, String warpName) {
        return getWarp(pl.getName(), warpName);
    }
    
    public Warp getWarp(String pl, String warpName) {
        List<Warp> list = warps.get(pl);
        if(list == null)
            return null;
        for(Warp w : list) {
            if(w.getName().equalsIgnoreCase(warpName))
                return w;
        }
        return null;
    }
    
    public List<Warp> getWarps(Player pl) {
        return warps.get(pl.getName());
    }
    
    public List<Warp> getWarps(String pl) {
        return warps.get(pl);
    }
    
    public void addWarp(Player pl, Warp w) {
        List<Warp> plWarps = warps.get(pl.getName());
        if(plWarps == null) {
            plWarps = new ArrayList<Warp>();
            warps.put(pl.getName(), plWarps);
        }
        plWarps.add(w);
    }

    public void removeWarp(Player pl, Warp w) {
        List<Warp> plWarps = warps.get(pl.getName());
        if(plWarps == null)
            return;
        plWarps.remove(w);
    }
    
    public boolean sendTo(Player pl, Warp warp) {
        if(warp == null)
            return false;
        pl.teleport(warp.getTarget());
        return true;
    }
    
    public boolean sendTo(Player pl, String warp) {
        Warp target = getWarp(pl, warp);
        return sendTo(pl, target);
    }

    public HashMap<String, List<Warp>> getWarpTable() {
        return warps;
    }

    // Saving and Loading Routines

    public void loadWarps(String dir) {
        String player, dbFile;
        FlatDB playersDB = new FlatDB(dir, "players.db");
        List<Row> playerRows =  playersDB.getAll();
        for(Row r : playerRows) {
            player = r.getIndex();
            dbFile = r.getElement("db");
            System.out.println("[Points] Loading Player: " + player);
            loadPlayer(player, dir, dbFile);
        }
    }

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

    public void savePlayers(String dir) {
        for(Map.Entry<String, List<Warp>> entry : warps.entrySet()) {
            String playerName = entry.getKey();
            savePlayer(playerName, dir);
        }
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
        // Remove deleted warps from the database
        List<String> warpNames = new ArrayList<String>();
        for(Warp warp : plWarps)
            warpNames.add(warp.getName());
        for(Row row : warpsDB.getAll())
            if(!warpNames.contains(row.getIndex()))
                warpsDB.removeRow(row.getIndex());
        warpsDB.update();
    }
}