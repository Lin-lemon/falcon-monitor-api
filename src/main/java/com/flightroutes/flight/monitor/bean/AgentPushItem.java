package com.flightroutes.flight.monitor.bean;

public class AgentPushItem {

    //监控项
    private String metric;

    //当前机器的机器名
    private String endpoint;

    //时间戳，单位：秒
    private long timestamp;

    //监控项的值
    private double value;

    //数据采集项的汇报周期,单位 s，最小为30
    private int step;

    //COUNTER:计时器，GAUGE:原值
    private String counterType;

    //监控项的标签
    private String tags;

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getCounterType() {
        return counterType;
    }

    public void setCounterType(String counterType) {
        this.counterType = counterType;
    }

    public String getTags() {
        return tags;
    }

    public AgentPushItem buildTags(String key, String content) {
        String tag = key.concat("=").concat(content).concat(",");
        if (this.tags == null) {
            this.tags = tag;
        } else {
            this.tags = this.tags.concat(tag);
        }
        return this;
    }
    
    public AgentPushItem removeLastChar(){
    	if(this.tags != null){
    		this.tags = this.tags.substring(0, this.tags.length()-1);
    	}
    	return this;
    }

    @Override
    public String toString() {
        return "PushItem{" +
                "metric='" + metric + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", timestamp=" + timestamp +
                ", value=" + value +
                ", step=" + step +
                ", counterType='" + counterType + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}
