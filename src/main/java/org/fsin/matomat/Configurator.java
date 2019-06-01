package org.fsin.matomat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Configurator {
    private static volatile Configurator config = null;

    private final HashMap<String, String> configuration;

    private Configurator(String file) {
        configuration = readConfiguration(file);
    }

    public static void init(String configFile) {
        config = new Configurator(configFile);
    }

    public static Configurator getInstance() {
        if(config == null) {
            throw new RuntimeException("Configurator.init() was not executed yet");
        }
        return config;
    }

    public String getValueString(String key) {
        if(configuration.get(key) == null)
            throw new RuntimeException(key + " not knwon");
        return configuration.get(key);
    }

    public int getValueInt(String key) {
        try {
            return Integer.valueOf(getValueString(key));
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(key + " is no number", nfe);
        }
    }

    public boolean getValueBool(String key) {
        try {
            return getValueString(key).toLowerCase().equals("true");
        } catch (Exception e) {
            throw new RuntimeException(key + " could not be identified as boolean", e);
        }
    }

    private HashMap<String, String> readConfiguration(String fileName) {
        try {
            HashMap<String, String> conf = new HashMap<>();
            BufferedReader file = new BufferedReader(new FileReader(fileName));
            String line = "";
            ArrayList<String> content = new ArrayList<>();
            while ((line = file.readLine()) != null) {
                if(!line.isEmpty() && !line.startsWith("#")) {  //ignore comend
                    String[] pair = line.split("=");
                    conf.put(pair[0], pair[1]);
                }
            }
            file.close();
            return conf;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
