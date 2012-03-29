package com.pvminecraft.points.commands.warp;

import static com.pvminecraft.points.Messages._;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.commands.ArgumentSet;
import com.pvminecraft.points.commands.Command;
import com.pvminecraft.points.utils.Pair;
import com.pvminecraft.points.warps.OwnedWarp;
import com.pvminecraft.points.warps.PlayerWarpManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpGo extends ArgumentSet {
    
    public WarpGo(Command base, String pat, JavaPlugin plugin, Permission ... perms) {
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
        if(args.length >= 3) {
            String otherPlayer = args[1];
            String warp = args[2];
            OwnedWarp target = manager.getWarp(otherPlayer, warp);
            if(target == null || !target.getVisible())
                player.sendMessage(_("plNoWarp", otherPlayer, warp));
            else
                PlayerWarpManager.sendTo(player, target);
        } else {
            String warp = args[1];
            OwnedWarp target = manager.getWarp(player.getName(), warp);
            if(target == null)
                player.sendMessage(_("noWarpName", warp));
            else
                PlayerWarpManager.sendTo(player, target);
        }
        return true;
    }

    @Override
    public Pair<String, String> getHelp() {
        return new Pair<String, String>("/warp go [name]", "Go to one of your warps");
    }

}
