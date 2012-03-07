package com.pvminecraft.points.homes;

import com.pvminecraft.FlatDB.FlatDB;
import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.utils.Locations;
import java.util.HashMap;
import java.util.Set;
import org.bukkit.Location;

public class HomeManager {
    private Points plugin;
    private String directory;
    private HashMap<String, Location> homes;

    public HomeManager(Points instance) {
        this.plugin = instance;
        this.directory = plugin.getDataFolder().getPath();
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
        FlatDB db = new FlatDB(directory, "homes.db");
        for(Row row : db.getAll())
            homes.put(row.getIndex(), Locations.fromRow(row, plugin.getServer()));
    }
}
