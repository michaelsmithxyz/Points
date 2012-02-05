/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.warps;

import org.bukkit.Location;

/**
 *
 * @author michael
 */
public class GlobalWarp extends Warp {
    public GlobalWarp(Location to, String owner, String name) {
        super(to, owner, name);
        setVisible(true);
        setOwner("server");
    }
    
    public static GlobalWarp fromWarp(Warp warp) {
        return new GlobalWarp(warp.getTarget(), warp.getOwner(), warp.getName());
    }
}
