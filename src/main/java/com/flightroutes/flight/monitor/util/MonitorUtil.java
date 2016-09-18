package com.flightroutes.flight.monitor.util;

/**
 * Created with IntelliJ IDEA.
 * User: lin.zhao
 * Email: linlemo@gmail.com
 * Date: 16/8/30 Time: 14:17
 */
public class MonitorUtil {

    public static String makeKeyName(String name) {
        return name.replace(' ', '_');
    }

    public static String makeCountKeyName(String name) {
        return makeKeyName(name).concat("_count");
    }

    public static String makeTimeKeyName(String name) {
        return makeKeyName(name).concat("_time");
    }
}
