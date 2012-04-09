package com.pvminecraft.points.commands.warp;

import static com.pvminecraft.points.Messages._;
import com.pvminecraft.points.commands.ArgumentSet;
import com.pvminecraft.points.commands.Command;
import com.pvminecraft.points.utils.Invite;
import com.pvminecraft.points.utils.InviteManager;
import com.pvminecraft.points.utils.Pair;
import com.pvminecraft.points.warps.PlayerWarpManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpAccept extends ArgumentSet {
    
    public WarpAccept(Command base, String pat, JavaPlugin plugin, Permission ... perms) {
        super(base, pat, plugin, perms);
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if(!hasPermission(sender)) {
            sender.sendMessage(_("alertNoPerm"));
            return true;
        }
        Player player = (Player) sender;
        Invite invite = InviteManager.removeInvite(player);
        if(invite == null) {
            player.sendMessage(_("noInvites"));
            return true;
        }
        invite.getPlayer().sendMessage(_("warpAccepted", player.getName()));
        PlayerWarpManager.sendTo(player, invite.getWarp());
        return true;
    }

    @Override
    public Pair<String, String> getHelp() {
        return new Pair<String, String>("/warp accept", "Accept an invite to a warp");
    }

}
