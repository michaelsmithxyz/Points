package com.pvminecraft.points.commands.points;

import static com.pvminecraft.points.Messages._;
import static com.pvminecraft.points.Messages._;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.commands.ArgumentSet;
import com.pvminecraft.points.commands.Command;
import com.pvminecraft.points.utils.Pair;
import com.pvminecraft.points.warps.Warp;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class PointsAddGlobal extends ArgumentSet {
    
    public PointsAddGlobal(Command base, String pat, JavaPlugin plugin, Permission ... perms) {
        super(base, pat, plugin, perms);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if(!hasPermission(sender)) {
            sender.sendMessage(_("alertNoPerm"));
            return true;
        }
        if(!(sender instanceof Player)) {
           sender.sendMessage(_("noConsole"));
           return true;
        }
        String name = args[1];
        Location loc = ((Player) sender).getLocation();
        Warp warp = Warp.createWarp(loc, name);
        ((Points) plugin).getGlobalManager().addWarp(warp);
        sender.sendMessage(_("warpCreate", warp.getName(), (int)loc.getX(),
                (int)loc.getY(), (int)loc.getZ()));
        return true;
    }

    @Override
    public Pair<String, String> getHelp() {
        return new Pair<String, String>("/points addglobal [name]", "Add a global warp");
    }
}
