package com.tobipristupin.simplerun.utils;

import java.util.logging.Level;

/**
 * Wrapper around java.util.logger
 */

public class LogWrapper {

    public static void log(Level level, String tag, String msg){
        java.util.logging.Logger.getLogger(tag).log(level, msg);
    }

    public static void warn(String tag, String msg){
        java.util.logging.Logger.getLogger(tag).log(Level.WARNING, msg);
    }

    public static void info(String tag, String msg){
        java.util.logging.Logger.getLogger(tag).log(Level.INFO, msg);
    }
}
