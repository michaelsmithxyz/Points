/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.plugins.signs;

import com.pvminecraft.points.Points;
import com.pvminecraft.points.warps.Warp;
import com.pvminecraft.points.warps.WarpManager;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

/**
 *
 * @author s0lder
 */
public class SignPlayerListener extends PlayerListener {
    private Points plugin;
    private WarpManager manager;
    private WarpSignManager sMgr;
    
    public SignPlayerListener(Points pl, WarpManager wm, WarpSignManager sign) {
        plugin = pl;
        manager = wm;
        sMgr = sign;
        plugin.getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT,
                this, Priority.Normal, plugin);
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block bl = event.getClickedBlock();
        if((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (bl.getState() instanceof Sign)) {
            Sign sg = (Sign) bl.getState();
            WarpSign ws = sMgr.getSign(sg);
            if(!(ws == null)) {
                Warp warp = ws.getTarget();
                manager.sendTo(event.getPlayer(), warp);
            }
        }
    }
}
