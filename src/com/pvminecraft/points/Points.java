/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points;

import com.pvminecraft.points.commands.HomeCommand;
import com.pvminecraft.points.commands.WarpCommand;
import com.pvminecraft.points.warps.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author s0lder
 */
public class Points extends JavaPlugin {
    private HomeCommand homesManager;
    private WarpCommand warpCommand;
    private WarpManager warpManager;
    
    @Override
    public void onDisable() {
        homesManager.saveHomes();
        warpManager.savePlayers(getDataFolder().getPath());
    }

    @Override
    public void onEnable() {
        homesManager = new HomeCommand(this);
        warpManager = new WarpManager(this);
        warpCommand = new WarpCommand(this);
        homesManager.loadHomes();
        warpManager.loadWarps(getDataFolder().getPath());
        getCommand("home").setExecutor(homesManager);
        getCommand("sethome").setExecutor(homesManager);
        getCommand("warp").setExecutor(warpCommand);
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
