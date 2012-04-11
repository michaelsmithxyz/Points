package com.pvminecraft.points;

import com.pvminecraft.points.commands.ArgumentSet;
import com.pvminecraft.points.commands.Command;
import com.pvminecraft.points.commands.CommandHandler;
import com.pvminecraft.points.commands.home.HomeDefault;
import com.pvminecraft.points.commands.home.HomeSet;
import com.pvminecraft.points.commands.points.PointsAbout;
import com.pvminecraft.points.commands.points.PointsAddGlobal;
import com.pvminecraft.points.commands.points.PointsPlayers;
import com.pvminecraft.points.commands.spawn.SpawnBed;
import com.pvminecraft.points.commands.spawn.SpawnDefault;
import com.pvminecraft.points.commands.spawn.SpawnSet;
import com.pvminecraft.points.commands.warp.*;
import com.pvminecraft.points.homes.HomeManager;
import com.pvminecraft.points.log.Level;
import com.pvminecraft.points.log.Stdout;
import com.pvminecraft.points.utils.ClassPathAdder;
import com.pvminecraft.points.utils.Downloader;
import com.pvminecraft.points.warps.GlobalWarpManager;
import com.pvminecraft.points.warps.PlayerWarpManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
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
    public static final String dbURL = "http://cloud.github.com/downloads/s0lder/FlatDB/FlatDB.jar";
    public static final String vrsnURL = "http://bukget.org/api/plugin/points/latest";
    private YamlConfiguration config;
    private File confFile;
    
    @Override
    public void onDisable() {
        playerManager.save();
        globalManager.save();
        homeManager.save();
    }

    @Override
    public void onEnable() {
        if(!checkLibs("lib/")) {
            Stdout.println("Could not download required libraries!", Level.ERROR);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if(!getDataFolder().exists())
            getDataFolder().mkdirs();
        
        setupConfig();
        if(Config.checkUpdates.getBoolean())
            checkVersion();
        
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
        Stdout.println("Points is now active", Level.MESSAGE);
    }
    
    private void checkVersion() {
        String metaData = Downloader.getFile(vrsnURL);
        String[] lines;
        String version = null;
        if(metaData == null) {
            Stdout.println("Couldn't check for updates!", Level.ERROR);
            return;
        }
        lines = metaData.split("\n");
        for(int i = 0; i < lines.length; i++)
            lines[i] = lines[i].trim();
        for(String line : lines)
            if(line.matches("\"name\": \"Points [\\.0-9A-z]+\","))
                version = line; 
        if(version == null) {
            Stdout.println("Couldn't check for updates!", Level.ERROR);
            return;
        }
        version = version.replace("\"", "");
        version = version.replace(",", "");
        version = version.split(":")[1].trim();
        version = version.split(" ")[1].substring(1);
        if(version.compareTo(getDescription().getVersion()) > 0) {
            Stdout.println("A new version of Points (" + version + ") is available.", Level.MESSAGE);
            Stdout.println("        Check http://dev.bukkit.org/server-mods/points", Level.NONE);
        }
    }
    
    private void setupConfig() {
        try {
            confFile = new File(getDataFolder().getPath(), "config.yml");
            config = new YamlConfiguration();
            config.load(confFile.getPath());
            Config.load(config);
            Stdout.println("Loaded configuration", Level.MESSAGE);
        } catch(FileNotFoundException e) {
            Stdout.println("Couldn't find config.yml... Generating...", Level.MESSAGE);
            if(Downloader.copyFile(Points.class.getResourceAsStream("resources/config.yml"), confFile.getPath())) {
                Stdout.println("config.yml has been generated!", Level.MESSAGE);
                setupConfig();
            } else {
                Stdout.println("Couldn't generate config.yml! Anything goes...", Level.ERROR);
            }
        } catch(IOException e) {
            Stdout.println("Couldn't read config.yml! Anything goes...", Level.ERROR);
        } catch(InvalidConfigurationException e) {
            Stdout.println("Malformed configuration! Anything goes...", Level.ERROR);
        }
    }
    
    private void setupCommands() {
        commands = new CommandHandler(this);
        if(Config.spawnEnabled.getBoolean()) {
            Command spawnCommand = new Command("spawn", this);
            ArgumentSet spawnDefault = new SpawnDefault(spawnCommand, "", this, new Permission("points.spawn"));
            ArgumentSet spawnSet = new SpawnSet(spawnCommand, "set", this, new Permission("points.admin"));
            spawnCommand.addArgument(spawnDefault);
            spawnCommand.addArgument(spawnSet);
            if(Config.spawnBed.getBoolean()) {
                ArgumentSet spawnBed = new SpawnBed(spawnCommand, "bed", this, new Permission("points.spawn"));
                spawnCommand.addArgument(spawnBed);
            }
            commands.addCommand(spawnCommand);
            getCommand("spawn").setExecutor(commands);
        }
        
        if(Config.homeEnabled.getBoolean()) {
            Command setHome = new Command("sethome", this);
            ArgumentSet homeSet = new HomeSet(setHome, "", this, new Permission("points.home"));
            setHome.addArgument(homeSet);
            Command homeCommand = new Command("home", this);
            ArgumentSet home = new HomeDefault(homeCommand, "", this, new Permission("points.home"));
            homeCommand.addArgument(home);
            commands.addCommand(setHome);
            commands.addCommand(homeCommand);
            getCommand("home").setExecutor(commands);
            getCommand("sethome").setExecutor(commands);
        }
        
        if(Config.warpsEnabled.getBoolean()) {
            Command warpCommand = new Command("warp", this);
            Permission warpPerm = new Permission("points.warp");
            ArgumentSet warpNew = new WarpNew(warpCommand, "new \\w+$", this, warpPerm);
            ArgumentSet warpDelete = new WarpDelete(warpCommand, "delete \\w+$", this, warpPerm);
            ArgumentSet warpGo = new WarpGo(warpCommand, "go \\w+$", this, warpPerm);
            ArgumentSet warpList = new WarpList(warpCommand, "list", this, warpPerm);
            ArgumentSet warpInfo = new WarpInfo(warpCommand, "info \\w+$", this, warpPerm);
            warpCommand.addArgument(warpNew);
            warpCommand.addArgument(warpDelete);
            warpCommand.addArgument(warpGo);
            warpCommand.addArgument(warpList);     
            warpCommand.addArgument(warpInfo);
            if(Config.warpsSocial.getBoolean()) {
                ArgumentSet warpGoPlayer = new WarpGoPlayer(warpCommand, "go \\w+ \\w+$", this, warpPerm);
                warpCommand.addArgument(warpGoPlayer);
                ArgumentSet warpPublic = new WarpPublic(warpCommand, "public \\w+$", this, warpPerm);
                warpCommand.addArgument(warpPublic);
                ArgumentSet warpListPlayer = new WarpListPlayer(warpCommand, "list \\w+$", this, warpPerm);
                warpCommand.addArgument(warpListPlayer);
            }
            if(Config.warpsGlobals.getBoolean()) {
                ArgumentSet warpGlobal = new WarpGlobal(warpCommand, "global \\w+$", this, warpPerm);
                warpCommand.addArgument(warpGlobal);
                ArgumentSet warpListGlobal = new WarpListGlobal(warpCommand, "list globals$", this, warpPerm);
                warpCommand.addArgument(warpListGlobal);
            }
            if(Config.warpsCompass.getBoolean()) {
                ArgumentSet warpFindReset = new WarpFindReset(warpCommand, "find", this, warpPerm);
                warpCommand.addArgument(warpFindReset);
                ArgumentSet warpFind = new WarpFind(warpCommand, "find \\w+$", this, warpPerm);
                warpCommand.addArgument(warpFind);
            }
            if(Config.warpsInvites.getBoolean()) {
                ArgumentSet warpInvite = new WarpInvite(warpCommand, "invite \\w+ \\w+$", this, warpPerm);
                warpCommand.addArgument(warpInvite);
                ArgumentSet warpAccept = new WarpAccept(warpCommand, "accept", this, warpPerm);
                warpCommand.addArgument(warpAccept);
            }
            commands.addCommand(warpCommand);
            getCommand("warp").setExecutor(commands);
        }
        
        Command pointsCommand = new Command("points", this);
        Permission pointsPerm = new Permission("points.admin");
        ArgumentSet pointsAbout = new PointsAbout(pointsCommand, "about", this, pointsPerm);
        pointsCommand.addArgument(pointsAbout);
        ArgumentSet pointsAddGlobal = new PointsAddGlobal(pointsCommand, "addglobal \\w+$", this, pointsPerm);
        pointsCommand.addArgument(pointsAddGlobal);
        ArgumentSet pointsPlayers = new PointsPlayers(pointsCommand, "players", this, pointsPerm);
        pointsCommand.addArgument(pointsPlayers);
        commands.addCommand(pointsCommand);
        getCommand("points").setExecutor(commands);
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
            Stdout.println("Downloading FlatDB.jar", Level.MESSAGE);
            boolean down = Downloader.getFile(dbURL, jar.getPath());
            boolean load = ClassPathAdder.addFile(jar);
            return down && load;
        }
        return true;
    }
}
