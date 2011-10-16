/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.commands;

import com.pvminecraft.points.Points;
import com.pvminecraft.points.warps.Warp;
import com.pvminecraft.points.warps.WarpManager;
import java.util.List;
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
public class WarpCommand implements CommandExecutor {
    private Points plugin;
    
    public WarpCommand(Points ins) {
        plugin = ins;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        WarpManager manager = plugin.getWarpManager();
        Player pl = (Player) cs;
        if(!pl.hasPermission("points.warp")) {
            pl.sendMessage(ChatColor.RED + "You don't have permission to do that!");
            return true;
        }
        if(args.length < 1) {
            return false;
        } else if(args.length == 1) {
            String action = args[0];
            Player player = (Player) cs;
            if("list".equals(action)) {
                List<Warp> plWarps = manager.getWarps(player);
                if(plWarps == null || plWarps.size() < 1) {
                    player.sendMessage(ChatColor.RED + "You don't have any warps!");
                } else {
                    player.sendMessage(ChatColor.GREEN + "Your Warps:");
                    for(Warp w : plWarps) {
                        player.sendMessage(ChatColor.BLUE + "    " + w.getName() + ": " + 
                                (w.getVisible()?"public":"private"));
                    }
                }
                return true;
            }
        } else if(args.length == 2) {
            Player player = (Player) cs;
            String action = args[0];
            String target = args[1];
            if(action.equalsIgnoreCase("new")) {
                Location loc = player.getLocation();
                Warp warp = new Warp(loc, player.getName(), target);
                manager.addWarp(player, warp);
                player.sendMessage(ChatColor.GREEN + "Your warp has been created!");
                return true;
            } else if(action.equalsIgnoreCase("list")) {
                List<Warp> plWarps = manager.getWarps(target);
                if(plWarps == null || plWarps.size() < 1) {
                    player.sendMessage(ChatColor.RED + "That player doesn't have any warps!");
                } else {
                    player.sendMessage(ChatColor.GREEN + "Warps:");
                    for(Warp w : plWarps) {
                        if(w.getVisible())
                            player.sendMessage(ChatColor.BLUE + "    " + w.getName());
                    }
                }
                return true;
            } else if(action.equalsIgnoreCase("go")) {
                Warp w = manager.getWarp(player, target);
                if(w == null) {
                    player.sendMessage(ChatColor.RED + "You don't have a warp named " +
                            ChatColor.YELLOW + target);
                } else {
                    manager.sendTo(player, w);
                }
                return true;
            } else if(action.equalsIgnoreCase("public")) {
                Warp w = manager.getWarp(player, target);
                if(w == null) {
                    player.sendMessage(ChatColor.RED + "You don't have a warp named " +
                            ChatColor.YELLOW + target);
                } else {
                    w.setVisible(true);
                }
                return true;
            }
        } else {
            Player player = (Player) cs;
            String action = args[0];
            String target = args[1];
            String targetTwo = args[2];
            if(action.equalsIgnoreCase("go")) {
                Warp w = manager.getWarp(target, targetTwo);
                if(w == null || !w.getVisible()) {
                    player.sendMessage(ChatColor.RED + target + " doesn't have a warp"
                            + " by that name!");
                } else
                    manager.sendTo(player, w);
                return true;
            }
        }
        return false;
    }
    
}
