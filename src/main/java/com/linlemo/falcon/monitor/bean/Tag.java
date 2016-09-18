package com.linlemo.falcon.monitor.bean;

/**
 * Created with IntelliJ IDEA.
 * User: lin.zhao
 * Email: linlemo@gmail.com
 * Date: 16/9/1
 * Time: 19:12
 * <p/>
 * 用来做tag分组及搜索
 */
public enum Tag {

    KEY("key", "监控名"),

    SERVICE("service", "服务名");

    private String key;

    private String desc;

    Tag(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }
}

