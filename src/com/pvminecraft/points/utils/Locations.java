package com.pvminecraft.points.utils;

import com.pvminecraft.FlatDB.Row;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

/**
 *
 * @author s0lder
 */
public class Locations {
    public static Location fromRow(Row row, Server server) {
        String world = row.getElement("world"),
                x = row.getElement("x"),
                y = row.getElement("y"),
                z = row.getElement("z"),
                pitch = row.getElement("pitch"),
                yaw = row.getElement("yaw");  
        World gameWorld = server.getWorld(world);
        return new Location(gameWorld, Double.parseDouble(x),
                Double.parseDouble(y), Double.parseDouble(z), Float.parseFloat(yaw),
                Float.parseFloat(pitch));
    }
    
    public static Row locToRow(Location loc, String index) {
        String world = loc.getWorld().getName(),
                x = String.valueOf(loc.getX()),
                y = String.valueOf(loc.getY()),
                z = String.valueOf(loc.getZ()),
                pitch = String.valueOf(loc.getPitch()),
                yaw = String.valueOf(loc.getYaw());
        Row row = new Row(index);
        row.addElement("world", world);
        row.addElement("x", x);
        row.addElement("y", y);
        row.addElement("z", z);
        row.addElement("yaw", yaw);
        row.addElement("pitch", pitch);
        return row;
    }
}
