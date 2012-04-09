package com.pvminecraft.points.utils;

import com.pvminecraft.points.warps.OwnedWarp;
import org.bukkit.entity.Player;

public class Invite {
    private OwnedWarp warp;
    private Player player;
    
    public Invite(Player player, OwnedWarp warp) {
        this.warp = warp;
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }

    public OwnedWarp getWarp() {
        return warp;
    }
}
