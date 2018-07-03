package com.zfm.mirco.gleaning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ClassUtils;


/**
 * Sprng Boot项目入口函数
 * @author zm
 *
 */
@SpringBootApplication
public class App{
	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
		new Thread().sleep(86400000);	//睡眠24小时后再关闭
	}
}
