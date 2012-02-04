package com.pvminecraft.points.warps;

import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author michael
 */
public interface WarpManager {
    void addWarp(String player, Warp warp);
    
    void addWarp(Player player, Warp warp);
    
    Warp getWarp(String player, String name);
    
    Warp getWarp(Player player, String name);
    
    List<Warp> getWarps(String player);
    
    List<Warp> getWarps(Player player);
    
    List<Warp> getAll(); 
    
    void sendTo(Player player, Warp warp);
}
