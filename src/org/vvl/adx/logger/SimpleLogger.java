package org.vvl.adx.logger;

import voyage.common.text.FastDateFormat;

import java.io.PrintStream;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SimpleLogger implements Logger {


    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private String name;
    private PrintStream output;
    private Level level = Level.DEBUG;

    public SimpleLogger(String name) {
        this.name = name;
    }

    public Logger getLogger(String name) {
        return new SimpleLogger(name);
    }

    public String getName() {
        return name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public PrintStream getOutput() {
        return output;
    }

    public void setOutput(PrintStream output) {
        this.output = output;
    }

    public boolean isTraceEnabled() {
        return isLevelEnabled(Level.TRACE);
    }

    @Override
    public void trace(String msg, Object... args) {
        if (isLevelEnabled(Level.TRACE)) {
            format(Level.TRACE, msg, args);
        }
    }

    @Override
    public void trace(Throwable thrown) {
        if (isLevelEnabled(Level.TRACE)) {
            log(Level.TRACE, null, thrown);
        }
    }

    @Override
    public void trace(String msg, Throwable thrown) {
        if (isLevelEnabled(Level.TRACE)) {
            log(Level.TRACE, msg, thrown);
        }
    }

    public boolean isDebugEnabled() {
        return isLevelEnabled(Level.DEBUG);
    }

    @Override
    public void debug(String msg, Object... args) {
        if (isLevelEnabled(Level.DEBUG)) {
            format(Level.DEBUG, msg, args);
        }
    }

    @Override
    public void debug(Throwable thrown) {
        if (isLevelEnabled(Level.DEBUG)) {
            log(Level.DEBUG, null, thrown);
        }
    }

    @Override
    public void debug(String msg, Throwable thrown) {
        if (isLevelEnabled(Level.DEBUG)) {
            log(Level.DEBUG, msg, thrown);
        }
    }

    public boolean isInfoEnabled() {
        return isLevelEnabled(Level.INFO);
    }

    @Override
    public void info(String msg, Object... args) {
        if (isLevelEnabled(Level.INFO)) {
            format(Level.INFO, msg, args);
        }
    }

    @Override
    public void info(Throwable thrown) {
        if (isLevelEnabled(Level.INFO)) {
            log(Level.INFO, null, thrown);
        }
    }

    @Override
    public void info(String msg, Throwable thrown) {
        if (isLevelEnabled(Level.INFO)) {
            log(Level.INFO, msg, thrown);
        }
    }

    public boolean isWarnEnabled() {
        return isLevelEnabled(Level.WARN);
    }

    @Override
    public void warn(String msg, Object... args) {
        if (isLevelEnabled(Level.WARN)) {
            format(Level.WARN, msg, args);
        }
    }

    @Override
    public void warn(Throwable thrown) {
        if (isLevelEnabled(Level.WARN)) {
            log(Level.WARN, null, thrown);
        }
    }

    @Override
    public void warn(String msg, Throwable thrown) {
        if (isLevelEnabled(Level.WARN)) {
            log(Level.WARN, msg, thrown);
        }
    }

    @Override
    public void ignore(Throwable ignored) {
        if (isLevelEnabled(Level.IGNORE)) {
            format(level, "", new Object[] {ignored});
        }
    }

    protected boolean isLevelEnabled(Level level) {
        return level.ordinal() >= this.level.ordinal();
    }

    protected void format(Level level, String message, Object[] argArray) {
        Throwable throwableCandidate = getThrowableCandidate(argArray);
        if (message == null) {
            log(level, null, throwableCandidate);
            return;
        }
        if (argArray == null) {
            log(level, message, null);
            return;
        }

        int i = 0;
        int j;
        StringBuffer sb = new StringBuffer(message.length() + 50);

        int L;
        for (L = 0; L < argArray.length; L++) {

            j = message.indexOf("{}", i);

            if (j == -1) {
                // no more variables
                if (i == 0) { // this is a simple string
                    log(level, message, throwableCandidate);
                    return;
                } else { // add the tail string which contains no variables and return
                    // the result.
                    sb.append(message.substring(i, message.length()));
                    log(level, sb.toString(), throwableCandidate);
                    return;
                }
            } else {
                if (isEscapedDelimeter(message, j)) {
                    if (!isDoubleEscaped(message, j)) {
                        L--; // DELIM_START was escaped, thus should not be incremented
                        sb.append(message.substring(i, j - 1));
                        sb.append('{');
                        i = j + 1;
                    } else {
                        // The escape character preceding the delimiter start is
                        // itself escaped: "abc x:\\{}"
                        // we have to consume one backward slash
                        sb.append(message.substring(i, j - 1));
                        deeplyAppendParameter(sb, argArray[L], new HashMap());
                        i = j + 2;
                    }
                } else {
                    // normal case
                    sb.append(message.substring(i, j));
                    deeplyAppendParameter(sb, argArray[L], new HashMap());
                    i = j + 2;
                }
            }
        }
        // append the characters following the last {} pair.
        sb.append(message.substring(i, message.length()));
        if (L < argArray.length - 1) {
            log(level, sb.toString(), throwableCandidate);
        } else {
            log(level, sb.toString(), null);
        }
    }

    private static Throwable getThrowableCandidate(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }
        Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable) lastEntry;
        }
        return null;
    }

    private static boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        return potentialEscape == '\\';
    }

    private static boolean isDoubleEscaped(String pattern, int delimeterStartIndex) {
        return delimeterStartIndex >= 2 && pattern.charAt(delimeterStartIndex - 2) == '\\';
    }

    private static void deeplyAppendParameter(StringBuffer sb, Object o, Map seenMap) {
        if (o == null) {
            sb.append("null");
            return;
        }
        if (!o.getClass().isArray()) {
            sb.append(o.toString());
        } else {
            // check for primitive array types because they
            // unfortunately cannot be cast to Object[]
            if (o instanceof boolean[]) {
                booleanArrayAppend(sb, (boolean[]) o);
            } else if (o instanceof byte[]) {
                byteArrayAppend(sb, (byte[]) o);
            } else if (o instanceof char[]) {
                charArrayAppend(sb, (char[]) o);
            } else if (o instanceof short[]) {
                shortArrayAppend(sb, (short[]) o);
            } else if (o instanceof int[]) {
                intArrayAppend(sb, (int[]) o);
            } else if (o instanceof long[]) {
                longArrayAppend(sb, (long[]) o);
            } else if (o instanceof float[]) {
                floatArrayAppend(sb, (float[]) o);
            } else if (o instanceof double[]) {
                doubleArrayAppend(sb, (double[]) o);
            } else {
                objectArrayAppend(sb, (Object[]) o, seenMap);
            }
        }
    }

    private static void objectArrayAppend(StringBuffer sb, Object[] a, Map seenMap) {
        sb.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                deeplyAppendParameter(sb, a[i], seenMap);
                if (i != len - 1)
                    sb.append(", ");
            }
            // allow repeats in siblings
            seenMap.remove(a);
        } else {
            sb.append("...");
        }
        sb.append(']');
    }

    private static void booleanArrayAppend(StringBuffer sb, boolean[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1)
                sb.append(", ");
        }
        sb.append(']');
    }

    private static void byteArrayAppend(StringBuffer sb, byte[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1)
                sb.append(", ");
        }
        sb.append(']');
    }

    private static void charArrayAppend(StringBuffer sb, char[] a) {
        Arrays.toString(a);
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1)
                sb.append(", ");
        }
        sb.append(']');
    }

    private static void shortArrayAppend(StringBuffer sb, short[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1)
                sb.append(", ");
        }
        sb.append(']');
    }

    private static void intArrayAppend(StringBuffer sb, int[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1)
                sb.append(", ");
        }
        sb.append(']');
    }

    private static void longArrayAppend(StringBuffer sb, long[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1)
                sb.append(", ");
        }
        sb.append(']');
    }

    private static void floatArrayAppend(StringBuffer sb, float[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1)
                sb.append(", ");
        }
        sb.append(']');
    }

    private static void doubleArrayAppend(StringBuffer sb, double[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1)
                sb.append(", ");
        }
        sb.append(']');
    }

    private void log(Level level, String message, Throwable t) {
        PrintStream output = this.output;
        if (output == null) {
            output = System.out;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(getTimestamp());
        sb.append(" ");
        sb.append(name);
        sb.append(" [");
        sb.append(level.name());
        sb.append("]");
        sb.append(" - ");
        if (message != null) {
            sb.append(message);
        }
        sb.append(LINE_SEPARATOR);
        output.print(sb.toString());
        if (t != null) {
            t.printStackTrace(output);
        }
        output.flush();
    }

    private String getTimestamp() {
        DateFormat formatter = FastDateFormat.getDateFormat();
        return formatter.format(new Date(System.currentTimeMillis()));
    }
}
