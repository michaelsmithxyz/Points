/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.Points;

import com.pvminecraft.FlatDB.FlatDB;
import com.pvminecraft.FlatDB.Row;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author s0lder
 */
public class Homes {
    Points plugin;
    String dataDir;
    FlatDB database;

    public Homes(String dir, Points instance) {
        dataDir = dir;
        plugin = instance;
        database = new FlatDB(dataDir, "homes.db");
    }
    
    public void doHome(Player player) {
        Row row = database.getRow(player.getName());
        if(row == null) {
            player.sendMessage(ChatColor.RED + "You must first set your home!");
            return;
        }
        Location home = plugin.toLocation(row.getElement("x"), row.getElement("y"),
                                   row.getElement("z"), row.getElement("yaw"), 
                                   row.getElement("pitch"), row.getElement("world"));
        if(home != null) {
            player.teleport(home);
        }
    }
    
    public void setHome(Player player) {
        Location home = player.getLocation();
        Row newHome =  new Row(player.getName());
        newHome.addElement("x", String.valueOf(home.getX()));
        newHome.addElement("y", String.valueOf(home.getY()));
        newHome.addElement("z", String.valueOf(home.getZ()));
        newHome.addElement("yaw", String.valueOf(home.getYaw()));
        newHome.addElement("pitch", String.valueOf(home.getPitch()));
        newHome.addElement("world", home.getWorld().getName());
        database.addRow(player.getName(), newHome);
        player.sendMessage(ChatColor.GREEN + "Your home has been set!");
    }
    
    public void saveAll() {
        database.update();
    }
}
