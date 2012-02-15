package com.pvminecraft.points;

import com.pvminecraft.points.warps.GlobalWarpManager;
import com.pvminecraft.points.warps.PlayerWarpManager;

public interface PointsService {
    GlobalWarpManager getGlobalManager();
    
    PlayerWarpManager getPlayerManager();
    
    WarpsService getWarpsService();
}
