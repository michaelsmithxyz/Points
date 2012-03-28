package com.pvminecraft.points.commands.warp;

import com.pvminecraft.points.commands.Command;
import com.pvminecraft.points.utils.Pair;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpListGlobal extends WarpList {
    public WarpListGlobal(Command base, String pat, JavaPlugin plugin, Permission ... perms) {
        super(base, pat, plugin, perms);
    }

    @Override
    public Pair<String, String> getHelp() {
        return new Pair<String, String>("/warp list globals", "List all global warps");
    }
}