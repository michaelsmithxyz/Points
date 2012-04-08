package com.pvminecraft.points.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Downloader {
    public static boolean getFile(String target, String dest) {
        try {
            return getFile(new URL(target), dest);
        } catch (MalformedURLException ex) {
            return false;
        }
    }
    
    public static boolean getFile(URL url, String dest) {
        try {
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream out = new FileOutputStream(dest);
            out.getChannel().transferFrom(rbc, 0, 1 << 24);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public static boolean copyFile(InputStream resource, String path) {
        try {
            File target = new File(path);
            if(!target.exists())
                target.createNewFile();
            OutputStream out = new FileOutputStream(target);
            int next = resource.read();
            while(next != -1) {
                out.write(next);
                next = resource.read();
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
