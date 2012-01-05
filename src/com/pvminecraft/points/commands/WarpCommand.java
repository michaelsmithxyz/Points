package com.pvminecraft.points.commands;

import static com.pvminecraft.points.Points._;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.warps.Warp;
import com.pvminecraft.points.warps.WarpManager;
import java.util.HashMap;
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
    private HashMap<Player, Warp> requests;
    
    public WarpCommand(Points ins) {
        plugin = ins;
        manager = plugin.getWarpManager();
        requests = new HashMap<Player, Warp>();
    }

    public boolean listWarps(String player, Player reciever) {
        List<Warp> plWarps = manager.getWarps(player);
        if(player == null) {
            reciever.sendMessage(_("noPlayer", player));
            return true;
        }
        if(player.equalsIgnoreCase(reciever.getName())) {
            if(plWarps == null)
                reciever.sendMessage(_("youNoWarps"));
            else {
                reciever.sendMessage(_("warpList"));
                for(Warp w : plWarps)
                    reciever.sendMessage(_("warpItem", w.getName(), w.getVisible()?"public":"private"));
            }
        } else {
            if(plWarps == null)
                reciever.sendMessage(_("plNoWarps", player));
            else {
                reciever.sendMessage(_("warpList"));
                for(Warp w : plWarps)
                    if(w.getVisible())
                        reciever.sendMessage(_("warpItem", w.getName(),
                                w.getVisible()?"public":"private"));
            }
        }
        return true;
    }

    public boolean newWarp(Player player, String target) {
        Location loc = player.getLocation();
        Warp warp = new Warp(loc, player.getName(), target);
        manager.addWarp(player, warp);
        player.sendMessage(_("warpCreate", warp.getName(), (int)loc.getX(),
                (int)loc.getY(), (int)loc.getZ()));
        return true;
    }

    public boolean goToWarp(Player player, Player target, String name) {
        if(target == null) {
            player.sendMessage(_("noPlayer", target.getName()));
            return true;
        }
        Warp w = manager.getWarp(target, name);
        if(w == null) {
            if(player == target)
                player.sendMessage(_("noWarpName", name));
            else
                player.sendMessage(_("plNoWarp", target.getName(), name));
        } else {
            manager.sendTo(player, w);
        }
        return true;
    }

    public void showInfo(Warp w, Player player) {
        Location l = w.getTarget();
        player.sendMessage(_("warpInfo", w.getName()));
        player.sendMessage(_("location"));
        player.sendMessage(_("x", (int)l.getX()));
        player.sendMessage(_("y", (int)l.getY()));
        player.sendMessage(_("z", (int)l.getZ()));
        player.sendMessage(_("world", l.getWorld().getName()));
        player.sendMessage(_("visibility", w.getVisible()?"public":"private"));
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        Player player = (Player) cs;
        if(!player.hasPermission("points.warp")) {
            player.sendMessage(_("alertNoPerm"));
            return true;
        }
        // Commands of the form /command [arg]
        if(args.length == 1) {
            String action = args[0];
            if(action.equals("list"))
                return listWarps(player.getName(), player);
            else if(action.equalsIgnoreCase("?") || action.equalsIgnoreCase("help")) {
                showHelp(player);
                return true;
            } else if(action.equalsIgnoreCase("find")) {
                player.setCompassTarget(player.getLocation().getWorld().getSpawnLocation());
                return true;
            } else if(action.equalsIgnoreCase("accept")) {
                Warp w = requests.get(player);
                if(w == null)
                    player.sendMessage(_("noInvites"));
                else {
                    Player owner = plugin.getServer().getPlayer(w.getOwner());
                    owner.sendMessage(_("warpAccepted", player.getName()));
                    manager.sendTo(player, w);
                }
                return true;
            }
        //Commands of the form /command [arg] [arg]
        } else if(args.length == 2) {
            String action = args[0];
            String target = args[1];
            if(action.equalsIgnoreCase("new"))
                return newWarp(player, target);
            else if(action.equalsIgnoreCase("list"))
                return listWarps(target, player);
            else if(action.equalsIgnoreCase("go"))
                return goToWarp(player, player, target);
            else if(action.equalsIgnoreCase("delete")) {
                Warp w = manager.getWarp(player, target);
                if(w == null)
                    player.sendMessage(_("noWarpName", target));
                else {
                    manager.removeWarp(player, w);
                    player.sendMessage(_("warpDeleted", target));
                }
                return true;
            } else if(action.equalsIgnoreCase("public")) {
                Warp w = manager.getWarp(player, target);
                if(w == null)
                    player.sendMessage(_("noWarpName", target));
                else
                    w.setVisible(true);
                return true;
            } else if(action.equalsIgnoreCase("find")) {
                Warp w = manager.getWarp(player, target);
                if(w == null)
                    player.sendMessage(_("noWarpName", target));
                else {
                    player.setCompassTarget(w.getTarget());
                    player.sendMessage(_("compass"));
                }
                return true;
            } else if(action.equalsIgnoreCase("info")) {
                Warp w = manager.getWarp(player, target);
                if(w == null)
                    player.sendMessage(_("noWarpName", target));
                else {
                    showInfo(w, player);
                }
                return true;
            }
        // Commands of the form /command [arg] [arg] [arg]..[argn]
        } else if(args.length >= 3) {
            String action = args[0];
            String target = args[1];
            String targetTwo = args[2];
            if(action.equalsIgnoreCase("go")) {
                Warp w = manager.getWarp(target, targetTwo);
                if(w == null || !w.getVisible())
                    player.sendMessage(_("plNoWarp", target, targetTwo));
                else
                    manager.sendTo(player, w);
                return true;
            } else if(action.equalsIgnoreCase("invite")) {
                Player pl = plugin.getServer().getPlayer(target);
                if(pl == null) {
                    player.sendMessage(_("noPlayer", target));
                } else {
                    Warp w = manager.getWarp(player, targetTwo);
                    if(w == null) {
                        player.sendMessage(_("noWarpName", targetTwo));
                    } else {
                        requests.put(pl, w);
                        pl.sendMessage(_("warpInvite", target, targetTwo));
                        player.sendMessage(_("warpInvited", target, targetTwo));
                    }
                }
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
        pl.sendMessage(ChatColor.GREEN + "/warp invite [player] [name] " + ChatColor.WHITE +
                 " - " + ChatColor.BLUE + "Invite a player to a warp");
        
    }
}
