package com.pvminecraft.points.commands;

import static com.pvminecraft.points.Points._;
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
    private WarpManager manager;
    
    public WarpCommand(Points ins) {
        plugin = ins;
        manager = plugin.getWarpManager();
    }

    public boolean listWarps(Player player, Player reciever) {
        List<Warp> plWarps = manager.getWarps(player);
        if(player == reciever) {
            if(plWarps == null)
                reciever.sendMessage(_("youNoWarps"));
            else {
                reciever.sendMessage(_("warpList"));
                for(Warp w : plWarps)
                    reciever.sendMessage(_("warpItem", new Object[] {w.getName(), w.getVisible()}));
            }
        } else {
            if(plWarps == null)
                reciever.sendMessage(_("plNoWarps", new Object[] {player.getName()}));
            else {
                reciever.sendMessage(_("warpList"));
                for(Warp w : plWarps)
                    if(w.getVisible())
                        reciever.sendMessage(_("warpItem", new Object[] {w.getName(), w.getVisible()}));
            }
        }
        return true;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        Player player = (Player) cs;
        if(!player.hasPermission("points.warp")) {
            player.sendMessage(_("alertNoPerm"));
            return true;
        }
        if(args.length == 1) {
            String action = args[0];
            if(action.equals("list"))
                return listWarps(player, player);
            } else if(action.equalsIgnoreCase("?") || action.equalsIgnoreCase("help")) {
                showHelp(player);
                return true;
            } else if(action.equalsIgnoreCase("find"))
                player.setCompassTarget(player.getLocation().getWorld().getSpawnLocation());
        } else if(args.length == 2) {
            String action = args[0];
            String target = args[1];
            if(action.equalsIgnoreCase("new")) {
                Location loc = player.getLocation();
                Warp warp = new Warp(loc, player.getName(), target);
                manager.addWarp(player, warp);
                player.sendMessage(ChatColor.GREEN + "Created " + ChatColor.YELLOW +
                        target + ChatColor.GREEN + " at " + ChatColor.BLUE + (int)loc.getX() +
                        ", " + (int)loc.getY() + ", " + (int)loc.getZ());
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
            } else if(action.equalsIgnoreCase("delete")) {
                Warp w = manager.getWarp(player, target);
                if(w == null) {
                    player.sendMessage(ChatColor.RED + "You don't have a warp named " +
                            ChatColor.YELLOW + target);
                } else {
                    manager.removeWarp(player, w);
                    player.sendMessage(ChatColor.GREEN + "The warp " + ChatColor.YELLOW +
                            target + ChatColor.GREEN + " has been deleted");
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
            } else if(action.equalsIgnoreCase("find")) {
                Warp w = manager.getWarp(player, target);
                if(w == null) {
                    player.sendMessage(ChatColor.RED + "You dont have a warp named " +
                            ChatColor.YELLOW + target);
                } else {
                    player.setCompassTarget(w.getTarget());
                    player.sendMessage(ChatColor.GREEN + "Your compass is now pointing toward the warp");
                }
                return true;
            } else if(action.equalsIgnoreCase("info")) {
                Warp w = manager.getWarp(player, target);
                if(w == null) {
                    player.sendMessage(ChatColor.RED + "You don't have a warp named " +
                            ChatColor.YELLOW + target);
                } else {
                    Location l = w.getTarget();
                    player.sendMessage(ChatColor.YELLOW + "Warp info for " +
                            ChatColor.BLUE + target + ChatColor.YELLOW + ":");
                    player.sendMessage(ChatColor.GREEN + "  Location:");
                    player.sendMessage(ChatColor.BLUE + "    X: " + (int)l.getX());
                    player.sendMessage(ChatColor.BLUE + "    Y: " + (int)l.getY());
                    player.sendMessage(ChatColor.BLUE + "    Z: " + (int)l.getZ());
                    player.sendMessage(ChatColor.BLUE + "    World: " + l.getWorld().getName());
                    player.sendMessage(ChatColor.GREEN + "  Visibility: " +
                        ChatColor.BLUE + (w.getVisible()?"public":"private"));
                }
                return true;
            }
        } else if(args.length < 2) {
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
        showHelp(player);
        return true;
    }

    public void showHelp(Player pl) {
        pl.sendMessage(ChatColor.YELLOW + "Help for /warp:");
        pl.sendMessage(ChatColor.GREEN + "/warp list " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "List all of your warps");
        pl.sendMessage(ChatColor.GREEN + "/warp ? " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "Show this help screen");
        pl.sendMessage(ChatColor.GREEN + "/warp new [name] " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "Create a new warp with the given name");
        pl.sendMessage(ChatColor.GREEN + "/warp delete [name] " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "Delete the warp with the given name");
        pl.sendMessage(ChatColor.GREEN + "/warp go [name] " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "Go to the warp with the given name");
        pl.sendMessage(ChatColor.GREEN + "/warp public [name] " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "Make the given warp public");
        pl.sendMessage(ChatColor.GREEN + "/warp find [name] " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "Point your compass to the given warp");
        pl.sendMessage(ChatColor.GREEN + "/warp list [player] " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "List the given player's warps");
        pl.sendMessage(ChatColor.GREEN + "/warp info [name] " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "Show information about a warp");
        pl.sendMessage(ChatColor.GREEN + "/warp go [player] [name] " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "Go to the given player's warp");
        
    }
}
