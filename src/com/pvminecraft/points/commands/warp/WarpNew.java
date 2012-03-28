package com.pvminecraft.points.commands.warp;

import static com.pvminecraft.points.Messages._;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.commands.ArgumentSet;
import com.pvminecraft.points.commands.Command;
import com.pvminecraft.points.utils.Pair;
import com.pvminecraft.points.warps.OwnedWarp;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpNew extends ArgumentSet {
    
    public WarpNew(Command base, String pat, JavaPlugin plugin, Permission ... perms) {
        super(base, pat, plugin, perms);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if(!hasPermission(sender)) {
            sender.sendMessage(_("alertNoPerm"));
            return true;
        }
        Player player = (Player) sender;
        Location location = player.getLocation();
        OwnedWarp warp = OwnedWarp.createWarp(location, args[1], player.getName());
        ((Points) plugin).getPlayerManager().addWarp(warp);
        player.sendMessage(_("warpCreate", warp.getName(), (int)location.getX(),
                (int)location.getY(), (int)location.getZ()));
        return true;
    }

    @Override
    public Pair<String, String> getHelp() {
        return new Pair<String, String>("/warp new [name]", "Creates a new warp at you current position");
    }

}