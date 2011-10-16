/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.listeners;

import com.pvminecraft.points.Points;
import com.pvminecraft.points.warps.Warp;
import com.pvminecraft.points.warps.WarpManager;
import com.pvminecraft.points.warps.WarpSign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author s0lder
 */
public class PointsBlockListener extends BlockListener {
    private Points plugin;
    private WarpManager manager;
    
    public PointsBlockListener(Points pl, WarpManager wm) {
        plugin = pl;
        manager = wm;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock().getState() instanceof Sign) {
            Location loc = event.getBlock().getLocation();
            manager.removeSign(loc);
        }
    }

    @Override
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
        manager.addSign(ws);
        player.sendMessage(ChatColor.GREEN + "Sign successfully created!");
    }
}
