package com.pvminecraft.points.commands;

import com.pvminecraft.points.utils.Pair;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ArgumentSet {
    private Pattern pattern;
    private String regex;
    private Permission[] perms;
    private Command base;
    private JavaPlugin plugin;
    
    public ArgumentSet(Command base, String pat, JavaPlugin plugin, Permission ... perms)
            throws PatternSyntaxException {
        this.base = base;
        this.perms = perms;
        if(pat.length() < 1)
            this.regex = base.getPattern();
        else
            this.regex = base.getPattern() + " " + pat;
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        this.plugin = plugin;
    }
    
    public Command getBase() {
        return base;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Permission[] getPerms() {
        return perms;
    }

    public String getRegex() {
        return regex;
    }
    
    public boolean match(String cmd) {
        return pattern.matcher(cmd).matches();
    }
    
    public boolean hasPermission(CommandSender sender) {
        for(Permission perm : perms)
            if(!(sender.hasPermission(perm)))
                return false;
        return true;
    }
    
    public abstract boolean execute(CommandSender sender, String command, String[] args);
    
    public abstract Pair<String, String> getHelp();
}
