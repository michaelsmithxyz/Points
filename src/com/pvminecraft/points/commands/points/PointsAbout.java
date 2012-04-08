package com.pvminecraft.points.commands.points;

import static com.pvminecraft.points.Messages._;
import com.pvminecraft.points.commands.ArgumentSet;
import com.pvminecraft.points.commands.Command;
import com.pvminecraft.points.utils.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class PointsAbout extends ArgumentSet {
    
    public PointsAbout(Command base, String pat, JavaPlugin plugin, Permission ... perms) {
        super(base, pat, plugin, perms);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if(!hasPermission(sender)) {
            sender.sendMessage(_("alertNoPerm"));
            return true;
        }
        sender.sendMessage(_("about", plugin.getDescription().getVersion()));
        sender.sendMessage(_("copyright"));
        sender.sendMessage(_("license"));
        return true;
    }

    @Override
    public Pair<String, String> getHelp() {
        return new Pair<String, String>("/points about", "Shows the current Points version");
    }
}
