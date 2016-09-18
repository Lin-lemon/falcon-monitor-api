package com.flightroutes.flight.monitor.bean;

/**
 * Created with IntelliJ IDEA.
 * User: lin.zhao
 * Email: linlemo@gmail.com
 * Date: 16/8/30
 * Time: 18:00
 */
public class ComboResultItem {

    private final double count;

    private final double time;

    ComboResultItem(double count, double time) {
        this.count = count;
        this.time = time;
    }

    public double getCount() {
        return count;
    }

    public double getTime() {
        return time;
    }
}
