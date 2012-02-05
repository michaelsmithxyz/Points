package com.pvminecraft.points.warps;

import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author michael
 */
public interface WarpManager {
    void addWarp(Warp warp);
    
    Warp getWarp(String player, String name);
    
    void removeWarp(Warp warp);
    
    List<Warp> getWarps(String player);
    
    List<Warp> getAll(); 
    
    void sendTo(Player player, Warp warp);
    
    void load();
    
    void save();
}
