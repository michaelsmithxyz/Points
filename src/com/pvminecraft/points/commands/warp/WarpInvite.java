package com.pvminecraft.points.commands.warp;

import static com.pvminecraft.points.Messages._;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.commands.ArgumentSet;
import com.pvminecraft.points.commands.Command;
import com.pvminecraft.points.utils.Invite;
import com.pvminecraft.points.utils.InviteManager;
import com.pvminecraft.points.utils.Pair;
import com.pvminecraft.points.warps.OwnedWarp;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpInvite extends ArgumentSet {
    
    public WarpInvite(Command base, String pat, JavaPlugin plugin, Permission ... perms) {
        super(base, pat, plugin, perms);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if(!hasPermission(sender)) {
            sender.sendMessage(_("alertNoPerm"));
            return true;
        }
        Player player = (Player) sender;
        Player invitee = plugin.getServer().getPlayer(args[1]);
        if(invitee == null) {
            player.sendMessage(_("noPlayer", args[1]));
            return true;
        }
        OwnedWarp warp = ((Points) plugin).getPlayerManager().getWarp(player.getName(), args[2]);
        if(warp == null) {
            player.sendMessage(_("noWarpName", args[2]));
            return true;
        }
        Invite invite = new Invite(player, warp);
        InviteManager.setInvite(invitee, invite);
        player.sendMessage(_("warpInvited", invitee.getName(), warp.getName()));
        invitee.sendMessage(_("warpInvite", player.getName(), warp.getName()));
        return true;
    }

    @Override
    public Pair<String, String> getHelp() {
        return new Pair<String, String>("/warp invite [name] [warp]", "Invite another player to a warp");
    }

}
