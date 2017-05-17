package org.vvl.adx.logger;

import java.io.PrintStream;

/**
 * A simple logging facade that is intended simply to capture the style of logging.
 */
public interface Logger {

    public static enum Level {
        IGNORE,
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL,
    }
    /**
     * @return the name of this logger
     */
    String getName();

    /**
     * Formats and logs at warn level.
     *
     * @param msg  the formatting string
     * @param args the optional arguments
     */
    void warn(String msg, Object... args);

    /**
     * Logs the given Throwable information at warn level
     *
     * @param thrown the Throwable to log
     */
    void warn(Throwable thrown);

    /**
     * Logs the given message at warn level, with Throwable information.
     *
     * @param msg    the message to log
     * @param thrown the Throwable to log
     */
    void warn(String msg, Throwable thrown);

    /**
     * Formats and logs at info level.
     *
     * @param msg  the formatting string
     * @param args the optional arguments
     */
    void info(String msg, Object... args);

    /**
     * Logs the given Throwable information at info level
     *
     * @param thrown the Throwable to log
     */
    void info(Throwable thrown);

    /**
     * Logs the given message at info level, with Throwable information.
     *
     * @param msg    the message to log
     * @param thrown the Throwable to log
     */
    void info(String msg, Throwable thrown);

    /**
     * @return whether the debug level is enabled
     */
    boolean isDebugEnabled();

    /**
     * Formats and logs at debug level.
     *
     * @param msg  the formatting string
     * @param args the optional arguments
     */
    void debug(String msg, Object... args);

    /**
     * Logs the given Throwable information at debug level
     *
     * @param thrown the Throwable to log
     */
    void debug(Throwable thrown);

    /**
     * Logs the given message at debug level, with Throwable information.
     *
     * @param msg    the message to log
     * @param thrown the Throwable to log
     */
    void debug(String msg, Throwable thrown);

    /**
     * @return whether the trace level is enabled
     */
    boolean isTraceEnabled();

    /**
     * Formats and logs at trace level.
     *
     * @param msg  the formatting string
     * @param args the optional arguments
     */
    void trace(String msg, Object... args);

    /**
     * Logs the given Throwable information at trace level
     *
     * @param thrown the Throwable to log
     */
    void trace(Throwable thrown);

    /**
     * Logs the given message at trace level, with Throwable information.
     *
     * @param msg    the message to log
     * @param thrown the Throwable to log
     */
    void trace(String msg, Throwable thrown);

    /**
     * @param name the name of the logger
     * @return a logger with the given name
     */
    Logger getLogger(String name);

    /**
     * Ignore an exception.
     * <p>This should be used rather than an empty catch block.
     *
     * @param ignored the throwable to log as ignored
     */
    void ignore(Throwable ignored);

    void setLevel(Level level);
    Level getLevel();
    void setOutput(PrintStream output);
}
