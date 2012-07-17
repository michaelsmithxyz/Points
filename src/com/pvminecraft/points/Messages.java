package com.pvminecraft.points;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/*
 * This class handles all prebaked messages displayed by Points.
 * They are all stored in the JAR in a .properties file.
 */

public class Messages {

    private static HashMap<String, MessageFormat> messages;

    // The name _ is just for the sake of space
    public static String _(String message, Object... arr) {
        if(messages == null)
            buildMessages();
        MessageFormat msg = messages.get(message);
        if(msg == null)
            return "";
        if(arr != null)
            return msg.format(arr);
        return msg.toPattern();
    }

    public static String _(String message) {
        return _(message, (Object) null);
    }

    public static void buildMessages() {
        Properties messageProps = new Properties();
        InputStream in = Messages.class.getResourceAsStream("resources/messages.properties");
        messages = new HashMap<String, MessageFormat>();
        Enumeration en;
        try {
            messageProps.load(in);
        } catch(IOException ex) {
            System.err.println("Couldn't read message properties file!");
            return;
        } catch(NullPointerException ex) {
            System.err.println("Couldn't read message properties file!");
            if(in == null)
                System.err.println("The InputStream is null!");
            return;
        }
        en = messageProps.propertyNames();
        while(en.hasMoreElements()) {
            String key = (String)en.nextElement();
            String prop = messageProps.getProperty(key);
            MessageFormat form = new MessageFormat(prop.replaceAll("&", "\u00a7").replaceAll("`", ""));
            messages.put(key, form);
        }
        System.out.println("[Points] Loaded Messages");
    }
}
