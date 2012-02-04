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
public class Global extends Warp {
    public Global(Location to, String owner, String name) {
        super(to, owner, name);
        setVisible(true);
        setOwner("server");
    }
    
    public static Global fromWarp(Warp warp) {
        return new Global(warp.getTarget(), warp.getOwner(), warp.getName());
    }
}
