/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.listeners;

import com.pvminecraft.points.Points;
import com.pvminecraft.points.warps.Warp;
import com.pvminecraft.points.warps.WarpManager;
import com.pvminecraft.points.warps.WarpSign;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

/**
 *
 * @author s0lder
 */
public class PointsPlayerListener extends PlayerListener {
    private Points plugin;
    private WarpManager manager;
    
    public PointsPlayerListener(Points pl, WarpManager wm) {
        plugin = pl;
        manager = wm;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block bl = event.getClickedBlock();
        if((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (bl.getState() instanceof Sign)) {
            Sign sg = (Sign) bl.getState();
            WarpSign ws = manager.getSign(sg);
            if(!(ws == null)) {
                Warp warp = ws.getTarget();
                manager.sendTo(event.getPlayer(), warp);
            }
        }
    }
}
