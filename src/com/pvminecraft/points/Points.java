package com.pvminecraft.points;

import com.pvminecraft.points.commands.*;
import com.pvminecraft.points.commands.home.HomeDefault;
import com.pvminecraft.points.commands.home.HomeSet;
import com.pvminecraft.points.commands.spawn.SpawnBed;
import com.pvminecraft.points.commands.spawn.SpawnDefault;
import com.pvminecraft.points.commands.spawn.SpawnSet;
import com.pvminecraft.points.commands.warp.*;
import com.pvminecraft.points.homes.HomeManager;
import com.pvminecraft.points.utils.ClassPathAdder;
import com.pvminecraft.points.utils.Downloader;
import com.pvminecraft.points.warps.GlobalWarpManager;
import com.pvminecraft.points.warps.PlayerWarpManager;
import java.io.File;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Points extends JavaPlugin implements PointsService {
    private CommandHandler commands;
    private HomeManager homeManager;
    private PlayerWarpManager playerManager;
    private GlobalWarpManager globalManager;
    public static final String dbURL = "https://github.com/s0lder/FlatDB/blob/master/download/FlatDB.jar?raw=true";
    
    @Override
    public void onDisable() {
        playerManager.save();
        globalManager.save();
        homeManager.save();
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
        homeManager = new HomeManager(this);
        playerManager = new PlayerWarpManager(this);
        globalManager = new GlobalWarpManager(this);
        
        homeManager.load();
        playerManager.load();
        globalManager.load();
        setupCommands();
        
        sm.register(PointsService.class, this, this, ServicePriority.Normal);
        System.out.println("[Points] Points is now active.");
    }
    
    private void setupCommands() {
        commands = new CommandHandler(this);
    
        Command spawnCommand = new Command("spawn", this);
        ArgumentSet spawnDefault = new SpawnDefault(spawnCommand, "", this, new Permission("points.spawn"));
        ArgumentSet spawnBed = new SpawnBed(spawnCommand, "bed", this, new Permission("points.spawn"));
        ArgumentSet spawnSet = new SpawnSet(spawnCommand, "set", this, new Permission("points.admin"));
        spawnCommand.addArgument(spawnDefault);
        spawnCommand.addArgument(spawnBed);
        spawnCommand.addArgument(spawnSet);
        commands.addCommand(spawnCommand);
        
        Command setHome = new Command("sethome", this);
        ArgumentSet homeSet = new HomeSet(setHome, "", this, new Permission("points.home"));
        setHome.addArgument(homeSet);
        Command homeCommand = new Command("home", this);
        ArgumentSet home = new HomeDefault(setHome, "", this, new Permission("points.home"));
        homeCommand.addArgument(home);
        commands.addCommand(setHome);
        commands.addCommand(homeCommand);
        
        Command warpCommand = new Command("warp", this);
        Permission warpPerm = new Permission("points.warp");
        ArgumentSet warpNew = new WarpNew(warpCommand, "new \\w+$", this, warpPerm);
        ArgumentSet warpDelete = new WarpDelete(warpCommand, "delete \\w+$", this, warpPerm);
        ArgumentSet warpGo = new WarpGo(warpCommand, "go \\w+$", this, warpPerm);
        ArgumentSet warpGoPlayer = new WarpGoPlayer(warpCommand, "go \\w+ \\w+$", this, warpPerm);
        ArgumentSet warpGlobal = new WarpGlobal(warpCommand, "global \\w+$", this, warpPerm);
        ArgumentSet warpPublic = new WarpPublic(warpCommand, "public \\w+$", this, warpPerm);
        ArgumentSet warpList = new WarpList(warpCommand, "list", this, warpPerm);
        ArgumentSet warpListPlayer = new WarpListPlayer(warpCommand, "list \\w+$", this, warpPerm);
        ArgumentSet warpInfo = new WarpInfo(warpCommand, "info \\w+$", this, warpPerm);
        ArgumentSet warpFind = new WarpFind(warpCommand, "find \\w+$", this, warpPerm);
        ArgumentSet warpFindReset = new WarpFindReset(warpCommand, "find", this, warpPerm);
        warpCommand.addArgument(warpNew);
        warpCommand.addArgument(warpDelete);
        warpCommand.addArgument(warpGo);
        warpCommand.addArgument(warpGoPlayer);
        warpCommand.addArgument(warpGlobal);
        warpCommand.addArgument(warpPublic);
        warpCommand.addArgument(warpList);
        warpCommand.addArgument(warpListPlayer);
        warpCommand.addArgument(warpInfo);
        warpCommand.addArgument(warpFind);
        warpCommand.addArgument(warpFindReset);
        
        commands.addCommand(warpCommand);
        
        getCommand("warp").setExecutor(commands);
        // getCommand("points").setExecutor(pointsCommand);
        getCommand("spawn").setExecutor(commands);
        getCommand("home").setExecutor(commands);
        getCommand("sethome").setExecutor(commands);
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
    
    public HomeManager getHomeManager() {
        return homeManager;
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
