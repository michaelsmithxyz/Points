package com.pvminecraft.points.db.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author s0lder
 */
public class FileManager {
    public String fileName;
    public String dirName;
    public LinkedList<String> lines = new LinkedList<String>();
    
    public FileManager(String dir, String name) {
        this.fileName = name;
        this.dirName = dir;
    }
    
    public boolean createNotExists() {
        return createNotExists(dirName, fileName);
    }
    
    public boolean createNotExists(String dir, String file) {
        return create(dir, file);
    }
    
    public boolean create() {
        return create(dirName, fileName);
    }
    
    public boolean create(String dir, String file) {
        try {
            createDir(dir);
            (new File(dir, file)).createNewFile();
            return true;
        } catch(IOException e) {
            return false;
        }
    }
    
    public boolean createDir(String dir) {
        return (new File(dir)).mkdirs();
    }
    
    public boolean readLines() {
        BufferedReader br;
        String line;
        lines = new LinkedList<String>();
        try {
            br = new BufferedReader(new FileReader(new File(dirName, fileName)));
            while((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
            return true;
        } catch(IOException e) {
            return false;
        }
    }
    
    public void appendLine(String line) {
        lines.add(line);
    }
    
    public boolean write() {
        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(new File(dirName, fileName)));
            for(String l : lines) {
                out.write(l);
                out.newLine();
            }
            out.flush();
            out.close();
            return true;
        } catch(IOException e) {
            return false;
        }
    }
    
    public String getLine(int num) {
        return lines.get(num);
    }
    
    public LinkedList<String> getLines() {
        return lines;
    }
    
    public void clear() {
        lines = new LinkedList<String>();
    }
}
