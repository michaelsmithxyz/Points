package com.pvminecraft.points.warps;

import com.pvminecraft.FlatDB.FlatDB;
import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.Points;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GlobalWarpManager implements WarpManager {
    private Points plugin;
    private List<Warp> globals;
    private String directory;
    
    public GlobalWarpManager(Points points) {
        plugin = points;
        directory = plugin.getDataFolder().getPath();
        globals = new ArrayList<Warp>();
    }

    @Override
    public void addWarp(Warp warp) {
        for(Warp w : globals)
            if(w.getName().equalsIgnoreCase(warp.getName()))
                globals.remove(w);
        globals.add(warp);
    }

    @Override
    public Warp getWarp(String player, String name) {
        for(Warp warp : globals)
            if(warp.getName().equalsIgnoreCase(name))
                return warp;
        return null;
    }

    @Override
    public void removeWarp(Warp warp) {
        globals.remove(warp);
    }

    @Override
    public List<Warp> getWarps(String player) {
        return globals;
    }

    @Override
    public List<Warp> getAll() {
        return globals;
    }

    @Override
    public void sendTo(Player player, Warp warp) {
        Location target = warp.getTarget();
        player.teleport(target);
    }

    @Override
    public void load() {
        FlatDB database = new FlatDB(directory, "globals.db");
        List<Row> rows = database.getAll();
        for(Row row : rows) {
            try {
                Warp warp = Warp.fromRow(row, plugin, "server");
                globals.add(warp);
            } catch (NullPointerException e) {
                System.err.println("[Points] Error loading global: " + row.getIndex());
            }
        }
    }

    @Override
    public void save() {
        FlatDB database = new FlatDB(directory, "globals.db");
        for(Warp warp : globals) {
            try {
                Row row = Warp.toRow(warp);
                database.addRow(row);
            } catch(NullPointerException e) {
                System.err.println("[Points] Error saving global: " + warp.getName());
            }
        }
        cleanWarps(database, globals);
        database.update();
    }
    
    private static void cleanWarps(FlatDB database, List<Warp> warps) {
        List<Row> rows = database.getAll();
        List<String> names = new ArrayList<String>();
        for(Warp warp : warps)
            names.add(warp.getName());
        for(Row row : rows)
            if(!(names.contains(row.getIndex())))
                database.removeRow(row.getIndex());
    }
    
}
