package com.linlemo.falcon.monitor;

import com.google.common.collect.Maps;
import com.linlemo.falcon.monitor.config.MonitorConstant;
import com.linlemo.falcon.monitor.util.MonitorSendUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: lin.zhao
 * Email: linlemo@gmail.com
 * Date: 16/8/30
 * Time: 14:12
 */
public class MonitorPushScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MonitorPushScheduler.class);

    private static final ScheduledExecutorService MonitorScheduler = Executors.newSingleThreadScheduledExecutor(
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "MonitorPush-Schduler");
                }

            });


    public static void init() {
        logger.info("MonitorPushScheduler start");
        MonitorScheduler.scheduleAtFixedRate(new MonitorPushRunner(), MonitorConstant.MONITOR_PUSH_PERIOD_TIME,
                MonitorConstant.MONITOR_PUSH_PERIOD_TIME, TimeUnit.SECONDS);
    }

    static class MonitorPushRunner implements Runnable {

        @Override
        public void run() {
            try {
                Map<String, Number> monitors = Maps.newHashMap();
                if (MonitorConstant.COLLECT_JVM_INFO) {
                    SysMonitor.generateJVMMetrics(monitors);
                }
                if (MonitorConstant.COLLECT_TOMCAT_INFO) {
                    SysMonitor.generateTomcatMetrics(monitors);
                }
                FalconMonitor.generateBusinessMetrics(monitors);
                MonitorSendUtil.send(monitors);
            } catch (Exception e) {
                logger.error("run MonitorPushRunner exception", e);
            }
        }
    }

    public static void destroy() {
        MonitorScheduler.shutdownNow();
    }

}
