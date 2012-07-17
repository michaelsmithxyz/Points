package com.pvminecraft.points.homes;

import com.pvminecraft.points.Points;
import com.pvminecraft.points.storage.Database;
import com.pvminecraft.points.storage.YamlDatabase;
import java.io.File;
import java.util.HashMap;
import java.util.Set;
import org.bukkit.Location;

public class HomeManager {
    private Points plugin;
    private File database;
    private HashMap<String, Location> homes;

    public HomeManager(Points instance, File db) {
        this.database = db;
        this.plugin = instance;
        this.homes = new HashMap<String, Location>();
    }
    
    public void setHome(String player, Location home) {
        if(homes.containsKey(player)) homes.remove(player);
        homes.put(player, home);
    }
    
    public Location getHome(String player) {
        return homes.get(player);
    }
    
    public void save() {
        FlatDB db = new FlatDB(directory, "homes.db");
        Set<String> keys = homes.keySet();
        for(String key : keys)
            db.addRow(Locations.locToRow(homes.get(key), key));
        db.update();
    }
    
    public void load() {
        Database db = new YamlDatabase();
        db.load(database);
    }
}
