package com.linlemo.falcon.monitor.bean;

import javax.management.*;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: lin.zhao
 * Email: linlemo@gmail.com
 * Date: 16/8/30
 * Time: 10:53
 */
public class TomcatInformations implements Serializable {

    private static final long serialVersionUID = -513883436430360937L;

    private static final boolean TOMCAT_USED = System.getProperty("catalina.home") != null;

    private static final List<ObjectName> THREAD_POOLS = new ArrayList<ObjectName>();

    private static final List<ObjectName> GLOBAL_REQUEST_PROCESSORS = new ArrayList<ObjectName>();

    private final String name;

    private final int maxThreads;

    private final int currentThreadCount;

    private final int currentThreadsBusy;

    private final long bytesReceived;

    private final long bytesSent;

    private final int requestCount;

    private final int errorCount;

    private final long processingTime;

    private final long maxTime;
    private static final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

    static Set<ObjectName> getTomcatThreadPools() throws MalformedObjectNameException {
        return mbeanServer.queryNames(new ObjectName("Catalina:type=ThreadPool,*"), null);
    }

    static Set<ObjectName> getTomcatGlobalRequestProcessors() throws MalformedObjectNameException {
        return mbeanServer.queryNames(new ObjectName("*:type=GlobalRequestProcessor,*"), null);
    }

    static Object getAttribute(ObjectName name, String attribute) throws JMException {
        return mbeanServer.getAttribute(name, attribute);
    }

    static MBeanInfo getMBeanInfo(ObjectName name) throws JMException {
        return mbeanServer.getMBeanInfo(name);
    }

    private TomcatInformations(ObjectName threadPool) throws JMException {
        super();
        name = threadPool.getKeyProperty("name");
        maxThreads = (Integer) getAttribute(threadPool, "maxThreads");
        currentThreadCount = (Integer) getAttribute(threadPool, "currentThreadCount");
        currentThreadsBusy = (Integer) getAttribute(threadPool, "currentThreadsBusy");
        ObjectName grp = null;
        for (final ObjectName globalRequestProcessor : GLOBAL_REQUEST_PROCESSORS) {
            if (name.equals(globalRequestProcessor.getKeyProperty("name"))) {
                grp = globalRequestProcessor;
                break;
            }
        }
        if (grp != null) {
            bytesReceived = (Long) getAttribute(grp, "bytesReceived");
            bytesSent = (Long) getAttribute(grp, "bytesSent");
            requestCount = (Integer) getAttribute(grp, "requestCount");
            errorCount = (Integer) getAttribute(grp, "errorCount");
            processingTime = (Long) getAttribute(grp, "processingTime");
            maxTime = (Long) getAttribute(grp, "maxTime");
        } else {
            bytesReceived = 0;
            bytesSent = 0;
            requestCount = 0;
            errorCount = 0;
            processingTime = 0;
            maxTime = 0;
        }
    }

    public static List<TomcatInformations> buildTomcatInformationsList() {
        if (!TOMCAT_USED) {
            return Collections.emptyList();
        }
        try {
            synchronized (THREAD_POOLS) {
                if (THREAD_POOLS.isEmpty() || GLOBAL_REQUEST_PROCESSORS.isEmpty()) {
                    initMBeans();
                }
            }
            final List<TomcatInformations> tomcatInformationsList = new ArrayList<TomcatInformations>(
                    THREAD_POOLS.size());

            for (final ObjectName threadPool : THREAD_POOLS) {
                tomcatInformationsList.add(new TomcatInformations(threadPool));
            }
            return tomcatInformationsList;
        } catch (final InstanceNotFoundException e) {
            return Collections.emptyList();
        } catch (final JMException e) {

            throw new IllegalStateException(e);
        }
    }

    static void initMBeans() throws MalformedObjectNameException {

        THREAD_POOLS.clear();
        GLOBAL_REQUEST_PROCESSORS.clear();
        THREAD_POOLS.addAll(getTomcatThreadPools());
        GLOBAL_REQUEST_PROCESSORS.addAll(getTomcatGlobalRequestProcessors());
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public int getMaxThreads() {
        return maxThreads;
    }

    /**
     * @return
     */
    public int getCurrentThreadCount() {
        return currentThreadCount;
    }

    /**
     * @return
     */
    public int getCurrentThreadsBusy() {
        return currentThreadsBusy;
    }

    /**
     * @return
     */
    public long getBytesReceived() {
        return bytesReceived;
    }

    /**
     * @return
     */
    public long getBytesSent() {
        return bytesSent;
    }

    /**
     * @return
     */
    public int getRequestCount() {
        return requestCount;
    }

    /**
     * @return
     */
    public int getErrorCount() {
        return errorCount;
    }

    /**
     * @return
     */
    public long getProcessingTime() {
        return processingTime;
    }

    /**
     * @return
     */
    public long getMaxTime() {
        return maxTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[name=" + getName() + ", maxThreads=" + getMaxThreads()
                + ", currentThreadCount=" + getCurrentThreadCount() + ", currentThreadsBusy=" + getCurrentThreadsBusy()
                + ", bytesReceived=" + getBytesReceived() + ", bytesSent=" + getBytesSent() + ", requestCount="
                + getRequestCount() + ", errorCount=" + getErrorCount() + ", processingTime=" + getProcessingTime()
                + ", maxTime=" + getMaxTime() + ']';
    }
}
