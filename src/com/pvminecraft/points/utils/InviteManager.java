package com.pvminecraft.points.utils;

import java.util.HashMap;
import org.bukkit.entity.Player;

public class InviteManager {
    private static HashMap<Player, Invite> invites = new HashMap<Player, Invite>();
    
    private InviteManager() {}
    
    public static Invite removeInvite(Player player) {
        Invite in = invites.get(player);
        invites.remove(player);
        return in;
    }
    
    public static void setInvite(Player player, Invite invite) {
        invites.put(player, invite);
    }
}
