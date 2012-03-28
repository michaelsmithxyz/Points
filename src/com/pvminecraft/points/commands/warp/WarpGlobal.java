package com.pvminecraft.points.commands.warp;

import static com.pvminecraft.points.Messages._;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.commands.ArgumentSet;
import com.pvminecraft.points.commands.Command;
import com.pvminecraft.points.utils.Pair;
import com.pvminecraft.points.warps.GlobalWarpManager;
import com.pvminecraft.points.warps.Warp;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpGlobal extends ArgumentSet {
    
    public WarpGlobal(Command base, String pat, JavaPlugin plugin, Permission ... perms) {
        super(base, pat, plugin, perms);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if(!hasPermission(sender)) {
            sender.sendMessage(_("alertNoPerm"));
            return true;
        }
        Player player = (Player) sender;
        GlobalWarpManager manager = ((Points) plugin).getGlobalManager();
        Warp warp = manager.getWarp(args[1]);
        if(warp == null)
            player.sendMessage(_("noWarpName", args[1]));
        else
            GlobalWarpManager.sendTo(player, warp);
        return true;
    }

    @Override
    public Pair<String, String> getHelp() {
        return new Pair<String, String>("/warp global [name]", "Go to a global warp");
    }

}
