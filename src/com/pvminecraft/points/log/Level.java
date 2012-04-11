package com.pvminecraft.points.log;

public enum Level {
    ERROR("[Points] ERROR:"),
    DEBUG("[Points] DEBUG:"),
    MESSAGE("[Points]"),
    NONE("");
    
    private String prefix;
    
    private Level(String prefix) {
        this.prefix = prefix;
    }
    
    public String getPrefix() {
        return prefix;
    }
}
