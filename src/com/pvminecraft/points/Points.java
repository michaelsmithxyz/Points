package com.pvminecraft.points;

import com.pvminecraft.points.commands.HomeCommand;
import com.pvminecraft.points.commands.PointsCommand;
import com.pvminecraft.points.commands.WarpCommand;
import com.pvminecraft.points.plugins.PointsPlugin;
import com.pvminecraft.points.plugins.signs.WarpSignManager;
import com.pvminecraft.points.warps.WarpManager;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
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
    private WarpManager warpManager;
    private PointsCommand pointsCommand;
    private PointsPlugin signPlugin;
    private static HashMap<String, MessageFormat> messages;
    
    @Override
    public void onDisable() {
        homesManager.saveHomes();
        warpManager.savePlayers(getDataFolder().getPath());
        signPlugin.disable();
    }

    @Override
    public void onEnable() {
        Points.buildMessages();
        homesManager = new HomeCommand(this);
        warpManager = new WarpManager(this);
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
    
    public WarpManager getWarpManager() {
        return warpManager;
    }

    public static String _(String message, Object[] arr) {
        if(messages == null)
            buildMessages();
        MessageFormat msg = messages.get(message);
        if(msg == null)
            return "";
        if(arr != null)
            return msg.format(arr);
        return msg.toPattern();
    }

    public static String _(String message) {
        return _(message, null);
    }

    public static void buildMessages() {
        Properties messageProps = new Properties();
        InputStream in = Points.class.getResourceAsStream("resources/messages.properties");
        messages = new HashMap<String, MessageFormat>();
        Enumeration en;
        try {
            messageProps.load(in);
        } catch(IOException ex) {
            System.err.println("Couldn't read message properties file!");
            return;
        }
        en = messageProps.propertyNames();
        while(en.hasMoreElements()) {
            String key = (String)en.nextElement();
            String prop = messageProps.getProperty(key);
            MessageFormat form = new MessageFormat(prop.replaceAll("&", "\u00a7").replaceAll("`", ""));
            messages.put(key, form);
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        if(command.getName().equalsIgnoreCase("spawn")) {
            if(!player.hasPermission("points.spawn")) {
                player.sendMessage(_("alertNoPerm"));
                return true;
            }
            if(args.length < 0 && args[0].equalsIgnoreCase("bed")) {
                Location bed = player.getBedSpawnLocation();
                if(bed != null)
                    teleportTo(player, bed);
                else
                    teleportTo(player, player.getLocation().getWorld().getSpawnLocation());
                return true;
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
