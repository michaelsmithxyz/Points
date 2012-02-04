/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.plugins.signs;

import com.pvminecraft.points.Points;
import com.pvminecraft.points.warps.PlayerWarpManager;
import com.pvminecraft.points.warps.Warp;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author s0lder
 */
public class SignBlockListener implements Listener {
    private Points plugin;
    private PlayerWarpManager manager;
    private WarpSignManager sMgr;
    
    public SignBlockListener(Points pl, PlayerWarpManager wm, WarpSignManager sign) {
        plugin = pl;
        manager = wm;
        sMgr = sign;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock().getState() instanceof Sign) {
            Location loc = event.getBlock().getLocation();
            if(sMgr.getSign((Sign) event.getBlock().getState()) == null)
                    return;
            sMgr.removeSign(loc);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You've broken a warp sign!");
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        String[] lines = event.getLines();
        Player player = event.getPlayer();
        Sign sign = (Sign) event.getBlock().getState();
        if(!(lines[0].isEmpty() && lines[1].equalsIgnoreCase("Warp") && !lines[2].isEmpty())) {
            return;
        }
        Warp warp = manager.getWarp(player, lines[2]);
        if(warp == null) {
            player.sendMessage(ChatColor.RED + "That is not a valid warp!");
            return;
        }
        event.setLine(0, player.getName());
        WarpSign ws = new WarpSign(sign, warp, event.getBlock().getLocation());
        sMgr.addSign(ws);
        player.sendMessage(ChatColor.GREEN + "Sign successfully created!");
    }
}
