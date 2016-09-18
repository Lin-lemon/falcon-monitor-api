package com.flightroutes.flight.monitor.bean;

import com.flightroutes.flight.monitor.config.MonitorConstant;
import com.flightroutes.flight.monitor.util.MonitorUtil;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lin.zhao
 * Email: linlemo@gmail.com
 * Date: 16/8/30
 * Time: 14:10
 */
public class ComboMonitorItem {

    private long count;

    private long time;

    public synchronized void add(long count, long time) {
        this.count += count;
        this.time += time;
    }

    public synchronized ComboResultItem dumpAndClear() {
        long currCount = this.count;
        long currTime = this.time;
        this.count = 0;
        this.time = 0;
        return new ComboResultItem(calAvgCount(currCount), calAvgTime(currCount, currTime));
    }

    private synchronized ComboResultItem computeAndSwap(long count, long time) {
        long tmpCount = count - this.count;
        long tmpTime = time - this.time;
        this.count = count;
        this.time = time;
        return new ComboResultItem(tmpCount, calAvgTime(tmpCount, tmpTime));
    }

    public static void computeComboMetric(Map<String, ComboMonitorItem> items, String key,
                                          Map<String, Number> resultMap, long count, long time) {
        ComboMonitorItem item = items.get(key);
        if (item == null) {
            item = new ComboMonitorItem();
            item.add(count, time);
            items.put(key, item);
        }
        ComboResultItem result = item.computeAndSwap(count, time);
        resultMap.put(MonitorUtil.makeCountKeyName(key), result.getCount());
        resultMap.put(MonitorUtil.makeTimeKeyName(key), result.getTime());
    }

    private static double calAvgTime(long count, long time) {
        return count == 0 ? 0D : time * 1.0D / count;
    }
    
    private static double calAvgCount(long count){
    	return count == 0 ? 0D : count * 1.0D / MonitorConstant.MONITOR_PUSH_PERIOD_TIME;
    }
}
