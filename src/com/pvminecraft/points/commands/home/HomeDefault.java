package com.pvminecraft.points.commands.home;

import static com.pvminecraft.points.Messages._;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.commands.ArgumentSet;
import com.pvminecraft.points.commands.Command;
import com.pvminecraft.points.utils.Pair;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class HomeDefault extends ArgumentSet {
    
    public HomeDefault(Command base, String pat, JavaPlugin plugin, Permission ... perms) {
        super(base, pat, plugin, perms);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if(!hasPermission(sender)) {
            sender.sendMessage(_("alertNoPerm"));
            return true;
        }
        Player player = (Player) sender;
        Points points = (Points) plugin;
        Location home = points.getHomeManager().getHome(player.getName());
        if(home == null)
            player.sendMessage(_("noHome"));
        else {
            player.teleport(home);
            player.sendMessage(_("welcome"));
        }
        return true;
    }

    @Override
    public Pair<String, String> getHelp() {
        return null;
    }

}
