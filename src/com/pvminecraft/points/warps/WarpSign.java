/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.warps;

import org.bukkit.block.Sign;

/**
 *
 * @author s0lder
 */
public class WarpSign {
    private Sign sign;
    private Warp target;
    
    public WarpSign(Sign block, Warp warp) {
        sign = block;
        target = warp;
    }
    
    public Sign getSign() {
        return sign;
    }
    
    public Warp getTarget() {
        return target;
    }
}
