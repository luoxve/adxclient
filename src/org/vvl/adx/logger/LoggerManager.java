package org.vvl.adx.logger;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LoggerManager {
    private static HashMap<String, Logger> loggers = new LinkedHashMap<String, Logger>();

    public static String[] getLoggerNames() {
        Set<String> names = loggers.keySet();
        return names.toArray(new String[0]);
    }

    public static Logger getLogger(String name) {
        Logger logger = loggers.get(name);
        if (logger == null) {
            logger = new SimpleLogger(name);
            loggers.put(name, logger);
        }
        return logger;
    }

    public static Logger[] getLoggers() {
        return loggers.values().toArray(new Logger[0]);
    }

    public static void reset(File configFile) throws IOException {
        for (Logger logger : loggers.values()) {
            logger.setLevel(Logger.Level.DEBUG);
            logger.setOutput(System.out);
        }
        Map<String, String> properties = getProperties(configFile);
        for (String key : properties.keySet()) {
            int index = key.indexOf('.');
            if (index == -1) {
                continue;
            }
            String value = properties.get(key);
            String name = key.substring(0, index);
            String surfix = key.substring(index + 1);
            Logger logger = getLogger(name);
            if ("level".equals(surfix)) {
                logger.setLevel(Logger.Level.valueOf(value));
            } else if ("output".equals(surfix)) {
                if (value == null || value.length() == 0) {
                    logger.setOutput(null);
                } else {
                    logger.setOutput(new PrintStream(new File(value)));
                }
            }
        }
    }

    private static Map<String, String> getProperties(File file) throws IOException {
        if (!file.exists()) {
            return new LinkedHashMap<String, String>();
        }
        return getProperties(new FileInputStream(file));
    }

    private static Map<String, String> getProperties(InputStream in) throws IOException {
        Map<String, String> properties = new LinkedHashMap<String, String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                int index = line.indexOf('=');
                if (index > 0) {
                    String name = line.substring(0, index);
                    String value = line.substring(index + 1);
                    properties.put(name, value);
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return properties;
    }

}
