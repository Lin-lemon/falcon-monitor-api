package com.flightroutes.flight.monitor.bean;

import com.flightroutes.flight.monitor.config.MonitorConstant;
import com.flightroutes.flight.monitor.util.MonitorUtil;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: lin.zhao
 * Email: linlemo@gmail.com
 * Date: 16/8/30
 * Time: 14:07
 */
public class SingleMonitorItem {

    private AtomicLong counter = new AtomicLong();

    public void add(long count) {
        counter.addAndGet(count);
    }

    public double dumpAndClear() {
    	long count = counter.getAndSet(0);
        return count * 1.0D / MonitorConstant.MONITOR_PUSH_PERIOD_TIME;
    }

    private long computeAndSwap(long count) {
        long oldCount = counter.getAndSet(count);
        return count - oldCount;
    }

    public static void computeSingleMetric(Map<String, SingleMonitorItem> items, String key,
                                           Map<String, Number> resultMap, long value) {
        SingleMonitorItem item = items.get(key);
        if (item == null) {
            item = new SingleMonitorItem();
            item.add(value);
            items.put(key, item);
        }
        resultMap.put(MonitorUtil.makeCountKeyName(key), item.computeAndSwap(value));
    }
}
