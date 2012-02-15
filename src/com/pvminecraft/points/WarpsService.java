package com.pvminecraft.points;

import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.warps.OwnedWarp;
import com.pvminecraft.points.warps.Warp;
import org.bukkit.Location;
import org.bukkit.Server;

public class WarpsService {
    public static Warp createWarp(Location location, String name) {
        return Warp.createWarp(location, name);
    }
    
    public static OwnedWarp createOwnedWarp(Location location, String name, String owner) {
        return OwnedWarp.createWarp(location, name, owner);
    }
    
    public static OwnedWarp creaOwnedWarp(Location location, String name, String owned, boolean vis) {
        return OwnedWarp.createWarp(location, name, owned, vis);
    }
    
    public static Warp warpFromRow(Row row, Server server) {
        return Warp.fromRow(row, server);
    }
    
    public static OwnedWarp ownedWarpFromRow(Row row, Server server, String owner) {
        return OwnedWarp.fromRow(row, server, owner);
    }
    
    public static Row warpToRow(Warp warp) {
        return Warp.toRow(warp);
    }
    
    public static Row ownedWarpToRow(OwnedWarp warp) {
        return OwnedWarp.toRow(warp);
    }
}
