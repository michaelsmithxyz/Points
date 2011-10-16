/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points;

import com.pvminecraft.points.commands.HomeCommand;
import com.pvminecraft.points.commands.WarpCommand;
import com.pvminecraft.points.listeners.PointsBlockListener;
import com.pvminecraft.points.listeners.PointsPlayerListener;
import com.pvminecraft.points.warps.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author s0lder
 */
public class Points extends JavaPlugin {
    private HomeCommand homesManager;
    private WarpCommand warpCommand;
    private WarpManager warpManager;
    private PointsPlayerListener playerListener;
    private PointsBlockListener blockListener;
    
    @Override
    public void onDisable() {
        homesManager.saveHomes();
        warpManager.savePlayers(getDataFolder().getPath());
        warpManager.saveSigns(getDataFolder().getPath());
    }

    @Override
    public void onEnable() {
        homesManager = new HomeCommand(this);
        warpManager = new WarpManager(this);
        warpCommand = new WarpCommand(this);
        playerListener = new PointsPlayerListener(this, warpManager);
        blockListener = new PointsBlockListener(this, warpManager);
        homesManager.loadHomes();
        warpManager.loadWarps(getDataFolder().getPath());
        warpManager.loadSigns(getDataFolder().getPath());
        getCommand("home").setExecutor(homesManager);
        getCommand("sethome").setExecutor(homesManager);
        getCommand("warp").setExecutor(warpCommand);
        getServer().getPluginManager().registerEvent(Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Type.SIGN_CHANGE, blockListener, Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        System.out.println("[Points] Points is now active.");
    }
    
    public WarpManager getWarpManager() {
        return warpManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        if(command.getName().equalsIgnoreCase("spawn")) {
            if(!player.hasPermission("points.spawn")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
                return true;
            }
            teleportTo(player, player.getLocation().getWorld().getSpawnLocation());
            return true;
        }
        return false;
    }
    
    private void teleportTo(Player pl, Location loc) {
        if(loc.getBlock().getTypeId() != 0) {
            Location locN = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
            teleportTo(pl, locN);
        } else {
            pl.teleport(loc);
        }
    }
}
