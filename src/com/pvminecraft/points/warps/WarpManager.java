/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.warps;

import com.pvminecraft.FlatDB.FlatDB;
import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.Points;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author s0lder
 */
public class WarpManager {
    private Points plugin;
    private HashMap<String, List<Warp>> warps;
    private HashMap<Location, WarpSign> signs;
    private FlatDB players;
    
    public WarpManager(Points pl) {
        warps = new HashMap<String, List<Warp>>();
        signs = new HashMap<Location, WarpSign>();
        plugin = pl;
    }
    
    public void loadWarps(String dir) {
        players = new FlatDB(dir, "players.db");
        List<Row> playerRows =  players.getAll();
        for(Row r : playerRows) {
            String name = r.getIndex();
            String db = r.getElement("db");
            loadPlayer(name, dir, db);
        }
    }
    
    public void loadPlayer(String name, String dir, String db) {
        List<Warp> playerList = new ArrayList<Warp>();
        FlatDB database = new FlatDB(dir, db);
        List<Row> rows = database.getAll();
        for(Row r : rows) {
            playerList.add(Warp.fromRow(r, plugin, name));
        }
        warps.put(name, playerList);
    }
    
    public void savePlayers(String dir) {
        Iterator it = warps.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            String playerName = (String) pairs.getKey();
            savePlayer(playerName, dir);
        }
    }
    
    public void savePlayer(String name, String dir) {
        List<Warp> plWarps = warps.get(name);
        if(plWarps == null)
            return;
        FlatDB playerDB = new FlatDB(dir, "players.db");
        Row r = new Row(name);
        r.addElement("db", name + ".db");
        playerDB.addRow(r);
        playerDB.update();
        FlatDB warpsDB = new FlatDB(dir, name + ".db");
        for(Warp w : plWarps) {
            Row row = Warp.toRow(w);
            warpsDB.addRow(row);
        }
        warpsDB.update();
    }
    
    public Warp getWarp(Player pl, String warpName) {
        List<Warp> list = warps.get(pl.getName());
        if(list == null)
            return null;
        for(Warp w : list) {
            if(w.getName().equalsIgnoreCase(warpName))
                return w;
        }
        return null;
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
    
    public boolean sendTo(Player pl, Warp warp) {
        pl.teleport(warp.getTarget());
        return true;
    }
    
    public boolean sendTo(Player pl, String warp) {
        Warp target = getWarp(pl, warp);
        if(target == null)
            return false;
        pl.teleport(target.getTarget());
        return true;
    }
}
