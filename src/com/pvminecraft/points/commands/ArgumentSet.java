package com.pvminecraft.points.commands;

import com.pvminecraft.points.utils.Pair;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * Represents one specific set of possible arguments to a command.
 * Usually, this disinguished sub-commands and can be used to enforce
 * an expected number of variable parameters.
 */
public abstract class ArgumentSet {
    protected Pattern pattern;
    protected String regex;
    protected Permission[] perms;
    protected Command base;
    protected JavaPlugin plugin;
    
    // Regex (pat) is used to match the set. \\w+ represents any old word which
    // is your parameters usually.
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
    
    // Key value pairs for command and description. Automatically compiled
    // when help is called.
    public abstract Pair<String, String> getHelp();
}
