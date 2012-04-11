package com.pvminecraft.points;

import org.bukkit.configuration.file.YamlConfiguration;

public enum Config {
    debugging("debugging", false),
    checkUpdates("checkupdates", true),
    
    warpsEnabled("features.warps.enabled", true),
    warpsSocial("features.warps.social", true),
    warpsGlobals("features.warps.globals", true),
    warpsCompass("features.warps.compass", true),
    warpsInvites("features.warps.invites", true),
    
    spawnEnabled("features.spawn.enabled", true),
    spawnBed("features.spawn.bed", true),
    
    homeEnabled("features.home.enabled", true);
    
    private String path;
    private Object value;
    
    private Config(String path, Object value) {
        this.path = path;
        this.value = value;
    }
    
    public String getPath() {
        return path;
    }
    
    public Boolean getBoolean() {
        return (Boolean) value;
    }
    
    public String getString() {
        return (String) value;
    }
    
    public Integer getInteger() {
        return (Integer) value;
    }
    
    public void setValue(Object obj) {
        this.value = obj;
    } 
    
    public static void load(YamlConfiguration yaml) {
        for(Config c : values())
            if(!c.getPath().isEmpty())
                if(yaml.get(c.getPath()) != null)
                    c.setValue(yaml.get(c.getPath()));
    }
}
