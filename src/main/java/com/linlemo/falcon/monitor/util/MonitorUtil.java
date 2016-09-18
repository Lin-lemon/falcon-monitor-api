package com.linlemo.falcon.monitor.util;

import com.linlemo.falcon.monitor.config.MonitorConstant;

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

    public static double calAvgCount(long count) {
        return count == 0 ? 0D : count * 1.0D / MonitorConstant.MONITOR_PUSH_PERIOD_TIME;
    }
}
