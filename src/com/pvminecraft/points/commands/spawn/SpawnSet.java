package com.pvminecraft.points.commands.spawn;

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

public class SpawnSet extends ArgumentSet {
    
    public SpawnSet(Command base, String pat, JavaPlugin plugin, Permission ... perms) {
        super(base, pat, plugin, perms);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if(!hasPermission(sender)) {
            sender.sendMessage(_("alertNoPerm"));
            return true;
        }
        Player player = (Player) sender;
        Location newLoc = player.getLocation();
        newLoc.getWorld().setSpawnLocation(newLoc.getBlockX(),
            newLoc.getBlockY(), newLoc.getBlockZ());
        player.sendMessage(_("spawnSet"));
        return true;
    }

    @Override
    public Pair<String, String> getHelp() {
        return new Pair<String, String>("/spawn bed", "Teleport to your bed spawn point");
    }

}
