/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.plugins.signs;

import com.pvminecraft.points.Points;
import com.pvminecraft.points.warps.Warp;
import com.pvminecraft.points.warps.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author s0lder
 */
public class SignBlockListener extends BlockListener {
    private Points plugin;
    private WarpManager manager;
    private WarpSignManager sMgr;
    
    public SignBlockListener(Points pl, WarpManager wm, WarpSignManager sign) {
        plugin = pl;
        manager = wm;
        sMgr = sign;
        plugin.getServer().getPluginManager().registerEvent(Type.BLOCK_BREAK,
                this, Priority.Normal, plugin);
        plugin.getServer().getPluginManager().registerEvent(Type.SIGN_CHANGE,
                this, Priority.Normal, plugin);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock().getState() instanceof Sign) {
            Location loc = event.getBlock().getLocation();
            if(sMgr.getSign((Sign) event.getBlock().getState()) == null)
                    return;
            sMgr.removeSign(loc);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You've broken a warp sign!");
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
        sMgr.addSign(ws);
        player.sendMessage(ChatColor.GREEN + "Sign successfully created!");
    }
}
