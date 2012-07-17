package com.pvminecraft.points.storage;

import java.util.*;

public class Record<K, V> {
    private String name;
    private List<Record> children;
    private Map<K, V> data;
    
    public Record(String name) {
        this.name = name;
        this.data = new HashMap<K, V>();
        this.children = new ArrayList<Record>();
    }
    
    public Record(String name, List<Record> children) {
        this(name);
        this.children = children;
    }
    
    public String getName() {
        return name;
    }
    
    public V get(K key) {
        return data.get(key);
    }
    
    public Set<K> getKeys() {
        return data.keySet();
    }
    
    public List<Record> getChildren() {
        return children;
    }
    
    public void set(K key, V value) {
        data.put(key, value);
    }
    
    public Record getChild(String name) {
        for(Record r : children)
            if(r.getName().equalsIgnoreCase(name))
                return r;
        return null;
    }
    
    public void addChild(Record record) {
        this.children.add(record);
    }
}
