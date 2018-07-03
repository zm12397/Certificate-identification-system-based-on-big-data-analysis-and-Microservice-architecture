package com.zfm.mirco.gleaning;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件配置类
 * 
 * @author zm
 *
 */
@ConfigurationProperties(prefix = "msql-hdfs-mirco")
@Component
public class MircoConfiguration {
	// 主机
	private static String address;
	// 端口，默认8080
	private static Integer port;
	// 服务名
	private static String name;
	// 节点路径
	private static String path;
	public static String getAddress() {
		return address;
	}
	public static void setAddress(String address) {
		MircoConfiguration.address = address;
	}
	public static Integer getPort() {
		return port;
	}
	public static void setPort(Integer port) {
		MircoConfiguration.port = port;
	}
	public static String getName() {
		return name;
	}
	public static void setName(String name) {
		MircoConfiguration.name = name;
	}
	public static String getPath() {
		return path;
	}
	public static void setPath(String path) {
		MircoConfiguration.path = path;
	}

	
}
