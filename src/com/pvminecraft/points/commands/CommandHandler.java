package com.pvminecraft.points.commands;

import com.pvminecraft.points.log.Level;
import com.pvminecraft.points.log.Stdout;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * Delegates resonsibility to all commands. Generally there is one
 * instance per plugin.
 */
public class CommandHandler implements CommandExecutor {
    private JavaPlugin plugin;
    private List<com.pvminecraft.points.commands.Command> cmds;
    
    public CommandHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cmds = new ArrayList<com.pvminecraft.points.commands.Command>();
    }
    
    public void addCommand(com.pvminecraft.points.commands.Command command) {
        this.cmds.add(command);
        Stdout.println("Added command " + command.getBase(), Level.DEBUG);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
        for(com.pvminecraft.points.commands.Command cmd : cmds)
            if(label.equalsIgnoreCase(cmd.getBase())) {
                cmd.execute(cs, toFull(label, args), args);
                return true;
            }
        Stdout.println("No match for command: " + cmnd.getLabel(), Level.DEBUG);
        return true;
    }
    
    private static String toFull(String label, String[] args) {
        String ret = label;
        for(String s : args)
            ret = ret + " " + s;
        return ret;
    }
}
