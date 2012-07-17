package com.pvminecraft.points.storage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlDatabase implements Database {
    
    private YamlConfiguration config;
    private Record root;
    
    public YamlDatabase() {
        this.config = new YamlConfiguration();
        this.root = new Record("root");
    }

    @Override
    public Record getRoot() {
        return root;
    }

    @Override
    public boolean load(File file) {
        try {
            config.load(file);
            root = fromSection(config);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private Record fromSection(ConfigurationSection section) {
        Record ret = new Record(section.getName());
        System.out.println("Working " + ret.getName());
        Set<String> keys = section.getKeys(false);
        for(String key : keys) {
            if(section.isConfigurationSection(key))
                ret.addChild(fromSection(section.getConfigurationSection(key)));
            else
                ret.set(key, section.get(key));
        }
        return ret;
    }

    @Override
    public boolean save(File file) {
        try {
            config = new YamlConfiguration();
            fromRecord(root, config.getRoot());
            config.save(file);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
    private ConfigurationSection fromRecord(Record record, ConfigurationSection base) {
        List<Record> children = record.getChildren();
        for(Record r : children) {
            ConfigurationSection s = base.createSection(r.getName());
            fromRecord(r, s);
        }
        for(Object key : record.getKeys())
            base.set(key.toString(), record.get(key));
        return base;
    }

}
