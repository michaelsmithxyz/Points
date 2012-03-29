package com.pvminecraft.points.commands.warp;

import static com.pvminecraft.points.Messages._;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.commands.ArgumentSet;
import com.pvminecraft.points.commands.Command;
import com.pvminecraft.points.utils.Pair;
import com.pvminecraft.points.warps.GlobalWarpManager;
import com.pvminecraft.points.warps.OwnedWarp;
import com.pvminecraft.points.warps.PlayerWarpManager;
import com.pvminecraft.points.warps.Warp;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpList extends ArgumentSet {
    
    public WarpList(Command base, String pat, JavaPlugin plugin, Permission ... perms) {
        super(base, pat, plugin, perms);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if(!hasPermission(sender)) {
            sender.sendMessage(_("alertNoPerm"));
            return true;
        }
        PlayerWarpManager playerManager = ((Points) plugin).getPlayerManager();
        GlobalWarpManager globalManager = ((Points) plugin).getGlobalManager();
        Player player = (Player) sender;
        if(args.length >= 2) {
            String other = args[1];
            if(other.equalsIgnoreCase("globals")) {
                List<Warp> globals = globalManager.getAll();
                if(globals.isEmpty() || globals == null) {
                    player.sendMessage(_("noGlobalWarps"));
                } else {
                    player.sendMessage(_("globalList"));
                    for(Warp warp : globals)
                        player.sendMessage(_("globalItem", warp.getName(), "global"));
                }
            } else {
                List<OwnedWarp> warps = playerManager.getWarps(other);
                if(warps == null || warps.isEmpty()) {
                    player.sendMessage(_("plNoWarps", other));
                } else {
                    player.sendMessage(_("warpList"));
                    for(OwnedWarp warp : warps)
                        if(warp.getVisible())
                            player.sendMessage(_("warpItem", warp.getName(), warp.getVisible()?"public":"private"));
                }
            }
        } else {
            List<OwnedWarp> warps = playerManager.getWarps(player.getName());
            if(warps == null || warps.isEmpty()) {
                player.sendMessage(_("youNoWarps"));
            } else {
                player.sendMessage(_("warpList"));
                for(OwnedWarp warp : warps)
                    player.sendMessage(_("warpItem", warp.getName(), warp.getVisible()?"public":"private"));
            }
        }
        return true;
    }

    @Override
    public Pair<String, String> getHelp() {
        return new Pair<String, String>("/warp list", "List all of your warps");
    }

}
