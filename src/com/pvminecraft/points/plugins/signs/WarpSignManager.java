/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.plugins.signs;

import com.pvminecraft.FlatDB.FlatDB;
import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.plugins.PointsPlugin;
import com.pvminecraft.points.utils.Locations;
import com.pvminecraft.points.warps.PlayerWarpManager;
import com.pvminecraft.points.warps.OwnedWarp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.block.Sign;

/**
 *
 * @author s0lder
 */
public class WarpSignManager implements PointsPlugin {
    private Points plugin;
    private HashMap<Location, WarpSign> signs;
    private SignBlockListener blockListener;
    private SignPlayerListener playerListener;
    
    public WarpSignManager(Points pl) {
        PlayerWarpManager manager = (PlayerWarpManager) pl.getPlayerManager();
        signs = new HashMap<Location, WarpSign>();
        plugin = pl;
        blockListener = new SignBlockListener(pl, manager, this);
        playerListener = new SignPlayerListener(pl, manager, this);
    }
    
    public void loadSigns(String dir) {
        FlatDB signDb = new FlatDB(dir, "signs.db");
        List<Row> rows = signDb.getAll();
        for(Row r : rows) {
            WarpSign sign = WarpSign.fromRow(r, plugin, (PlayerWarpManager) plugin.getPlayerManager());
            if(sign == null)
                continue;
            signs.put(sign.getLocation(), sign);
        }
    }
    
    public void saveSigns(String dir) {
        FlatDB db = new FlatDB(dir, "signs.db");
        db.removeAll();
        Iterator it = signs.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            WarpSign sign = (WarpSign) pairs.getValue();
            OwnedWarp target = sign.getTarget();
            String w = target.getOwner() + ";" + target.getName();
            Row row = Locations.locToRow(sign.getLocation(), String.valueOf(sign.hashCode()));
            row.addElement("warp", w);
            db.addRow(row);
            db.update();
        }
    }
    
    public void addSign(WarpSign ws) {
        signs.put(ws.getLocation(), ws);
    }

    public void removeSign(Location loc) {
        if(signs.containsKey(loc))
            signs.remove(loc);
    }

    public WarpSign getSign(Sign sg) {
        Location loc = sg.getBlock().getLocation();
        return signs.get(loc);
    }

    @Override
    public void disable() {
        saveSigns(plugin.getDataFolder().getPath());
    }

    @Override
    public void enable() {
        loadSigns(plugin.getDataFolder().getPath());
    }

}
