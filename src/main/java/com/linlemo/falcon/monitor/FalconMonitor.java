package com.linlemo.falcon.monitor;

import com.linlemo.falcon.monitor.bean.ComboMonitorItem;
import com.linlemo.falcon.monitor.bean.SingleMonitorItem;
import com.linlemo.falcon.monitor.util.MonitorUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: lin.zhao
 * Email: linlemo@gmail.com
 * Date: 16/8/30 Time: 14:05
 */
public class FalconMonitor {

    // count 的监控指标收集
    private static ConcurrentHashMap<String, SingleMonitorItem> singleItems = new ConcurrentHashMap<String, SingleMonitorItem>();

    // count time的监控指标收集
    private static ConcurrentHashMap<String, ComboMonitorItem> comboItems = new ConcurrentHashMap<String, ComboMonitorItem>();


    static {
        MonitorPushScheduler.init();
    }

    /**
     * 收集单个监控指标，增加count=1
     *
     * @param name
     */
    public static void recordOne(String name) {
        recordMany(name, 1L);
    }

    public static void recordOne(String name, long time) {
        recordMany(name, 1L, time);
    }

    /**
     * 收集单个监控指标增，增加count=count
     *
     * @param name
     * @param count
     */
    public static void incrRecord(String name, long count) {
        recordMany(name, count);
    }

    public static void incrRecord(String name, long count, long time) {
        recordMany(name, count, time);
    }

    private static void recordMany(String name, long count) {
        SingleMonitorItem item = singleItems.get(name);
        if (item == null) {
            item = new SingleMonitorItem();
            SingleMonitorItem old = singleItems.putIfAbsent(name, item);
            if (old != null) {
                item = old;
            }
        }
        item.add(count);
    }

    private static void recordMany(String name, long count, long time) {
        ComboMonitorItem item = comboItems.get(name);
        if (item == null) {
            item = new ComboMonitorItem();
            ComboMonitorItem old = comboItems.putIfAbsent(name, item);
            if (old != null) {
                item = old;
            }
        }
        item.add(count, time);
    }


    // 按分钟切割业务监控指标
    protected static void generateBusinessMetrics(Map<String, Number> ret) {
        // 生成_count和_time监控
        for (Map.Entry<String, SingleMonitorItem> entry : singleItems.entrySet()) {
            SingleMonitorItem item = entry.getValue();
            ret.put(MonitorUtil.makeCountKeyName(entry.getKey()), item.dumpAndClear());
        }

        for (Map.Entry<String, ComboMonitorItem> entry : comboItems.entrySet()) {
            ComboMonitorItem.ComboResultItem item = entry.getValue().dumpAndClear();
            ret.put(MonitorUtil.makeCountKeyName(entry.getKey()), item.getCount());
            ret.put(MonitorUtil.makeTimeKeyName(entry.getKey()), item.getTime());
        }
    }
}
