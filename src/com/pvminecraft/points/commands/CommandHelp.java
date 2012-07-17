package com.pvminecraft.points.commands;

import com.pvminecraft.points.utils.Pair;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandHelp {
    private List<Pair<String, String>> entries;
    private Command command;
    public static final int ITEMS_PER_PAGE = 7;
    
    public CommandHelp(Command command) {
        this.command = command;
        entries = new LinkedList<Pair<String, String>>();
    }
    
    public void addEntry(String notation, String desc) {
        entries.add(new Pair<String, String>(notation, desc));
    }
    
    public void addEntry(Pair<String, String> help) {
        if(help == null) return;
        entries.add(help);
    }
    
    public boolean hasEntries() {
        return entries.size() > 0;
    }
    
    // Generate help message
    public void displayHelp(CommandSender sender, int page) {
        if(entries.isEmpty()) return;
        int numPages = (int) Math.ceil((double) entries.size() / (double) ITEMS_PER_PAGE);
        if(page < 1) page = 1;
        if(page > numPages) page = numPages;
        int offset = (page - 1) * ITEMS_PER_PAGE;
        sender.sendMessage(ChatColor.YELLOW + "-- Help for /" + command.getBase() + ": --");
        for(int i = offset; i < offset + ITEMS_PER_PAGE && i < entries.size(); i++) {
            String not = entries.get(i).a;
            String des = entries.get(i).b;
            sender.sendMessage(ChatColor.BLUE + not + ChatColor.WHITE + " - " +
                    ChatColor.GREEN + des);
        }
        sender.sendMessage(ChatColor.YELLOW + "-- Page " + page + " of " + numPages + " --");
    }
}
