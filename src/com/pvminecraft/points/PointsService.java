package com.pvminecraft.points;

import com.pvminecraft.points.warps.GlobalWarpManager;
import com.pvminecraft.points.warps.OwnedWarp;
import com.pvminecraft.points.warps.PlayerWarpManager;
import com.pvminecraft.points.warps.Warp;

public interface PointsService {
    
    /**
     * Returns the current warp manager in use for global warps.
     * Global warps operate with the {@link Warp} class.
     * 
     * @return the current warp manager for globals
     */
    GlobalWarpManager getGlobalManager();
    
    /**
     * Returns the current warp manager in use for player warps.
     * Player warps operate with the {@link OwnedWarp} class.
     * 
     * @return the current warp manager for players
     */
    PlayerWarpManager getPlayerManager();
}
