package com.pvminecraft.Points;

import com.pvminecraft.FlatDB.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import java.io.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

public class Points extends JavaPlugin {
    private Warps warps;
    private Homes homes;
    private PluginDescriptionFile info;
    private File dir;
    private World world;
    private PointsPlayerListener playerListener;
    
    @Override
    public void onDisable() {
        System.out.println("[" + info.getName() + "] Disabled.");
        warps.saveAll();
        homes.saveAll();
    }
    
    @Override
    public void onEnable() {
        dir = getDataFolder();
        info = getDescription();
        dir.mkdirs();
        homes = new Homes(dir.getPath(), this);
        warps = new Warps(dir.getPath(), this);
        //Loads all currently online players
        //This addresses a NullPointerException caused when the server is reloaded
        //And online players aren't loaded because they never triggered a join event
        Player[] online = getServer().getOnlinePlayers();
        String[] names = new String[online.length];
        for(int x = 0; x < online.length; x++) {
            names[x] = online[x].getName();
        }
        warps.loadList(names);
        
        //Register Events
        playerListener = new PointsPlayerListener(this);
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
        
        System.out.println("[" + info.getName() + "] Enabled.");
    }
    
    public Warps getWarps() {
        return warps;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        
        if(command.getName().equalsIgnoreCase("home")) {
            if(player.hasPermission("points.home")) {
                homes.doHome(player);                
            } else
                player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
            return true;
        }
        
        else if(command.getName().equalsIgnoreCase("sethome")) {
            if(player.hasPermission("points.home")) {
                homes.setHome(player);
            } else
                player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
            return true;
        }

        else if(command.getName().equalsIgnoreCase("spawn")) {
            if(player.hasPermission("points.spawn")) {
                teleportTo(player, player.getLocation().getWorld().getSpawnLocation());
            } else
                player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
            return true;
        }
        
        else if(command.getName().equalsIgnoreCase("warp")) {
            if(player.hasPermission("points.warp")) {
                return warps.doCommand(sender, command, args);
            } else
                player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
        }
        return false;
    }
        
    public void teleportTo(Player p, Location loc) {
        if(loc.getBlock().getTypeId() != 0) {
            Location locN = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
            teleportTo(p, locN);
        } else {
            p.teleport(loc);
        }
    }
    
    public Location toLocation(String x, String y, String z, 
                               String yaw, String pitch, String worldName ) {
        World world = getServer().getWorld(worldName);
        return new Location(world, Double.parseDouble(x), Double.parseDouble(y), 
                Double.parseDouble(z), Float.parseFloat(yaw), 
                Float.parseFloat(pitch));

    }
}
