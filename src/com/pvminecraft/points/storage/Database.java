/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pvminecraft.points.storage;

import java.io.File;

/**
 *
 * @author michael
 */
public interface Database {
    
    public Record getRoot();
    
    public boolean load(File file);
    
    public boolean save(File file);
}
