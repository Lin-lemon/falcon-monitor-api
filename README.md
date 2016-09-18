####1.使用场景
结合小米的open-falcon监控系统使用，部署好agent之后，只能收集模版上的机器信息，比如cpu/内存占用率等，但是无法收集业务系统自定义的监控。这个jar就是解决业务系统自定义收集监控的功能。
####2.主要的三个功能点
1.封装了接口，方便业务系统使用

2.本地统计收集到的监控信息

3.定时重置监控内容并推送监控信息到agent中。

备注：可以选择收集jvm和tomcat信息。

####3.如何使用
#####3.1 配置pom依赖
```
        <dependency>
            <groupId>com.flightroutes.flight</groupId>
            <artifactId>fr-f-monitor</artifactId>
            <version>1.0.1</version>
        </dependency>
```
#####3.2添加配置文件 frMonitor.properties
配置文件内容

```
###服务名,用来区分不同服务的监控
monitor.service.name=fr_f_monitor
###负责收集监控的agent的url，用来推送业务收集的自定义监控到此处
monitor.agent.collection.url=http://127.0.0.1:1988/v1/push
###定时推送监控信息到agent的间隔时间,单位:秒
monitor.push.period.time=60
###是否收集tomcat信息 0不收集;1收集，默认不收集
monitor.collect.tomcatinfo=0
###是否收集jvm信息 0不收集;1收集，默认不收集
monitor.collect.jvminfo=0
```
#####3.3业务系统如何使用
共有4种收集方式，可根据需要使用不同的方式收集。

```
        long start = System.currentTimeMillis();
        // do sth
        long cost = System.currentTimeMillis() - start;
        // 收集监控 计数＋1
        FalconMonitor.recordOne("monitor_key_name");
        // 收集监控 计数＋1; 时间＋cost
        FalconMonitor.recordOne("monitor_key_name", cost);

        // 收集监控 计数＋3
        FalconMonitor.incrRecord("monitor_key_name", 3);
        // 收集监控 计数＋3; 时间＋cost
        FalconMonitor.incrRecord("monitor_key_name", 3, cost);
```



