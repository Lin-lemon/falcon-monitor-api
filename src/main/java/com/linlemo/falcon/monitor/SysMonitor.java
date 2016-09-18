package com.linlemo.falcon.monitor;

import com.linlemo.falcon.monitor.bean.ComboMonitorItem;
import com.linlemo.falcon.monitor.bean.SingleMonitorItem;
import com.linlemo.falcon.monitor.bean.TomcatInformations;
import com.linlemo.falcon.monitor.util.MonitorUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lin.zhao
 * Email: linlemo@gmail.com
 * Date: 16/8/30
 * Time: 14:30
 */
public class SysMonitor {

    private static final String JVM_PREFIX = "JVM_";

    private static final String TOMCAT_PREFIX = "TOMCAT_";

    private static Map<String, ComboMonitorItem> comboSysItems = new HashMap<String, ComboMonitorItem>();

    private static Map<String, SingleMonitorItem> singleSysItems = new HashMap<String, SingleMonitorItem>();


    // 生成jvm监控指标
    protected static void generateJVMMetrics(Map<String, Number> ret) {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        ret.put("JVM_Thread_Count", threadBean.getThreadCount());
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean bean : gcBeans) {
            ComboMonitorItem.computeComboMetric(comboSysItems, JVM_PREFIX + bean.getName(),
                    ret, bean.getCollectionCount(), bean.getCollectionTime());
        }
    }


    // 生成tomcat监控指标
    protected static void generateTomcatMetrics(Map<String, Number> ret) {
        List<TomcatInformations> list = TomcatInformations.buildTomcatInformationsList();
        for (TomcatInformations tomcatInfo : list) {
            // 获得tomcat每个线程池的状态
            String tomcatThreadPoolName = StringUtils.trimToNull(tomcatInfo.getName());
            if (StringUtils.isEmpty(tomcatThreadPoolName)) {
                // 线程池名称不存在的忽略, which is almost impossible
                continue;
            }
            final int maxThreads = tomcatInfo.getMaxThreads(); // 线程池最大线程数
            final int currentThreadCount = tomcatInfo.getCurrentThreadCount(); // 线程池当前线程数
            final int currentThreadsBusy = tomcatInfo.getCurrentThreadsBusy(); // 线程池当前活跃线程数
            final long bytesRecv = tomcatInfo.getBytesReceived(); // 线程池收到的字节数
            final long bytesSent = tomcatInfo.getBytesSent(); // 线程池发送的字节数
            final int requestCount = tomcatInfo.getRequestCount(); // 处理的总的请求次数
            final int errorCount = tomcatInfo.getErrorCount(); // 处理的总的失败次数
            final long processingTime = tomcatInfo.getProcessingTime(); // 处理所有请求的总的时间
            // 记录当前配置线程池最大线程数
            ret.put(MonitorUtil.makeKeyName(TOMCAT_PREFIX + tomcatThreadPoolName + "_maxThread_Value"), maxThreads);
            // 记录当前线程池线程数
            ret.put(MonitorUtil.makeKeyName(TOMCAT_PREFIX + tomcatThreadPoolName + "_currentThread_Value"), currentThreadCount);
            // 记录当前线程池线程繁忙数
            ret.put(MonitorUtil.makeKeyName(TOMCAT_PREFIX + tomcatThreadPoolName + "_currentThreadsBusy_Value"), currentThreadsBusy);
            // 记录指定时间段内收到的字节数
            SingleMonitorItem.computeSingleMetric(singleSysItems, MonitorUtil.makeKeyName(TOMCAT_PREFIX + tomcatThreadPoolName + "_bytesReceived"),
                    ret, bytesRecv);
            // 记录指定时间段内发送出的字节数
            SingleMonitorItem.computeSingleMetric(singleSysItems, MonitorUtil.makeKeyName(TOMCAT_PREFIX + tomcatThreadPoolName + "_bytesSent"), ret,
                    bytesSent);
            // 记录指定时间段内的失败请求个数
            SingleMonitorItem.computeSingleMetric(singleSysItems, MonitorUtil.makeKeyName(TOMCAT_PREFIX + tomcatThreadPoolName + "_error"), ret,
                    errorCount);
            // 记录指定时间段内处理的请求个数以及平均响应时间
            ComboMonitorItem.computeComboMetric(comboSysItems, MonitorUtil.makeKeyName(TOMCAT_PREFIX + tomcatThreadPoolName + "_request"), ret,
                    requestCount, processingTime);
        }
    }
}
