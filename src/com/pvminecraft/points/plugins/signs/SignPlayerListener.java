package com.pvminecraft.points.plugins.signs;

import com.pvminecraft.points.Points;
import com.pvminecraft.points.warps.PlayerWarpManager;
import com.pvminecraft.points.warps.Warp;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author s0lder
 */
public class SignPlayerListener implements Listener {
    private Points plugin;
    private PlayerWarpManager manager;
    private WarpSignManager sMgr;
    
    public SignPlayerListener(Points pl, PlayerWarpManager wm, WarpSignManager sign) {
        plugin = pl;
        manager = wm;
        sMgr = sign;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
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
