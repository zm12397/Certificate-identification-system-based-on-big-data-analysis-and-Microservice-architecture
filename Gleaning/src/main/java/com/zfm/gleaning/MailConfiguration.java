package com.zfm.gleaning;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件配置类
 * @author zm
 *
 */
@ConfigurationProperties(prefix = "mail")
@Component
public class MailConfiguration {
	// 邮件主机
	private static String host;
	// 端口，默认25
	private static Integer port;
	// 编码
	private static String encoding;
	// 是否启用tls
	private static Boolean starttlsEnable;
	// 是否授权
	private static Boolean auth;
	// 发送邮箱的用户名
	private static String username;
	// 发送邮箱的授权码
	private static String password;
	// 超时判定时间
	private static Long timeout;
	// 发送地址
	private static String from;

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		MailConfiguration.host = host;
	}

	public static Integer getPort() {
		return port;
	}

	public static void setPort(Integer port) {
		MailConfiguration.port = port;
	}

	public static String getEncoding() {
		return encoding;
	}

	public static void setEncoding(String encoding) {
		MailConfiguration.encoding = encoding;
	}

	public static Boolean getStarttlsEnable() {
		return starttlsEnable;
	}

	public static void setStarttlsEnable(Boolean starttlsEnable) {
		MailConfiguration.starttlsEnable = starttlsEnable;
	}

	public static Boolean getAuth() {
		return auth;
	}

	public static void setAuth(Boolean auth) {
		MailConfiguration.auth = auth;
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		MailConfiguration.username = username;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		MailConfiguration.password = password;
	}

	public static Long getTimeout() {
		return timeout;
	}

	public static void setTimeout(Long timeout) {
		MailConfiguration.timeout = timeout;
	}

	public static String getFrom() {
		return from;
	}

	public static void setFrom(String from) {
		MailConfiguration.from = from;
	}

}
