package com.flightroutes.flight.monitor.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: lin.zhao
 * Email: linlemo@gmail.com
 * Date: 16/9/1
 * Time: 16:22
 */
public class MonitorConstant {

    private static final Logger logger = LoggerFactory.getLogger(MonitorConstant.class);

    /**
     * 服务名,用来区分不同服务的监控
     */
    public static final String SERVICE_NAME = FRMonitorConfig.getItem("monitor.service.name", "ServiceNameDemo");

    /**
     * 负责收集监控的agent的url，用来推送业务收集的自定义监控到此处
     */
    public static final String AGENT_COLLECTION_URL = FRMonitorConfig.getItem("monitor.agent.collection.url", "http://127.0.0.1:1988/v1/push");

    /**
     * 定时推送监控信息到agent的间隔时间,单位:秒，默认60秒
     */
    public static final int MONITOR_PUSH_PERIOD_TIME = FRMonitorConfig.getIntItem("monitor.push.period.time", 60);

    /**
     * 是否收集tomcat信息 0不收集;1收集，默认不收集
     */
    public static final boolean COLLECT_TOMCAT_INFO = FRMonitorConfig.getIntItem("monitor.collect.tomcatinfo", 0) == 1;

    /**
     * 是否收集jvm信息 0不收集;1收集，默认不收集
     */
    public static final boolean COLLECT_JVM_INFO = FRMonitorConfig.getIntItem("monitor.collect.jvminfo", 0) == 1;

    public static final String COUNTER_TYPE = "GAUGE";

    private static class FRMonitorConfig {

        private static final String CONFIG_NAME = "frMonitor.properties";

        private static Properties monitorConfig = new Properties();

        static {
            try {
                logger.info(" ====================== begin read  frMonitor.properties ==================");
                InputStream in = MonitorConstant.class.getClassLoader().getResourceAsStream(CONFIG_NAME);
                if (in == null) {
                    in = MonitorConstant.class.getClassLoader().getResourceAsStream("props/".concat(CONFIG_NAME));
                }
                monitorConfig.load(in);
                in.close();
                logger.info(monitorConfig.toString());
                logger.info(" ====================== end read  frMonitor.properties ==================");
            } catch (Exception e) {
            }
        }

        private static String getItem(String key, String defaultStr) {
            if (StringUtils.isBlank(key) || monitorConfig == null) {
                return defaultStr;
            }
            return monitorConfig.getProperty(key, defaultStr);
        }

        private static int getIntItem(String key, int defaultValue) {
            if (StringUtils.isBlank(key) || monitorConfig == null) {
                return defaultValue;
            }
            return NumberUtils.toInt(monitorConfig.getProperty(key), defaultValue);
        }
    }

}
