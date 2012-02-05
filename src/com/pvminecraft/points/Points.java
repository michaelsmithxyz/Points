package com.pvminecraft.points;

import com.pvminecraft.points.commands.HomeCommand;
import com.pvminecraft.points.commands.PointsCommand;
import com.pvminecraft.points.commands.SpawnCommand;
import com.pvminecraft.points.commands.WarpCommand;
import com.pvminecraft.points.plugins.PointsPlugin;
import com.pvminecraft.points.plugins.signs.WarpSignManager;
import com.pvminecraft.points.warps.GlobalWarpManager;
import com.pvminecraft.points.warps.PlayerWarpManager;
import com.pvminecraft.points.warps.WarpManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author s0lder
 */
public class Points extends JavaPlugin implements PointsService {
    private HomeCommand homesCommand;
    private WarpCommand warpCommand;
    private PointsCommand pointsCommand;
    private SpawnCommand spawnCommand;
    private PlayerWarpManager playerManager;
    private GlobalWarpManager globalManager;
    private PointsPlugin signPlugin;
    
    @Override
    public void onDisable() {
        homesCommand.saveHomes();
        playerManager.save();
        globalManager.save();
        signPlugin.disable();
    }

    @Override
    public void onEnable() {
        ServicesManager sm = getServer().getServicesManager();
        
        Messages.buildMessages();
        homesCommand = new HomeCommand(this);
        warpCommand = new WarpCommand(this);
        pointsCommand = new PointsCommand(this);
        spawnCommand = new SpawnCommand();
        playerManager = new PlayerWarpManager(this);
        globalManager = new GlobalWarpManager(this);
        
        signPlugin = new WarpSignManager(this);
        
        homesCommand.loadHomes();
        playerManager.load();
        globalManager.load();
        
        getCommand("home").setExecutor(homesCommand);
        getCommand("sethome").setExecutor(homesCommand);
        getCommand("warp").setExecutor(warpCommand);
        getCommand("points").setExecutor(pointsCommand);
        getCommand("spawn").setExecutor(spawnCommand);

        signPlugin.enable();
        
        sm.register(PointsService.class, this, this, ServicePriority.High);
        System.out.println("[Points] Points is now active.");
    }
    
    public static void teleportTo(Player pl, Location loc) {
        if(loc.getBlock().getTypeId() != 0) {
            Location locN = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
            teleportTo(pl, locN);
        } else {
            pl.teleport(loc);
        }
    }
    
    //PointsService Implementation
    
    @Override
    public WarpManager getPlayerManager() {
        return playerManager;
    }
    
    @Override
    public WarpManager getGlobalManager() {
        return globalManager;
    }
}
