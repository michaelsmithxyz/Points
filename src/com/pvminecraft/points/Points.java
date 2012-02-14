package com.pvminecraft.points;

import com.pvminecraft.points.commands.HomeCommand;
import com.pvminecraft.points.commands.PointsCommand;
import com.pvminecraft.points.commands.SpawnCommand;
import com.pvminecraft.points.commands.WarpCommand;
import com.pvminecraft.points.utils.ClassPathAdder;
import com.pvminecraft.points.utils.Downloader;
import com.pvminecraft.points.warps.GlobalWarpManager;
import com.pvminecraft.points.warps.PlayerWarpManager;
import java.io.File;
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
    public static final String dbURL = "https://github.com/s0lder/FlatDB/blob/master/download/FlatDB.jar?raw=true";
    
    @Override
    public void onDisable() {
        homesCommand.saveHomes();
        playerManager.save();
        globalManager.save();
    }

    @Override
    public void onEnable() {
        if(!checkLibs("lib/")) {
            System.err.println("[Points] Could not download required libraries!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        ServicesManager sm = getServer().getServicesManager();
        
        Messages.buildMessages();
        playerManager = new PlayerWarpManager(this);
        globalManager = new GlobalWarpManager(this);
        homesCommand = new HomeCommand(this);
        warpCommand = new WarpCommand(this);
        pointsCommand = new PointsCommand(this);
        spawnCommand = new SpawnCommand();
        
        homesCommand.loadHomes();
        playerManager.load();
        globalManager.load();
        
        getCommand("home").setExecutor(homesCommand);
        getCommand("sethome").setExecutor(homesCommand);
        getCommand("warp").setExecutor(warpCommand);
        getCommand("points").setExecutor(pointsCommand);
        getCommand("spawn").setExecutor(spawnCommand);
        
        sm.register(PointsService.class, this, this, ServicePriority.Normal);
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
    public PlayerWarpManager getPlayerManager() {
        return playerManager;
    }
    
    @Override
    public GlobalWarpManager getGlobalManager() {
        return globalManager;
    }

    private boolean checkLibs(String dir) {
        //Check for lib/
        File lib = new File(dir);
        if(!lib.exists())
            lib.mkdirs();
        //Check for FlatDB.jar
        File jar = new File(dir, "FlatDB.jar");
        if(!jar.exists()){
            System.out.println("[Points] Downloading FlatDB.jar");
            boolean down = Downloader.getFile(dbURL, jar.getPath());
            boolean load = ClassPathAdder.addFile(jar);
            return down && load;
        }
        return true;
    }
}
