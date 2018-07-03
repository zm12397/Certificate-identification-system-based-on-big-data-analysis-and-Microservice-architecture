package com.zfm.gleaning;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件配置类
 * @author zm
 *
 */
@ConfigurationProperties(prefix = "zookeeper")
@Component
public class ZookeeperConfiguration {
	// 主机
	private static String ip;
	// 端口，默认4180
	private static Integer port;
	// 重连时间
	private static int retry;
	public static String getIp() {
		return ip;
	}
	public static void setIp(String ip) {
		ZookeeperConfiguration.ip = ip;
	}
	public static Integer getPort() {
		return port;
	}
	public static void setPort(Integer port) {
		ZookeeperConfiguration.port = port;
	}
	public static int getRetry() {
		return retry;
	}
	public static void setRetry(int retry) {
		ZookeeperConfiguration.retry = retry;
	}
}
