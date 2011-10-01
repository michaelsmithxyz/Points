package com.pvminecraft.Points;

import com.pvminecraft.FlatDB.FlatDB;
import com.pvminecraft.FlatDB.Row;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author s0lder
 */
public class Warps {
    private HashMap<String,FlatDB> warps;
    String dataDir;
    Points plugin;
    
    public Warps(String dir, Points instance) {
        warps = new HashMap<String, FlatDB>();
        dataDir = dir;
        plugin = instance;
    }
    
    public void loadPlayer(String name) {
        //If player is already loaded
        if(warps.containsKey(name)) {
            return;
        } else {
            FlatDB db;
            db = new FlatDB(dataDir + File.separator + name, name + ".db");
            warps.put(name, db);
        }
    }
    
    public void loadList(String[] names) {
        for(int x = 0; x < names.length; x++) {
            loadPlayer(names[x]);
        }
    }
    
    public void unloadPlayer(String name) {
        saveAll();
        if(warps.containsKey(name)) {
            warps.remove(name);
        }
    }
    
    public boolean doCommand(CommandSender sender, Command command, String[] args) {
        String operation;
        String name;
        try {
            operation = args[0];
            name = args[1];
        } catch(ArrayIndexOutOfBoundsException e) {
            return false;
        }
        Player player = (Player)sender;
        if(operation.equalsIgnoreCase("new")) {
            createNew(player.getName(), name, player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Warp sucessfully created!");
            return true;
        }
        else if(operation.equalsIgnoreCase("go")) {
            FlatDB pWarp = warps.get(player.getName());
            if(pWarp == null) {
                loadPlayer(player.getName());
                pWarp = warps.get(player.getName());
            }
            Row pLoc = pWarp.getRow(name);
            if(pLoc == null) {
                player.sendMessage(ChatColor.RED + "You don't have a warp by that name!");
                return true;
            }
            player.teleport(rowToLocation(pLoc));
            return true;
        }
        return false;
    }
    
    public Location rowToLocation(Row r) {
        World world;
        String x, y, z, yaw, pitch, worldName;
        x = r.getElement("x");
        y = r.getElement("y");
        z = r.getElement("z");
        yaw = r.getElement("yaw");
        pitch = r.getElement("pitch");
        worldName = r.getElement("world");
        
        world = plugin.getServer().getWorld(worldName);
        return new Location(world, Double.parseDouble(x), Double.parseDouble(y), 
                Double.parseDouble(z), Float.parseFloat(yaw), 
                Float.parseFloat(pitch));
    }
    
    public void createNew(String player, String name, Location loc) {
        FlatDB data = warps.get(player);
        Row row = new Row(name);
        row.addElement("x", String.valueOf(loc.getX()));
        row.addElement("y", String.valueOf(loc.getY()));
        row.addElement("z", String.valueOf(loc.getZ()));
        row.addElement("yaw", String.valueOf(loc.getYaw()));
        row.addElement("pitch", String.valueOf(loc.getPitch()));
        row.addElement("world", loc.getWorld().getName());
        data.addRow(row);
    }
    
    public void saveAll() {
        Iterator it = warps.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            FlatDB r = (FlatDB) pairs.getValue();
            r.update();
        }
    }
}
