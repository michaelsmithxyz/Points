package com.pvminecraft.points.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassPathAdder {
    public static final Class[] PARAMS = new Class[] {URL.class};
    
    public static boolean addFile(File file) {
        try {
            return addURL(file.toURI().toURL());
        } catch (MalformedURLException ex) {
            return false;
        }
    }
    
    public static boolean addURL(URL url) {
        try {
            URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Class sysclass = URLClassLoader.class;
            Method m = sysclass.getDeclaredMethod("addURL", PARAMS);
            m.setAccessible(true);
            m.invoke(sysloader, new Object[] {url});
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
