/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.Points.commands;

import com.pvminecraft.FlatDB.FlatDB;
import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.Points.Points;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author s0lder
 */
public class Homes implements CommandExecutor {
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
        database.addRow(newHome);
        player.sendMessage(ChatColor.GREEN + "Your home has been set!");
    }
    
    public void saveAll() {
        database.update();
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
        if(!cs.hasPermission("points.home")) {
            cs.sendMessage(ChatColor.RED + "You don't have permission to do that!");
            return true;
        }
        if("home".equalsIgnoreCase(cmnd.getName())) {
            doHome((Player) cs);
            return true;
        } else if("sethome".equalsIgnoreCase(cmnd.getName())) {
            setHome((Player) cs);
            return true;
        } else {
            return false;
        }
    }
}
