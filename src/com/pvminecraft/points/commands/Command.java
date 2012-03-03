package com.pvminecraft.points.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Command {
    private String base;
    private String pattern;
    private List<ArgumentSet> execs;
    private CommandHelp help;
    private JavaPlugin plugin;
    
    public Command(String base, JavaPlugin plugin) {
        this.base = base;
        this.execs = new ArrayList<ArgumentSet>();
        this.pattern = "^" + base;
        this.plugin = plugin;
    }
    
    public void addHelp(CommandHelp help) {
        this.help = help;
        for(ArgumentSet arg : execs)
            this.help.addEntry(arg.getHelp());
    }
    
    public String getPattern() {
        return pattern;
    }
    
    public String getBase() {
        return base;
    }
    
    public void addArgument(ArgumentSet arg) {
        execs.add(arg);
    }
    
    public boolean execute(CommandSender sender, String command, String[] args) {
        // Special Case for help and ?
        if(args.length > 0 && (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")))
            return executeHelp(sender, args);
        for(ArgumentSet arg : execs)
            if(arg.match(command))
                return arg.execute(sender, command, args);
        return executeHelp(sender, args);
    }
    
    public boolean executeHelp(CommandSender sender, String[] args) {
        if(!help.hasEntries()) return false;
        if(args.length > 1) {
            try {
                help.displayHelp(sender, Integer.parseInt(args[1]));
            } catch(NumberFormatException e) {
                help.displayHelp(sender, 1);
            }
            return true;
        }
        help.displayHelp(sender, 1);
        return true;
    }
}
