package com.pvminecraft.points.commands;

import com.pvminecraft.points.Messages;
import com.pvminecraft.points.Points;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        if(command.getName().equalsIgnoreCase("spawn")) {
            if(!player.hasPermission("points.spawn")) {
                player.sendMessage(Messages._("alertNoPerm"));
                return true;
            }
            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("bed")) {
                    Location bed = player.getBedSpawnLocation();
                    if(bed != null)
                        Points.teleportTo(player, bed);
                    else
                        Points.teleportTo(player, player.getLocation().getWorld().getSpawnLocation());
                    return true;
                } else if(args[0].equalsIgnoreCase("set")) {
                    if(!player.hasPermission("points.admin")) {
                        player.sendMessage(Messages._("alertNoPerm"));
                        return true;
                    }
                    Location newLoc = player.getLocation();
                    newLoc.getWorld().setSpawnLocation(newLoc.getBlockX(),
                            newLoc.getBlockY(), newLoc.getBlockZ());
                }
            } else {
                Points.teleportTo(player, player.getLocation().getWorld().getSpawnLocation());
                return true;
            }
        }
        return false;
    }
}
