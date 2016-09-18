package com.flightroutes.flight.monitor.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class MachineUtil {

    private static final Logger logger = LoggerFactory.getLogger(MachineUtil.class);

    public static String getMachineName() {
        try {
            return InetAddress.getLocalHost().getHostName().toString();// 获得本机名称
        } catch (Exception e) {
            logger.warn("getMachineName error");
        }
        return StringUtils.EMPTY;
    }

    public static String getMachineIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            logger.warn("getMachineIp error");
        }
        return StringUtils.EMPTY;
    }

    public static void main(String[] args) {
        System.out.println(MachineUtil.getMachineName());
        System.out.println(MachineUtil.getMachineIp());
    }

}
