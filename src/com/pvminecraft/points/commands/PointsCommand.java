package com.pvminecraft.points.commands;

import com.pvminecraft.points.Points;
import com.pvminecraft.points.warps.Warp;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author s0lder
 */
public class PointsCommand implements CommandExecutor {
    private Points plugin;


    public PointsCommand(Points pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("about")) {
                cs.sendMessage(ChatColor.BLUE + "Points " + ChatColor.WHITE + "version " +
                        ChatColor.GREEN + plugin.getDescription().getVersion());
                cs.sendMessage("Copyright 2011 " + ChatColor.GREEN + "s0lder (Michael Smith)");
                cs.sendMessage("Licensed under the " + ChatColor.GREEN + "GNU GPL");
                return true;
            } else if(args[0].equalsIgnoreCase("players")) {
                HashMap<String, List<Warp>> map = plugin.getWarpManager().getWarpTable();
                Set<String> players = map.keySet();
                if(players.size() < 1) {
                    cs.sendMessage("No players currently have warps");
                    return true;
                }
                cs.sendMessage("The following players have warps:");
                for(String player : players) {
                    cs.sendMessage(" - " + ChatColor.GREEN + player + ChatColor.WHITE +
                            " - " + ChatColor.BLUE + map.get(player).size());
                }
                return true;
            } else if(args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) {
                showHelp(cs);
                return true;
            }
        }
        showHelp(cs);
        return true;
    }

    public void showHelp(CommandSender cs) {
        cs.sendMessage(ChatColor.YELLOW + "Help for /points:");
        cs.sendMessage(ChatColor.GREEN + "/points about " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "Display information about Points");
        cs.sendMessage(ChatColor.GREEN + "/points players " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "Display all players who have warps");
        cs.sendMessage(ChatColor.GREEN + "/points ? " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "Display this help screen");
    }
}
