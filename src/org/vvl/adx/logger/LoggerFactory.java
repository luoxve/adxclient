package org.vvl.adx.logger;

public class LoggerFactory {
    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name) {
        return LoggerManager.getLogger(name);
    }
}
