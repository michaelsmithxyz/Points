package com.pvminecraft.points.commands;

import static com.pvminecraft.points.Messages._;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.warps.OwnedWarp;
import com.pvminecraft.points.warps.Warp;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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
public class PointsCommand implements CommandExecutor {
    private Points plugin;


    public PointsCommand(Points pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
        if(!cs.hasPermission("points.admin")) {
            cs.sendMessage(_("alertNoPerm"));
            return true;
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("about")) {
                cs.sendMessage(_("about", new Object[] {plugin.getDescription().getVersion()}));
                cs.sendMessage(_("copyright"));
                cs.sendMessage(_("license"));
                return true;
            } else if(args[0].equalsIgnoreCase("players")) {
                HashMap<String, List<OwnedWarp>> map = plugin.getPlayerManager().getTable();
                Set<String> players = map.keySet();
                if(players.size() < 1) {
                    cs.sendMessage(_("noPlayerWarps"));
                    return true;
                }
                cs.sendMessage(_("playersWarps"));
                for(String player : players) {
                    cs.sendMessage(_("warpNums", new Object[] {player, map.get(player).size()}));
                }
                return true;
            } else if(args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) {
                showHelp(cs);
                return true;
            }
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("addglobal")) {
                if(!(cs instanceof Player)) {
                    cs.sendMessage(_("noConsole"));
                    return true;
                }
                String name = args[1];
                Location location = ((Player) cs).getLocation();               
                Warp warp = Warp.createWarp(location, name);
                plugin.getGlobalManager().addWarp(warp);
                cs.sendMessage(_("warpCreate", warp.getName(), location.getX(),
                                 location.getY(), location.getZ()));
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
        cs.sendMessage(ChatColor.GREEN + "/points addglobal " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "Create a new global warp");
    }
}
