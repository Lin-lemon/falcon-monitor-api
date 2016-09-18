package com.linlemo.falcon.monitor.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.linlemo.falcon.monitor.bean.AgentPushItem;
import com.linlemo.falcon.monitor.bean.Tag;
import com.linlemo.falcon.monitor.config.MonitorConstant;
import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MonitorSendUtil {

    private static final String MACHINE_NAME = MachineUtil.getMachineName();

    public static void send(Map<String, Number> monitors) throws IOException {
        List<AgentPushItem> agentPushItems = transfromToPushItem(monitors);
        if (CollectionUtils.isEmpty(agentPushItems)) {
            return;
        }

        HttpUtil.post(JSONObject.toJSONString(agentPushItems), MonitorConstant.AGENT_COLLECTION_URL);
    }

    private static List<AgentPushItem> transfromToPushItem(Map<String, Number> items) {
        List<AgentPushItem> ret = Lists.newArrayListWithCapacity(items.size());
        long nowTimeStamp = new Date().getTime() / 1000;

        for (Map.Entry<String, Number> entry : items.entrySet()) {
            AgentPushItem agentPushItem = new AgentPushItem();
            agentPushItem.setMetric(entry.getKey());
            agentPushItem.setEndpoint(MACHINE_NAME);
            agentPushItem.setCounterType(MonitorConstant.COUNTER_TYPE);
            agentPushItem.setStep(MonitorConstant.MONITOR_PUSH_PERIOD_TIME);
            agentPushItem.setTimestamp(nowTimeStamp);
            agentPushItem.setValue(entry.getValue().doubleValue());
            agentPushItem.buildTags(Tag.KEY.getKey(), entry.getKey())
                    .buildTags(Tag.SERVICE.getKey(), MonitorConstant.SERVICE_NAME)
                    .removeLastChar();

            ret.add(agentPushItem);
        }
        return ret;
    }

}
