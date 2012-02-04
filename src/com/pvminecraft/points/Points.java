package com.pvminecraft.points;

import com.pvminecraft.points.commands.HomeCommand;
import com.pvminecraft.points.commands.PointsCommand;
import com.pvminecraft.points.commands.WarpCommand;
import com.pvminecraft.points.plugins.PointsPlugin;
import com.pvminecraft.points.plugins.signs.WarpSignManager;
import com.pvminecraft.points.warps.PlayerWarpManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author s0lder
 */
public class Points extends JavaPlugin {
    private HomeCommand homesManager;
    private WarpCommand warpCommand;
    private PlayerWarpManager warpManager;
    private PointsCommand pointsCommand;
    private PointsPlugin signPlugin;
    
    @Override
    public void onDisable() {
        homesManager.saveHomes();
        warpManager.saveWarps(getDataFolder().getPath());
        signPlugin.disable();
    }

    @Override
    public void onEnable() {
        Messages.buildMessages();
        homesManager = new HomeCommand(this);
        warpManager = new PlayerWarpManager(this);
        warpCommand = new WarpCommand(this);
        pointsCommand = new PointsCommand(this);
        signPlugin = new WarpSignManager(this);
        
        homesManager.loadHomes();
        warpManager.loadWarps(getDataFolder().getPath());
        
        getCommand("home").setExecutor(homesManager);
        getCommand("sethome").setExecutor(homesManager);
        getCommand("warp").setExecutor(warpCommand);
        getCommand("points").setExecutor(pointsCommand);

        signPlugin.enable();
        System.out.println("[Points] Points is now active.");
    }
    
    public PlayerWarpManager getWarpManager() {
        return warpManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        if(command.getName().equalsIgnoreCase("spawn")) {
            if(!player.hasPermission("points.spawn")) {
                player.sendMessage(Messages._("alertNoPerm"));
                return true;
            }
            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("bed")) {
                    Location bed = player.getBedSpawnLocation();
                    if(bed != null)
                        teleportTo(player, bed);
                    else
                        teleportTo(player, player.getLocation().getWorld().getSpawnLocation());
                    return true;
                } else if(args[0].equalsIgnoreCase("set")) {
                    if(!player.hasPermission("points.admin")) {
                        player.sendMessage(Messages._("alertNoPerm"));
                        return true;
                    }
                    Location newLoc = player.getLocation();
                    newLoc.getWorld().setSpawnLocation(newLoc.getBlockX(),
                            newLoc.getBlockY(), newLoc.getBlockZ());
                }
            } else {
                teleportTo(player, player.getLocation().getWorld().getSpawnLocation());
                return true;
            }
        }
        return false;
    }
    
    private void teleportTo(Player pl, Location loc) {
        if(loc.getBlock().getTypeId() != 0) {
            Location locN = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
            teleportTo(pl, locN);
        } else {
            pl.teleport(loc);
        }
    }
}
