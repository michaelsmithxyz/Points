/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points;

import com.pvminecraft.points.warps.WarpManager;

/**
 *
 * @author michael
 */
public interface Service {
    WarpManager getGlobalManager();
    
    WarpManager getPlayerManager();
}
