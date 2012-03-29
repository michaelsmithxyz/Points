package com.pvminecraft.points.commands.warp;

import static com.pvminecraft.points.Messages._;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.commands.ArgumentSet;
import com.pvminecraft.points.commands.Command;
import com.pvminecraft.points.utils.Pair;
import com.pvminecraft.points.warps.OwnedWarp;
import com.pvminecraft.points.warps.PlayerWarpManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpInfo extends ArgumentSet {
    
    public WarpInfo(Command base, String pat, JavaPlugin plugin, Permission ... perms) {
        super(base, pat, plugin, perms);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if(!hasPermission(sender)) {
            sender.sendMessage(_("alertNoPerm"));
            return true;
        }
        Player player = (Player) sender;
        PlayerWarpManager manager = ((Points) plugin).getPlayerManager();
        OwnedWarp warp = manager.getWarp(player.getName(), args[1]);
        if(warp == null)
            player.sendMessage(_("noWarpName", args[1]));
        else {
            Location l = warp.getTarget();
            player.sendMessage(_("warpInfo", warp.getName()));
            player.sendMessage(_("location"));
            player.sendMessage(_("x", (int) l.getX()));
            player.sendMessage(_("y", (int) l.getY()));
            player.sendMessage(_("z", (int) l.getZ()));
            player.sendMessage(_("world", l.getWorld().getName()));
            player.sendMessage(_("visibility", warp.getVisible()?"public":"private"));
        }
        return true;
    }

    @Override
    public Pair<String, String> getHelp() {
        return new Pair<String, String>("/warp info [name]", "Show information about a warp");
    }

}
