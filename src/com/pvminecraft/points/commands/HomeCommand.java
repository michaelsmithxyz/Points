package com.pvminecraft.points.commands;

import static com.pvminecraft.points.Points._;
import com.pvminecraft.FlatDB.FlatDB;
import com.pvminecraft.FlatDB.Row;
import com.pvminecraft.points.Points;
import com.pvminecraft.points.utils.Locations;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author s0lder
 */
public class HomeCommand implements CommandExecutor {
    private Points plugin;
    private FlatDB db;
    private HashMap<String, Location> homes;
    
    public HomeCommand(Points instance) {
        plugin = instance;
        db = new FlatDB(plugin.getDataFolder().getPath(), "homes.db");
        homes = new HashMap<String, Location>();
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
        Player pl = (Player) cs;
        if(!pl.hasPermission("points.home")) {
            pl.sendMessage(_("alertNoPerm"));
            return true;
        }
        if("sethome".equals(label)) {
            Location currLoc = pl.getLocation();
            homes.put(pl.getName(), currLoc);
            pl.sendMessage(_("setHome", 
                    new Object[] {(int)currLoc.getX(), (int)currLoc.getY(), (int)currLoc.getZ()}
                    ));
            return true;
        } else {
            if(homes.containsKey(pl.getName())) {
                sendHome(pl);
                pl.sendMessage(_("welcome"));
                return true;
            } else {
                pl.sendMessage(_("noHome"));
                return true;
            }
        }
    }
    
    public void sendHome(Player pl) {
        Location home = homes.get(pl.getName());
        pl.teleport(home);
    }
    
    public void loadHomes() {
        Location location;
        List<Row> allHomes = db.getAll();
        for(Row row : allHomes) {
            location = Locations.fromRow(row, plugin);
            homes.put(row.getIndex(), location);
        }
    }
    
    public void saveHomes() {
        Row row;
        Iterator it = homes.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            String playerName = (String) pairs.getKey();
            row = Locations.locToRow((Location) pairs.getValue(), playerName);
            db.addRow(row);
        }
        db.update();
    }
    
}
