package com.pvminecraft.points.commands.points;

import static com.pvminecraft.points.Messages._;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.commands.ArgumentSet;
import com.pvminecraft.points.commands.Command;
import com.pvminecraft.points.utils.Pair;
import com.pvminecraft.points.warps.OwnedWarp;
import java.util.HashMap;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class PointsPlayers extends ArgumentSet {
    
    public PointsPlayers(Command base, String pat, JavaPlugin plugin, Permission ... perms) {
        super(base, pat, plugin, perms);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if(!hasPermission(sender)) {
            sender.sendMessage(_("alertNoPerm"));
            return true;
        }
        HashMap<String, List<OwnedWarp>> map = ((Points) plugin).getPlayerManager().getTable();
        int numWarps = 0;
        String mostWarps = "N/A";
        int most = 0;
        for(String key : map.keySet()) {
            numWarps += map.get(key).size();
            if(map.get(key).size() > most) {
                most = map.get(key).size();
                mostWarps = key;
            }
        }
        sender.sendMessage(_("warpStats"));
        sender.sendMessage(_("numPlayers", map.size()));
        sender.sendMessage(_("numWarps", numWarps));
        sender.sendMessage(_("mostWarps", mostWarps));
        return true;
    }

    @Override
    public Pair<String, String> getHelp() {
        return new Pair<String, String>("/points players", "Shows a list of all players & warps");
    }
}
