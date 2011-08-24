/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.Points;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author s0lder
 */
class PointsPlayerListener extends PlayerListener {
    private Points plugin;
    private Warps warps;
    
    public PointsPlayerListener(Points instance) {
        plugin = instance;
        warps = plugin.getWarps();
    }
    
    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        warps.loadPlayer(name);
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        warps.unloadPlayer(name);
    }
    
    
    
}
